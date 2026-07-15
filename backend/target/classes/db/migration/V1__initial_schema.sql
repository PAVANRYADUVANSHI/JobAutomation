-- V1: Initial schema

CREATE TABLE IF NOT EXISTS candidate_profile (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(20),
    linkedin VARCHAR(255),
    github_handle VARCHAR(100),
    portfolio VARCHAR(255),
    location VARCHAR(255),
    experience_level VARCHAR(255),
    review_mode VARCHAR(20) DEFAULT 'MANUAL',
    daily_shortlist_target INT DEFAULT 25,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS education (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    candidate_id BIGINT NOT NULL,
    degree VARCHAR(255),
    certification VARCHAR(255),
    institution VARCHAR(255),
    years VARCHAR(50),
    percentage VARCHAR(20),
    coursework JSON,
    FOREIGN KEY (candidate_id) REFERENCES candidate_profile(id)
);

CREATE TABLE IF NOT EXISTS resume_version (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    candidate_id BIGINT NOT NULL,
    version_key VARCHAR(50) NOT NULL,
    target_roles JSON,
    summary TEXT,
    core_skills JSON,
    keyword_weights JSON,
    resume_file_path VARCHAR(255),
    FOREIGN KEY (candidate_id) REFERENCES candidate_profile(id)
);

CREATE TABLE IF NOT EXISTS resume_project (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resume_version_id BIGINT NOT NULL,
    name VARCHAR(255),
    type VARCHAR(100),
    dates VARCHAR(100),
    github_url VARCHAR(255),
    stack JSON,
    highlight TEXT,
    cover_letter_pitch TEXT,
    FOREIGN KEY (resume_version_id) REFERENCES resume_version(id)
);

CREATE TABLE IF NOT EXISTS target_company (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(50),
    ats_type VARCHAR(50),
    careers_url VARCHAR(500),
    api_slug VARCHAR(255),
    is_manual_watchlist BOOLEAN DEFAULT FALSE,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS job_listing (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    external_id VARCHAR(255),
    source VARCHAR(100),
    track VARCHAR(50),
    title VARCHAR(255) NOT NULL,
    company VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    description TEXT,
    apply_url VARCHAR(500),
    posted_date DATE,
    experience_required VARCHAR(100),
    is_remote BOOLEAN DEFAULT FALSE,
    is_manual_watchlist BOOLEAN DEFAULT FALSE,
    dedup_hash VARCHAR(64),
    java_match_score DOUBLE DEFAULT 0,
    genai_match_score DOUBLE DEFAULT 0,
    selected_resume_version VARCHAR(50),
    status VARCHAR(50) DEFAULT 'NEW',
    fetched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_dedup (dedup_hash)
);

CREATE TABLE IF NOT EXISTS application (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_listing_id BIGINT NOT NULL,
    candidate_id BIGINT NOT NULL,
    resume_version VARCHAR(50),
    cover_letter TEXT,
    status VARCHAR(50) DEFAULT 'SHORTLISTED',
    track VARCHAR(50),
    match_score DOUBLE,
    submitted_at TIMESTAMP NULL,
    response_at TIMESTAMP NULL,
    interview_at TIMESTAMP NULL,
    notes TEXT,
    auto_submitted BOOLEAN DEFAULT FALSE,
    follow_up_sent BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (job_listing_id) REFERENCES job_listing(id),
    FOREIGN KEY (candidate_id) REFERENCES candidate_profile(id)
);

CREATE TABLE IF NOT EXISTS app_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'ROLE_USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS scheduler_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    run_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    jobs_fetched INT DEFAULT 0,
    jobs_matched INT DEFAULT 0,
    jobs_shortlisted INT DEFAULT 0,
    jobs_submitted INT DEFAULT 0,
    mode VARCHAR(20),
    summary TEXT
);
