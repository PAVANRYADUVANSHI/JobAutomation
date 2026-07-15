package com.jobautomation.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobautomation.app.model.JobListing;
import com.jobautomation.app.model.TargetCompany;
import com.jobautomation.app.repository.JobListingRepository;
import com.jobautomation.app.repository.TargetCompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Pulls job listings from:
 * - Adzuna API (official)
 * - RemoteOK API (official)
 * - Greenhouse public job board API (per company slug)
 * - Lever public postings API (per company slug)
 * - Ashby public job board API (per company slug)
 * - Manual watchlist entries for companies with no API
 *
 * NO scraping. All sources are official public APIs or RSS feeds.
 */
@Service @RequiredArgsConstructor @Slf4j
public class JobAggregatorService {

    private final JobListingRepository jobListingRepo;
    private final TargetCompanyRepository targetCompanyRepo;
    private final ResumeMatcherService matcherService;
    private final ObjectMapper objectMapper;

    @Value("${adzuna.app.id:}") private String adzunaAppId;
    @Value("${adzuna.app.key:}") private String adzunaAppKey;
    @Value("${remoteok.api.url:https://remoteok.com/api}") private String remoteOkUrl;
    @Value("${greenhouse.api.base:https://boards-api.greenhouse.io/v1/boards}") private String greenhouseBase;
    @Value("${lever.api.base:https://api.lever.co/v0/postings}") private String leverBase;
    @Value("${ashby.api.base:https://api.ashbyhq.com/posting-public/job-board}") private String ashbyBase;
    @Value("${app.rate.limit.ms:200}") private long rateLimitMs;

    private final OkHttpClient http = new OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build();

    private static final List<String> JAVA_QUERIES = List.of(
        "Java Full Stack Developer Fresher",
        "Java Developer Fresher",
        "Software Developer Fresher Java",
        "Backend Developer Fresher",
        "Frontend Developer Fresher",
        "SQL Developer Fresher",
        "Database Developer Fresher",
        "Java Full Stack Developer Internship",
        "Software Developer Internship Java",
        "Backend Developer Internship",
        "Frontend Developer Internship",
        "SQL Developer Internship"
    );

    private static final List<String> GENAI_QUERIES = List.of(
        "GenAI Developer Fresher",
        "LLM Developer Fresher",
        "AI Software Developer Fresher",
        "GenAI Developer Internship",
        "AI Full Stack Developer Fresher"
    );

    // Exact allowed title patterns — must match at least one
    private static final List<String> ALLOWED_TITLE_KEYWORDS = List.of(
        "java full stack", "java fullstack", "full stack developer", "fullstack developer",
        "full stack engineer", "fullstack engineer",
        "java developer", "java engineer",
        "software developer", "software engineer",
        "genai developer", "genai engineer", "gen ai developer", "gen ai engineer",
        "llm developer", "llm engineer",
        "ai developer", "ai engineer", "ai full stack",
        "generative ai developer", "generative ai engineer",
        "backend developer", "back end developer",
        "frontend developer", "front end developer",
        "sql developer", "sql engineer", "database developer", "database engineer",
        "intern", "internship"
    );

    // Title must contain one of these to be considered fresher/entry-level
    private static final List<String> FRESHER_TITLE_SIGNALS = List.of(
        "fresher", "freshers", "entry level", "entry-level",
        "junior", "trainee", "associate", "new grad", "graduate",
        "0-1", "intern", "internship"
    );

    private static final List<String> LOCATIONS = List.of("Bangalore", "Remote");

    private static final List<String> BLOCKED_TITLE_PATTERNS = List.of(
        "senior", "sr.", "lead", "principal", "staff",
        "architect", "manager", "head of", "director", "vp ",
        "consultant", "consulting", "solutions engineer", "pre-sales",
        "devrel", "developer relations", "developer advocate",
        "security engineer", "threat", "benchmarking", "data warehouse",
        "data pipeline", "stream compute", "android", "ios ", "mobile",
        "customer forward", "customer success", "account ", "sales ",
        "people team", "hr ", "human resources", "professional services",
        "marketing", "finance intern", "legal ", "operations intern",
        "recruiting", "talent "
    );

