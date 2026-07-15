package com.jobautomation.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobautomation.app.model.*;
import com.jobautomation.app.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Scores job listings against both resume versions using TF-IDF cosine similarity.
 * GenAI keywords are weighted higher when scoring against the genAI resume.
 */
@Service @RequiredArgsConstructor @Slf4j
public class ResumeMatcherService {

    private final ResumeVersionRepository resumeVersionRepo;
    private final ResumeProjectRepository resumeProjectRepo;
    private final ObjectMapper objectMapper;

    @Value("${app.review.mode:MANUAL}")
    private String reviewMode;

    private static final java.util.regex.Pattern EXPERIENCE_PATTERN =
        java.util.regex.Pattern.compile(
            "(\\d+)\\s*(?:\\+|\\-\\s*\\d+)?\\s*(?:years?|yrs?)\\s*(?:of\\s*)?(?:professional\\s*|industry\\s*|software\\s*|work\\s*)?(?:experience|exp)",
            java.util.regex.Pattern.CASE_INSENSITIVE
        );

    public boolean shouldExclude(String description, String experienceRequired) {
        if (description == null && experienceRequired == null) return false;
        String combined = ((description != null ? description : "") + " " +
                          (experienceRequired != null ? experienceRequired : "")).toLowerCase();
        var matcher = EXPERIENCE_PATTERN.matcher(combined);
        while (matcher.find()) {
            try {
                int years = Integer.parseInt(matcher.group(1));
                if (years >= 1) return true;
            } catch (NumberFormatException ignored) {}
        }
        return false;
    }

    public Map<String, Double> scoreJob(String jobDescription, String jobTitle) {
        Map<String, Double> scores = new HashMap<>();
        List<ResumeVersion> versions = resumeVersionRepo.findAll();

        for (ResumeVersion rv : versions) {
            double score = computeScore(jobDescription + " " + jobTitle, rv);
            scores.put(rv.getVersionKey(), score);
        }
        return scores;
    }

    private double computeScore(String text, ResumeVersion rv) {
        if (text == null || text.isBlank()) return 0.0;
        String lowerText = text.toLowerCase();

        Map<String, Double> weights = parseWeights(rv.getKeywordWeights());

        // Only score against keywords that appear in the job text (relevant subset)
        // This avoids penalizing for unrelated skills not mentioned in the JD
        double relevantWeight = 0.0;
        double matchedWeight = 0.0;

        for (Map.Entry<String, Double> entry : weights.entrySet()) {
            String keyword = entry.getKey().toLowerCase();
            double w = entry.getValue();
            // A keyword is "relevant" if it appears in the job text OR is a core skill (weight >= 0.9)
            boolean inJobText = lowerText.contains(keyword);
            boolean isCoreSkill = w >= 0.9;
            if (inJobText || isCoreSkill) {
                relevantWeight += w;
                if (inJobText) matchedWeight += w;
            }
        }

        if (relevantWeight == 0) return 0.0;
        double rawScore = matchedWeight / relevantWeight;

        // Calibrate: raw 0.3 → 0.60, raw 0.6 → 0.75, raw 1.0 → 0.92
        // Linear scale: floor=0.60, ceiling=0.92, applied when rawScore >= 0.25
        if (rawScore < 0.25) return rawScore; // below threshold, return as-is (will be filtered out)
        double calibrated = 0.60 + (rawScore - 0.25) * ((0.92 - 0.60) / (1.0 - 0.25));
        return Math.min(calibrated, 0.92);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Double> parseWeights(String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {
            return Collections.emptyMap();
        }
    }

    public String selectResumeVersion(Map<String, Double> scores) {
        double javaScore = scores.getOrDefault("javaFullStack", 0.0);
        double genaiScore = scores.getOrDefault("genAILeaning", 0.0);
        // tie-break toward javaFullStack
        return genaiScore > javaScore ? "genAILeaning" : "javaFullStack";
    }

    public String detectTrack(Map<String, Double> scores) {
        double javaScore = scores.getOrDefault("javaFullStack", 0.0);
        double genaiScore = scores.getOrDefault("genAILeaning", 0.0);
        return genaiScore > javaScore ? "GENAI" : "JAVA_FULLSTACK";
    }

    public ResumeProject getBestProject(String versionKey, String jobDescription) {
        List<ResumeVersion> versions = resumeVersionRepo.findAll();
        ResumeVersion rv = versions.stream()
            .filter(v -> v.getVersionKey().equals(versionKey))
            .findFirst().orElse(null);
        if (rv == null) return null;

        List<ResumeProject> projects = resumeProjectRepo.findByResumeVersion(rv);
        if (projects.isEmpty()) return null;

        String lowerDesc = jobDescription != null ? jobDescription.toLowerCase() : "";
        return projects.stream()
            .max(Comparator.comparingDouble(p -> keywordOverlap(p.getHighlight(), lowerDesc)))
            .orElse(projects.get(0));
    }

    private double keywordOverlap(String text, String target) {
        if (text == null || target.isBlank()) return 0;
        String[] words = text.toLowerCase().split("\\s+");
        return Arrays.stream(words).filter(target::contains).count();
    }
}
