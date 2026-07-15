package com.jobautomation.app.service;

import com.jobautomation.app.model.*;
import com.jobautomation.app.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor @Slf4j
public class CoverLetterService {

    private final CandidateProfileRepository profileRepo;
    private final ResumeMatcherService matcherService;

    public String generate(JobListing job, String resumeVersionKey) {
        CandidateProfile profile = profileRepo.findAll().stream().findFirst().orElse(null);
        if (profile == null) return defaultCoverLetter(job);

        ResumeProject project = matcherService.getBestProject(resumeVersionKey, job.getDescription());
        boolean isGenAI = "genAILeaning".equals(resumeVersionKey);

        String roleLabel   = isGenAI ? "GenAI / Full-Stack Developer" : "Java Full-Stack Developer";
        String stackLine   = isGenAI
            ? "Java, Spring Boot, React.js, LLM API integration (OpenAI/Claude), prompt engineering, Docker, and CI/CD via GitHub Actions"
            : "Java, Spring Boot, React.js, MySQL, Docker, and CI/CD via GitHub Actions";

        // Project block — use best matching project pitch
        String projectBlock = buildProjectBlock(project, isGenAI);

        // Company-specific line
        String companyLine = buildCompanyLine(job.getCompany(), job.getTitle(), isGenAI);

        return String.format(
"Dear Hiring Team at %s,\n\n" +
"I'm a fresher Java Full-Stack Developer based in Bengaluru, writing to apply for the %s role at %s. " +
"I recently completed a Java Full-Stack Developer certification at NIIT Bangalore, where I built and " +
"deployed production-grade applications — and I'm looking to bring that hands-on foundation to your team.\n\n" +
"My core stack is %s. %s\n\n" +
"%s\n\n" +
"My resume, portfolio (pavanryaduvanshi.github.io), and GitHub (github.com/PAVANRYADUVANSHI) are attached. " +
"I'd love the opportunity to discuss how I can contribute to %s as a fresher who's ready to hit the ground running.\n\n" +
"Best regards,\n" +
"Pavan R\n" +
"+91 93805 42214\n" +
"pavanryadavkumsi25@gmail.com",
            job.getCompany(),
            job.getTitle(),
            job.getCompany(),
            stackLine,
            projectBlock,
            companyLine,
            job.getCompany()
        );
    }

    private String buildProjectBlock(ResumeProject project, boolean isGenAI) {
        if (project == null) {
            return isGenAI
                ? "On FAST, a food delivery platform I built with a team of four, I integrated an AI-powered " +
                  "chatbot using OpenAI/Claude APIs with prompt engineering patterns — directly applicable to " +
                  "GenAI product features. The project is live and open source: " +
                  "github.com/PAVANRYADUVANSHI/FAST-Food-Delivery-App."
                : "On FAST, a food delivery platform I built with a team of four, I implemented JWT-secured " +
                  "REST APIs with full endpoint test coverage using Postman and REST Assured, and our CI/CD " +
                  "setup cut deployment time by roughly 30%. The project is live and open source: " +
                  "github.com/PAVANRYADUVANSHI/FAST-Food-Delivery-App. I've also built a full-stack Task " +
                  "Manager application solo — covering authentication, testing, and deployment end to end — " +
                  "which taught me to own a project from architecture through delivery.";
        }

        String pitch = project.getCoverLetterPitch() != null ? project.getCoverLetterPitch() : "";
        String github = project.getGithubUrl() != null ? " — " + project.getGithubUrl() : "";

        return "On " + project.getName() + " (" + (project.getType() != null ? project.getType() : "") + "), " +
               pitch + github + ". " +
               (isGenAI
                   ? "I've also built a full-stack Task Manager solo, demonstrating independent system design " +
                     "capability extensible to AI-assisted product features."
                   : "I've also built a full-stack Task Manager solo — covering authentication, testing, and " +
                     "deployment end to end — which taught me to own a project from architecture through delivery.");
    }