    public int fetchAll() {
        int total = 0;
        total += fetchAdzuna();
        total += fetchRemoteOK();
        total += fetchFromTargetCompanies();
        return total;
    }

    // ── Adzuna ──────────────────────────────────────────────────────────────
    private int fetchAdzuna() {
        if (adzunaAppId == null || adzunaAppId.isBlank()) {
            log.info("Adzuna API keys not configured, skipping");
            return 0;
        }
        int count = 0;
        List<String> allQueries = new ArrayList<>(JAVA_QUERIES);
        allQueries.addAll(GENAI_QUERIES);

        for (String query : allQueries) {
            for (String location : LOCATIONS) {
                try {
                    String url = String.format(
                        "https://api.adzuna.com/v1/api/jobs/in/search/1?app_id=%s&app_key=%s" +
                        "&results_per_page=20&what=%s&where=%s&content-type=application/json",
                        adzunaAppId, adzunaAppKey,
                        encode(query), encode(location));

                    JsonNode root = get(url);
                    if (root == null) continue;

                    for (JsonNode job : root.path("results")) {
                        String title = job.path("title").asText();
                        String company = job.path("company").path("display_name").asText();
                        String desc = job.path("description").asText();
                        String applyUrl = job.path("redirect_url").asText();
                        String locationStr = job.path("location").path("display_name").asText();
                        String expReq = job.path("contract_time").asText();

                        if (matcherService.shouldExclude(desc, expReq)) continue;
                        if (!isFresherRole(title, desc)) continue;

                        count += saveJob(title, company, locationStr, desc, applyUrl,
                            "ADZUNA", detectTrackFromQuery(query), false);
                    }
                    rateLimitSleep();
                } catch (Exception e) {
                    log.warn("Adzuna fetch error for query '{}': {}", query, e.getMessage());
                }
            }
        }
        return count;
    }

    // ── RemoteOK ─────────────────────────────────────────────────────────────
    private int fetchRemoteOK() {
        int count = 0;
        try {
            JsonNode root = get(remoteOkUrl);
            if (root == null || !root.isArray()) return 0;

            for (JsonNode job : root) {
                String title = job.path("position").asText();
                String company = job.path("company").asText();
                String desc = job.path("description").asText();
                String applyUrl = job.path("url").asText();
                String tags = job.path("tags").toString().toLowerCase();

                if (!isRelevantRole(title, tags)) continue;
                if (!isFresherRole(title, desc)) continue;
                if (matcherService.shouldExclude(desc, null)) continue;

                String track = (tags.contains("ai") || tags.contains("llm") || tags.contains("genai"))
                    ? "GENAI" : "JAVA_FULLSTACK";

                count += saveJob(title, company, "Remote", desc, applyUrl, "REMOTEOK", track, false);
            }
            rateLimitSleep();
        } catch (Exception e) {
            log.warn("RemoteOK fetch error: {}", e.getMessage());
        }
        return count;
    }

    // ── Target Companies (Greenhouse / Lever / Ashby / Manual) ───────────────
    private int fetchFromTargetCompanies() {
        int count = 0;
        List<TargetCompany> companies = targetCompanyRepo.findByActive(true);

        for (TargetCompany company : companies) {
            try {
                if (company.getIsManualWatchlist()) {
                    count += saveManualWatchlist(company);
                    continue;
                }
                switch (company.getAtsType() != null ? company.getAtsType().toUpperCase() : "") {
                    case "GREENHOUSE" -> count += fetchGreenhouse(company);
                    case "LEVER"      -> count += fetchLever(company);
                    case "ASHBY"      -> count += fetchAshby(company);
                    default           -> count += saveManualWatchlist(company);
                }
                rateLimitSleep();
            } catch (Exception e) {
                log.warn("Error fetching from {}: {}", company.getName(), e.getMessage());
            }
        }
        return count;
    }

