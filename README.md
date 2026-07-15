# Job Application Automation Tool
**Pavan R — Fresher Java Full-Stack + GenAI Developer**

> ⚠️ **IMPORTANT:** `REVIEW_MODE=AUTO` submits applications without human review. Risk of platform bans and low-quality submissions. **Always start with `REVIEW_MODE=MANUAL`.**

---

## Stack
- **Backend:** Java 17, Spring Boot 3.2, Spring Security, JPA/Hibernate, MySQL, Flyway
- **Frontend:** React 18 + TypeScript, Redux Toolkit, Tailwind CSS
- **Auth:** JWT
- **Scheduler:** Spring `@Scheduled` (daily 7 AM IST)
- **Container:** Docker + docker-compose
- **CI/CD:** GitHub Actions

---

## Quick Start (Docker)

```bash
# 1. Clone and enter project
git clone <repo-url>
cd JobAutomation

# 2. Copy and fill env file
cp .env.example .env
# Edit .env — add API keys, mail credentials

# 3. Run everything
docker compose up --build

# 4. Register your user (first time only)
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"pavan","password":"yourpassword"}'

# 5. Open dashboard
open http://localhost:3000
```

---

## Local Dev (without Docker)

**Backend:**
```bash
cd backend
# Requires MySQL running on localhost:3306
mvn spring-boot:run
# Swagger UI: http://localhost:8080/swagger-ui.html
```

**Frontend:**
```bash
cd frontend
npm install
npm run dev
# Dashboard: http://localhost:3000
```

---

## Environment Variables

Create `.env` in project root (used by docker-compose):

```env
DB_USER=root
DB_PASS=yourpassword
JWT_SECRET=YourLongSecretKeyHere

# Job APIs
ADZUNA_APP_ID=your_adzuna_app_id
ADZUNA_APP_KEY=your_adzuna_app_key

# Email (Gmail App Password)
MAIL_HOST=smtp.gmail.com
MAIL_USER=your@gmail.com
MAIL_PASS=your_app_password

# MANUAL (default, recommended) or AUTO
REVIEW_MODE=MANUAL
```

### Getting API Keys
| API | URL | Notes |
|-----|-----|-------|
| Adzuna | https://developer.adzuna.com | Free tier, 250 req/day |
| RemoteOK | https://remoteok.com/api | No key needed |
| Greenhouse | https://boards-api.greenhouse.io | Public, no key |
| Lever | https://api.lever.co/v0/postings | Public, no key |
| Ashby | https://api.ashbyhq.com | Public, no key |

---

## Resume Setup

Place your resume PDFs at:
```
backend/src/main/resources/resumes/javafullstack-resume.pdf
backend/src/main/resources/resumes/genaifullstack-resume.pdf
```

The seed data (V2 migration) auto-loads Pavan R's profile, both resume versions, all projects, and 100+ target companies on first run.

---

## API Endpoints (Swagger: `/swagger-ui.html`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | Get JWT token |
| POST | `/api/auth/register` | Register user |
| GET | `/api/profile` | Candidate profile + resume versions |
| PUT | `/api/profile/review-mode?mode=AUTO` | Toggle review mode |
| GET | `/api/jobs/shortlisted` | Today's top 25 jobs |
| GET | `/api/jobs/watchlist` | Manual watchlist entries |
| GET | `/api/applications/queue` | Today's review queue |
| PUT | `/api/applications/{id}/status` | Approve/reject/move pipeline |
| POST | `/api/applications/shortlist` | Manually trigger shortlisting |
| GET | `/api/analytics` | Full analytics data |
| POST | `/api/scheduler/run` | Manually trigger full pipeline |

---

## How It Works

### Daily Pipeline (7 AM IST)
```
fetch jobs (Adzuna + RemoteOK + Greenhouse/Lever/Ashby per company)
    ↓
score against both resumes (TF-IDF cosine similarity)
    ↓
filter: fresher roles, Bangalore/Remote, exclude 3+ yrs exp
    ↓
deduplicate by SHA-256(company+title+date)
    ↓
shortlist top 25 by max(javaScore, genaiScore)
    ↓
auto-select resume version + generate cover letter
    ↓
[MANUAL] → populate review queue → you approve/reject
[AUTO]   → auto-submit via API → log → email summary
```

### Dual Track
- **Java Full-Stack track** — targets Java/Spring/React roles, uses `javaFullStack` resume
- **GenAI track** — targets LLM/AI/chatbot roles, uses `genAILeaning` resume, weights GenAI keywords higher

### Company Coverage (100+ companies)
- **MNCs:** Google, Microsoft, Amazon, Infosys, TCS, Wipro, Accenture, etc.
- **Unicorns:** Razorpay, CRED, Meesho, Swiggy, Zomato, PhonePe, Groww, etc.
- **Product:** Freshworks, Atlassian, Postman, BrowserStack, Zoho, etc.
- **GenAI startups:** Sarvam AI, Yellow.ai, Observe.AI, Uniphore, etc.
- **Global product:** Stripe, Cloudflare, Datadog, MongoDB, Notion, Vercel, etc.
- **YC Indian startups:** Khatabook, Slice, Jupiter, Fi Money, Juspay, etc.

Companies with Greenhouse/Lever/Ashby ATS → auto-fetched via public API  
Companies with no API → added as **manual watchlist** entries with direct careers link

### ToS Compliance
- No scraping. All sources are official public APIs.
- LinkedIn and Naukri web UI have no submission API → always manual-assist regardless of `REVIEW_MODE`.
- Rate limiting: 1 second between API calls (configurable via `app.rate.limit.ms`).

---

## Dashboard Pages
- **Dashboard** — stats, pipeline bar, track split, daily chart, queue preview, scheduler logs
- **Queue** — today's 25 shortlisted jobs, one-click approve/skip, cover letter preview, track filter
- **Tracker** — full application pipeline table, status updates, track/status filters
- **Analytics** — conversion rates by track, daily bar chart, full pipeline breakdown
- **Settings** — candidate profile, MANUAL/AUTO toggle with risk warning

---

## ⚠️ AUTO Mode Warning

`REVIEW_MODE=AUTO` will:
- Submit all 25 shortlisted applications daily without your review
- Use auto-generated cover letters
- Only submit via platforms with a submission API (Greenhouse, Lever, Ashby, Adzuna)
- Log every submission with full audit trail
- Send a daily summary email

**Risks:** Platform bans for bulk applications, low-quality matches getting submitted, irreversible submissions.  
**Recommendation:** Run in MANUAL mode for at least 1 week to validate match quality before switching to AUTO.