    private String buildCompanyLine(String company, String title, boolean isGenAI) {
        // Personalised one-liner per company category
        String c = company.toLowerCase();

        if (isGenAI) {
            if (c.contains("sarvam") || c.contains("krutrim") || c.contains("gnani") || c.contains("vernacular"))
                return "What draws me to " + company + " specifically is your focus on building AI for Indian languages and contexts — a problem space I find genuinely compelling and where my GenAI integration experience is directly applicable.";
            if (c.contains("yellow") || c.contains("haptik") || c.contains("cogno") || c.contains("senseforth"))
                return "What draws me to " + company + " specifically is your conversational AI platform — building production chatbots with LLM APIs is exactly the kind of work I've been doing on my own projects and want to do at scale.";
            if (c.contains("observe") || c.contains("uniphore") || c.contains("mihup"))
                return "What draws me to " + company + " specifically is your voice AI and contact centre intelligence platform — applying LLMs to real enterprise workflows is the intersection I'm most excited about.";
            return "What draws me to " + company + " specifically is your engineering culture around AI-first product development — I'd welcome the opportunity to contribute my full-stack and GenAI integration skills to your team.";
        }

        if (c.contains("google") || c.contains("microsoft") || c.contains("amazon") || c.contains("meta"))
            return "What draws me to " + company + " specifically is the scale and engineering rigour — I want to grow as an engineer in an environment where systems thinking and code quality are taken seriously from day one.";
        if (c.contains("razorpay") || c.contains("phonepe") || c.contains("groww") || c.contains("cred") || c.contains("zerodha") || c.contains("upstox"))
            return "What draws me to " + company + " specifically is your mission to make financial services accessible to every Indian — building reliable, high-throughput backend systems for fintech is exactly the kind of challenge I want to work on.";
        if (c.contains("swiggy") || c.contains("zomato") || c.contains("dunzo") || c.contains("ninjacart"))
            return "What draws me to " + company + " specifically is the real-time, high-scale engineering challenges in food and quick commerce — the kind of distributed systems problems I've started exploring through my own projects.";
        if (c.contains("freshworks") || c.contains("zoho") || c.contains("chargebee") || c.contains("clevertap") || c.contains("moengage"))
            return "What draws me to " + company + " specifically is your reputation for building world-class SaaS products out of India — I want to be part of an engineering team that ships products used by businesses globally.";
        if (c.contains("atlassian") || c.contains("postman") || c.contains("browserstack") || c.contains("notion") || c.contains("figma"))
            return "What draws me to " + company + " specifically is your developer-focused product — I use " + company + " in my own projects and understand the product deeply, which I believe makes me a more effective engineer on the team.";
        if (c.contains("brex") || c.contains("ramp") || c.contains("mercury") || c.contains("plaid") || c.contains("adyen"))
            return "What draws me to " + company + " specifically is your mission to rebuild financial infrastructure from the ground up — building reliable, scalable backend systems for modern fintech is exactly the kind of engineering challenge I want to grow into.";
        if (c.contains("stripe") || c.contains("cloudflare") || c.contains("datadog") || c.contains("mongodb") || c.contains("elastic"))
            return "What draws me to " + company + " specifically is your infrastructure-level product and the engineering bar it demands — I want to build systems that other developers depend on, and " + company + " is exactly that kind of company.";
        if (c.contains("infosys") || c.contains("tcs") || c.contains("wipro") || c.contains("cognizant") || c.contains("capgemini") || c.contains("accenture"))
            return "What draws me to " + company + " specifically is the breadth of enterprise projects and the structured learning environment for freshers — I'm looking to build strong engineering fundamentals while contributing to real client deliverables from day one.";
        if (c.contains("meesho") || c.contains("flipkart") || c.contains("nykaa") || c.contains("lenskart"))
            return "What draws me to " + company + " specifically is your e-commerce scale and the engineering problems that come with it — building systems that serve millions of users is the kind of challenge I want to grow into.";

        // Default
        return "What draws me to " + company + " specifically is your engineering culture and the opportunity to work on products that have real user impact — I'd welcome the chance to contribute my full-stack skills to your team from day one.";
    }

    private String defaultCoverLetter(JobListing job) {
        return "Dear Hiring Team at " + job.getCompany() + ",\n\n" +
               "I'm writing to apply for the " + job.getTitle() + " position at " + job.getCompany() + ". " +
               "I am a Fresher Java Full-Stack Developer with hands-on project experience in Spring Boot, React.js, " +
               "MySQL, Docker, and CI/CD, available for immediate joining.\n\n" +
               "Best regards,\nPavan R\n+91 93805 42214\npavanryadavkumsi25@gmail.com";
    }
}