    private int fetchGreenhouse(TargetCompany company) {
        if (company.getApiSlug() == null) return saveManualWatchlist(company);
        try {
            String url = greenhouseBase + "/" + company.getApiSlug() + "/jobs?content=true";
            JsonNode root = get(url);
            if (root == null) return 0;

            int count = 0;
            for (JsonNode job : root.path("jobs")) {
                String title = job.path("title").asText();
                String desc = job.path("content").asText();
                String applyUrl = job.path("absolute_url").asText();
                String location = job.path("location").path("name").asText();

                if (!isRelevantRole(title, desc)) continue;
                if (!isFresherRole(title, desc)) continue;
                if (matcherService.shouldExclude(desc, null)) continue;

                count += saveJob(title, company.getName(), location, desc, applyUrl,
                    "GREENHOUSE", detectTrackFromText(title + " " + desc), false);
            }
            return count;
        } catch (Exception e) {
            log.warn("Greenhouse error for {}: {}", company.getName(), e.getMessage());
            return saveManualWatchlist(company);
        }
    }

    private int fetchLever(TargetCompany company) {
        if (company.getApiSlug() == null) return saveManualWatchlist(company);
        try {
            String url = leverBase + "/" + company.getApiSlug() + "?mode=json";
            JsonNode root = get(url);
            if (root == null || !root.isArray()) return 0;

            int count = 0;
            for (JsonNode job : root) {
                String title = job.path("text").asText();
                String desc = job.path("descriptionPlain").asText();
                String applyUrl = job.path("hostedUrl").asText();
                String location = job.path("categories").path("location").asText();

                if (!isRelevantRole(title, desc)) continue;
                if (!isFresherRole(title, desc)) continue;
                if (matcherService.shouldExclude(desc, null)) continue;

                count += saveJob(title, company.getName(), location, desc, applyUrl,
                    "LEVER", detectTrackFromText(title + " " + desc), false);
            }
            return count;
        } catch (Exception e) {
            log.warn("Lever error for {}: {}", company.getName(), e.getMessage());
            return saveManualWatchlist(company);
        }
    }

    private int fetchAshby(TargetCompany company) {
        if (company.getApiSlug() == null) return saveManualWatchlist(company);
        try {
            String url = ashbyBase + "/" + company.getApiSlug();
            JsonNode root = get(url);
            if (root == null) return 0;

            int count = 0;
            for (JsonNode job : root.path("jobPostings")) {
                String title = job.path("title").asText();
                String desc = job.path("descriptionHtml").asText();
                String applyUrl = job.path("jobPostingUrl").asText();
                String location = job.path("locationName").asText();

                if (!isRelevantRole(title, desc)) continue;
                if (!isFresherRole(title, desc)) continue;
                if (matcherService.shouldExclude(desc, null)) continue;

                count += saveJob(title, company.getName(), location, desc, applyUrl,
                    "ASHBY", detectTrackFromText(title + " " + desc), false);
            }
            return count;
        } catch (Exception e) {
            log.warn("Ashby error for {}: {}", company.getName(), e.getMessage());
            return saveManualWatchlist(company);
        }
    }

    private int saveManualWatchlist(TargetCompany company) {
        String title = "Open Roles — Check Manually";
        String hash = dedupHash(company.getName(), title, LocalDate.now().toString());
        if (jobListingRepo.findByDedupHash(hash).isPresent()) return 0;
        try {
            JobListing job = JobListing.builder()
                .title(title)
                .company(company.getName())
                .location(company.getCareersUrl() != null ? "See careers page" : "Unknown")
                .applyUrl(company.getCareersUrl())
                .source("MANUAL_WATCHLIST")
                .track("JAVA_FULLSTACK")
                .isManualWatchlist(true)
                .status("MANUAL_WATCHLIST")
                .dedupHash(hash)
                .postedDate(LocalDate.now())
                .fetchedAt(LocalDateTime.now())
                .build();
            jobListingRepo.save(job);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────
    private int saveJob(String title, String company, String location, String desc,
                        String applyUrl, String source, String track, boolean isRemote) {
        String hash = dedupHash(company, title, LocalDate.now().toString());
        if (jobListingRepo.findByDedupHash(hash).isPresent()) return 0;
        try {
            Map<String, Double> scores = matcherService.scoreJob(desc, title);
            double javaScore = scores.getOrDefault("javaFullStack", 0.0);
            double genaiScore = scores.getOrDefault("genAILeaning", 0.0);
            if (Math.max(javaScore, genaiScore) < 0.05) return 0; // only drop near-zero matches

            JobListing job = JobListing.builder()
                .title(title).company(company).location(location)
                .description(stripHtml(desc != null && desc.length() > 5000 ? desc.substring(0, 5000) : desc))
                .applyUrl(applyUrl).source(source).track(track)
                .isRemote(isRemote || (location != null && location.toLowerCase().contains("remote")))
                .javaMatchScore(javaScore).genaiMatchScore(genaiScore)
                .selectedResumeVersion(matcherService.selectResumeVersion(scores))
                .status("SHORTLISTED")
                .dedupHash(hash)
                .postedDate(LocalDate.now())
                .fetchedAt(LocalDateTime.now())
                .build();
            jobListingRepo.save(job);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    private String dedupHash(String company, String title, String date) {
        try {
            String raw = (company + "|" + title + "|" + date).toLowerCase().replaceAll("\\s+", "");
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.substring(0, 64);
        } catch (Exception e) {
            return UUID.randomUUID().toString().replace("-", "");
        }
    }

    private JsonNode get(String url) {
        try {
            Request req = new Request.Builder().url(url)
                .header("User-Agent", "JobAutomationBot/1.0 (contact: pavanryadavkumsi25@gmail.com)")
                .build();
            try (Response resp = http.newCall(req).execute()) {
                if (!resp.isSuccessful() || resp.body() == null) return null;
                return objectMapper.readTree(resp.body().string());
            }
        } catch (Exception e) {
            log.warn("HTTP GET failed for {}: {}", url, e.getMessage());
            return null;
        }
    }

    private void rateLimitSleep() {
        try { Thread.sleep(rateLimitMs); } catch (InterruptedException ignored) {}
    }

    private String encode(String s) {
        return s.replace(" ", "%20");
    }

    private boolean isFresherRole(String title, String desc) {
        String t = title.toLowerCase();

        // Block senior/non-fresher titles first
        if (BLOCKED_TITLE_PATTERNS.stream().anyMatch(t::contains)) return false;

        // Title must match an allowed role
        if (ALLOWED_TITLE_KEYWORDS.stream().noneMatch(t::contains)) return false;

        // Internship in title — always accept
        if (t.contains("intern") || t.contains("internship")) return true;

        // Explicit fresher signal in title — always accept
        if (FRESHER_TITLE_SIGNALS.stream().anyMatch(t::contains)) return true;

        // No fresher signal in title — accept if description doesn’t require 3+ years
        // (shouldExclude already handles the 3+ year block)
        return !matcherService.shouldExclude(desc, null);
    }

    private boolean isRelevantRole(String title, String desc) {
        String t = title.toLowerCase();
        // Block first, then check allowed keywords
        if (BLOCKED_TITLE_PATTERNS.stream().anyMatch(t::contains)) return false;
        return ALLOWED_TITLE_KEYWORDS.stream().anyMatch(t::contains);
    }

    private String stripHtml(String html) {
        if (html == null) return null;
        // Decode double-encoded entities first
        String s = html.replace("&amp;lt;", "<").replace("&amp;gt;", ">")
                       .replace("&amp;quot;", "\"").replace("&amp;amp;", "&")
                       .replace("&lt;", "<").replace("&gt;", ">")
                       .replace("&quot;", "\"").replace("&amp;", "&")
                       .replace("&#39;", "'").replace("&nbsp;", " ");
        // Strip HTML tags
        return s.replaceAll("<[^>]+>", " ").replaceAll("\\s{2,}", " ").trim();
    }

    private String detectTrackFromQuery(String query) {
        String q = query.toLowerCase();
        return (q.contains("genai") || q.contains("llm") || q.contains("ai")) ? "GENAI" : "JAVA_FULLSTACK";
    }

    private String detectTrackFromText(String text) {
        String t = text.toLowerCase();
        long genaiSignals = List.of("llm", "genai", "generative ai", "prompt engineering",
            "openai", "claude", "langchain", "rag", "embeddings", "chatbot", "ai integration")
            .stream().filter(t::contains).count();
        return genaiSignals >= 2 ? "GENAI" : "JAVA_FULLSTACK";
    }
}
