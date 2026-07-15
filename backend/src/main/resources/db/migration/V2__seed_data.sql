-- V2: Seed candidate profile, resume versions, projects, and target companies

-- Candidate Profile
INSERT INTO candidate_profile (name, email, phone, linkedin, github_handle, portfolio, location, experience_level, review_mode, daily_shortlist_target)
VALUES ('Pavan R', '', '+91 93805 42214', 'linkedin.com/in/pavan-r9035b62a2', 'PAVANRYADUVANSHI',
        'https://pavanryaduvanshi.github.io', 'Bengaluru, Karnataka, India (Open to Relocation)',
        'Fresher (0 Years), Immediate Joiner, Open to Relocation Across India', 'MANUAL', 50);

-- Education
INSERT INTO education (candidate_id, degree, institution, years, percentage)
VALUES (1, 'B.E. Mechanical Engineering', 'JNNCE, Shivamogga (VTU)', '2020 - 2024', '73%');

INSERT INTO education (candidate_id, certification, institution, years, coursework)
VALUES (1, 'Java Full-Stack Developer Certification', 'NIIT Institute', 'Apr 2025 - Apr 2026 (In Progress)',
        '["Core Java OOP","SQL Database Design","React.js","REST API Architecture","Microservices","FullStack Web App Development","Generative AI Fundamentals"]');

-- Resume Version: Java Full-Stack
INSERT INTO resume_version (candidate_id, version_key, target_roles, summary, core_skills, keyword_weights, resume_file_path)
VALUES (1, 'javaFullStack',
        '["Java Full-Stack Developer","Full-Stack Engineer","API/QA Engineer","Software Developer"]',
        'Fresher Java Full-Stack Developer with hands-on experience building and deploying production-grade applications using Spring Boot, React.js, and MySQL. Delivered a 4-member Agile team project with JWT auth, an AI-powered chatbot, Redux state management, and Docker deployment.',
        '["Core Java","Spring Boot","Spring Security","Hibernate/JPA","REST API","Microservices","React.js","TypeScript","Redux","MySQL","JWT Authentication","Docker","CI/CD (GitHub Actions)","JUnit","REST Assured","Postman","Git","OOP","SDLC","Agile/Scrum"]',
        '{"spring boot":1.0,"spring security":0.9,"hibernate":1.0,"jpa":1.0,"java":1.0,"rest api":1.0,"microservices":0.9,"react":0.9,"typescript":0.9,"redux":0.8,"mysql":0.9,"docker":0.8,"jwt":0.8,"ci/cd":0.7,"junit":0.7,"postman":0.6,"rest assured":0.6,"agile":0.5,"scrum":0.5,"full stack":1.0,"software developer":0.9,"backend":0.8,"frontend":0.8}',
        '/resumes/javafullstack-resume.pdf');

-- Resume Version: GenAI-Leaning
INSERT INTO resume_version (candidate_id, version_key, target_roles, summary, core_skills, keyword_weights, resume_file_path)
VALUES (1, 'genAILeaning',
        '["GenAI/LLM Integration Engineer - Fresher","Software Engineer - GenAI Focus","Full-Stack Developer - AI Products","Software Developer"]',
        'Fresher Full-Stack Developer with practical exposure to Generative AI integration (OpenAI/Claude APIs, prompt engineering) applied to product features, combining Spring Boot + React engineering fundamentals with hands-on GenAI API integration and AI chatbot development.',
        '["Core Java","Spring Boot","React.js","TypeScript","REST API","JWT Authentication","LLM API Integration (OpenAI, Claude)","Prompt Engineering","AI Chatbot Development","Docker","MySQL","CI/CD (GitHub Actions)","Microservices"]',
        '{"llm":1.0,"genai":1.0,"generative ai":1.0,"prompt engineering":1.0,"rag":0.85,"embeddings":0.85,"openai":1.0,"claude":1.0,"langchain":0.8,"chatbot":1.0,"ai integration":1.0,"spring boot":0.8,"react":0.8,"rest api":0.8,"java":0.7,"full stack":0.8,"software developer":0.7,"ai engineer":1.0,"llm engineer":1.0}',
        '/resumes/genaifullstack-resume.pdf');

-- Projects for javaFullStack (resume_version_id=1)
INSERT INTO resume_project (resume_version_id, name, type, dates, github_url, stack, highlight, cover_letter_pitch)
VALUES (1, 'FAST - Full-Stack Food Delivery Platform', 'Production-deployed, team of 4', 'Jan 2026 - Mar 2026',
        'github.com/PAVANRYADUVANSHI/FAST-FoodDelivery-App',
        '["Java","Spring Boot","Hibernate/JPA","MySQL","JWT","Maven","REST API","Docker","GitHub Actions","React.js","TypeScript","Redux","React Router"]',
        'Zero-downtime platform serving 500+ menu items with 100% endpoint coverage across auth, menu, and order APIs',
        'Delivered a zero-downtime food delivery platform serving 500+ menu items with 100% endpoint coverage, building Spring Boot REST APIs with JWT authentication, role-based access control, and Hibernate/JPA ORM as part of a 4-developer Agile team.');

INSERT INTO resume_project (resume_version_id, name, type, dates, github_url, stack, highlight, cover_letter_pitch)
VALUES (1, 'Task Manager App', 'Solo full-stack build', 'Nov 2025 - Dec 2025',
        'github.com/PAVANRYADUVANSHI/task-managerapp',
        '["React.js","Spring Boot","MySQL","JWT","Maven","REST API","JUnit"]',
        'Shipped a complete multi-user task management system solo, end-to-end, with JUnit-validated CRUD and auth flows',
        'Shipped a complete multi-user task management system solo end-to-end, from MySQL schema design to Spring Boot APIs to React frontend, with JUnit test coverage validating CRUD and JWT auth flows.');

INSERT INTO resume_project (resume_version_id, name, type, dates, github_url, stack, highlight, cover_letter_pitch)
VALUES (1, 'Zomato UI Clone', 'Team of 4, frontend developer', 'Aug 2025 - Sep 2025',
        'github.com/PAVANRYADUVANSHI/zomato-uiclone',
        '["HTML5","CSS3","JavaScript","TypeScript"]',
        'Pixel-accurate cross-device UI using CSS Grid/Flexbox with zero framework dependency',
        'Delivered a pixel-accurate, cross-device responsive UI modeled on Zomato''s production interface using CSS Grid and Flexbox, with zero framework dependency.');

-- Projects for genAILeaning (resume_version_id=2)
INSERT INTO resume_project (resume_version_id, name, type, dates, github_url, stack, highlight, cover_letter_pitch)
VALUES (2, 'FAST - Full-Stack Food Delivery Platform', 'Production-deployed, team of 4', 'Jan 2026 - Mar 2026',
        'github.com/PAVANRYADUVANSHI/FAST-FoodDelivery-App',
        '["Spring Boot","React","JWT","Docker","AI Chatbot","OpenAI/Claude API"]',
        'Integrated an AI-powered chatbot for customer support, applying prompt design and API integration patterns relevant to GenAI-enabled product features',
        'Integrated an AI-powered chatbot into a production food delivery platform for customer support, applying prompt design and OpenAI/Claude API integration patterns within a Spring Boot + React architecture serving 500+ menu items.');

INSERT INTO resume_project (resume_version_id, name, type, dates, github_url, stack, highlight, cover_letter_pitch)
VALUES (2, 'Task Manager App', 'Solo full-stack build', 'Nov 2025 - Dec 2025',
        'github.com/PAVANRYADUVANSHI/task-managerapp',
        '["Spring Boot","React","MySQL","JUnit"]',
        'Full-stack foundation demonstrating independent system design capability, extensible to AI-assisted task features',
        'Built a full-stack task management system independently end-to-end, demonstrating the system-design ownership needed to layer in AI-assisted product features.');

-- ============================================================
-- TARGET COMPANIES: MNCs, Indian Startups, Unicorns, Product
-- ATS types: GREENHOUSE, LEVER, ASHBY, WORKDAY, NONE(manual)
-- ============================================================

-- TIER 1 MNCs (Greenhouse/Lever/Workday)
INSERT INTO target_company (name, category, ats_type, careers_url, api_slug, is_manual_watchlist) VALUES
('Google', 'MNC', 'NONE', 'https://careers.google.com', NULL, TRUE),
('Microsoft', 'MNC', 'NONE', 'https://careers.microsoft.com', NULL, TRUE),
('Amazon', 'MNC', 'NONE', 'https://www.amazon.jobs', NULL, TRUE),
('Meta', 'MNC', 'GREENHOUSE', 'https://www.metacareers.com', 'meta', FALSE),
('Apple', 'MNC', 'NONE', 'https://jobs.apple.com', NULL, TRUE),
('IBM', 'MNC', 'NONE', 'https://www.ibm.com/employment', NULL, TRUE),
('Oracle', 'MNC', 'NONE', 'https://www.oracle.com/careers', NULL, TRUE),
('SAP', 'MNC', 'NONE', 'https://jobs.sap.com', NULL, TRUE),
('Salesforce', 'MNC', 'GREENHOUSE', 'https://salesforce.wd12.myworkdayjobs.com', 'salesforce', FALSE),
('Adobe', 'MNC', 'GREENHOUSE', 'https://adobe.wd5.myworkdayjobs.com', 'adobe', FALSE),
('Cisco', 'MNC', 'NONE', 'https://jobs.cisco.com', NULL, TRUE),
('Intel', 'MNC', 'NONE', 'https://jobs.intel.com', NULL, TRUE),
('Qualcomm', 'MNC', 'NONE', 'https://careers.qualcomm.com', NULL, TRUE),
('Nvidia', 'MNC', 'GREENHOUSE', 'https://nvidia.wd5.myworkdayjobs.com', 'nvidia', FALSE),
('VMware', 'MNC', 'NONE', 'https://careers.vmware.com', NULL, TRUE),
('Accenture', 'MNC', 'NONE', 'https://www.accenture.com/in-en/careers', NULL, TRUE),
('Infosys', 'MNC', 'NONE', 'https://www.infosys.com/careers', NULL, TRUE),
('Wipro', 'MNC', 'NONE', 'https://careers.wipro.com', NULL, TRUE),
('TCS', 'MNC', 'NONE', 'https://www.tcs.com/careers', NULL, TRUE),
('HCL Technologies', 'MNC', 'NONE', 'https://www.hcltech.com/careers', NULL, TRUE),
('Tech Mahindra', 'MNC', 'NONE', 'https://careers.techmahindra.com', NULL, TRUE),
('Capgemini', 'MNC', 'NONE', 'https://www.capgemini.com/in-en/careers', NULL, TRUE),
('Cognizant', 'MNC', 'NONE', 'https://careers.cognizant.com', NULL, TRUE),
('Mphasis', 'MNC', 'NONE', 'https://careers.mphasis.com', NULL, TRUE),
('L&T Technology Services', 'MNC', 'NONE', 'https://www.ltts.com/careers', NULL, TRUE),
('Hexaware', 'MNC', 'NONE', 'https://hexaware.com/careers', NULL, TRUE),
('Persistent Systems', 'MNC', 'NONE', 'https://www.persistent.com/careers', NULL, TRUE),
('Mindtree', 'MNC', 'NONE', 'https://www.mindtree.com/careers', NULL, TRUE),
('Mitel', 'MNC', 'NONE', 'https://www.mitel.com/en-us/about/careers', NULL, TRUE),
('DXC Technology', 'MNC', 'NONE', 'https://careers.dxc.com', NULL, TRUE);

-- Product Companies (Greenhouse/Lever/Ashby)
INSERT INTO target_company (name, category, ats_type, careers_url, api_slug, is_manual_watchlist) VALUES
('Atlassian', 'PRODUCT', 'GREENHOUSE', 'https://www.atlassian.com/company/careers', 'atlassian', FALSE),
('Freshworks', 'PRODUCT', 'GREENHOUSE', 'https://careers.freshworks.com', 'freshworks', FALSE),
('Zoho', 'PRODUCT', 'NONE', 'https://careers.zohocorp.com', NULL, TRUE),
('Razorpay', 'UNICORN', 'LEVER', 'https://razorpay.com/jobs', 'razorpay', FALSE),
('CRED', 'UNICORN', 'LEVER', 'https://careers.cred.club', 'cred', FALSE),
('Meesho', 'UNICORN', 'GREENHOUSE', 'https://meesho.io/jobs', 'meesho', FALSE),
('Swiggy', 'UNICORN', 'GREENHOUSE', 'https://careers.swiggy.com', 'swiggy', FALSE),
('Zomato', 'UNICORN', 'LEVER', 'https://www.zomato.com/careers', 'zomato', FALSE),
('Ola', 'UNICORN', 'NONE', 'https://www.olacabs.com/careers', NULL, TRUE),
('Paytm', 'UNICORN', 'NONE', 'https://paytm.com/careers', NULL, TRUE),
('PhonePe', 'UNICORN', 'GREENHOUSE', 'https://www.phonepe.com/careers', 'phonepe', FALSE),
('Flipkart', 'UNICORN', 'NONE', 'https://www.flipkartcareers.com', NULL, TRUE),
('Nykaa', 'UNICORN', 'NONE', 'https://careers.nykaa.com', NULL, TRUE),
('Byju''s', 'UNICORN', 'NONE', 'https://byjus.com/careers', NULL, TRUE),
('Unacademy', 'UNICORN', 'LEVER', 'https://unacademy.com/careers', 'unacademy', FALSE),
('Groww', 'UNICORN', 'LEVER', 'https://groww.in/careers', 'groww', FALSE),
('Zerodha', 'PRODUCT', 'NONE', 'https://zerodha.com/careers', NULL, TRUE),
('Upstox', 'STARTUP', 'LEVER', 'https://upstox.com/careers', 'upstox', FALSE),
('Lenskart', 'UNICORN', 'NONE', 'https://www.lenskart.com/careers', NULL, TRUE),
('Urban Company', 'UNICORN', 'GREENHOUSE', 'https://www.urbancompany.com/careers', 'urbancompany', FALSE),
('Dunzo', 'STARTUP', 'NONE', 'https://www.dunzo.com/careers', NULL, TRUE),
('Delhivery', 'UNICORN', 'NONE', 'https://www.delhivery.com/careers', NULL, TRUE),
('Shiprocket', 'STARTUP', 'LEVER', 'https://www.shiprocket.in/careers', 'shiprocket', FALSE),
('Postman', 'PRODUCT', 'GREENHOUSE', 'https://www.postman.com/company/careers', 'postman', FALSE),
('BrowserStack', 'PRODUCT', 'GREENHOUSE', 'https://www.browserstack.com/careers', 'browserstack', FALSE),
('HasuraHQ', 'STARTUP', 'LEVER', 'https://hasura.io/careers', 'hasura', FALSE),
('Setu', 'STARTUP', 'LEVER', 'https://setu.co/careers', 'setu', FALSE),
('Darwinbox', 'STARTUP', 'GREENHOUSE', 'https://darwinbox.com/careers', 'darwinbox', FALSE),
('Leadsquared', 'STARTUP', 'NONE', 'https://www.leadsquared.com/careers', NULL, TRUE),
('Chargebee', 'STARTUP', 'GREENHOUSE', 'https://www.chargebee.com/careers', 'chargebee', FALSE),
('Clevertap', 'STARTUP', 'GREENHOUSE', 'https://clevertap.com/careers', 'clevertap', FALSE),
('MoEngage', 'STARTUP', 'GREENHOUSE', 'https://www.moengage.com/careers', 'moengage', FALSE),
('Exotel', 'STARTUP', 'NONE', 'https://exotel.com/careers', NULL, TRUE),
('Turing', 'STARTUP', 'GREENHOUSE', 'https://www.turing.com/careers', 'turing', FALSE),
('Springworks', 'STARTUP', 'NONE', 'https://www.springworks.in/careers', NULL, TRUE),
('Innovaccer', 'STARTUP', 'GREENHOUSE', 'https://innovaccer.com/careers', 'innovaccer', FALSE),
('Healthifyme', 'STARTUP', 'NONE', 'https://www.healthifyme.com/careers', NULL, TRUE),
('Practo', 'STARTUP', 'NONE', 'https://www.practo.com/company/careers', NULL, TRUE),
('1mg', 'STARTUP', 'NONE', 'https://www.1mg.com/careers', NULL, TRUE),
('Cure.fit', 'STARTUP', 'NONE', 'https://www.cure.fit/careers', NULL, TRUE),
('Ninjacart', 'STARTUP', 'NONE', 'https://ninjacart.com/careers', NULL, TRUE),
('Udaan', 'UNICORN', 'NONE', 'https://udaan.com/careers', NULL, TRUE),
('Moglix', 'STARTUP', 'NONE', 'https://www.moglix.com/careers', NULL, TRUE),
('Vedantu', 'STARTUP', 'NONE', 'https://www.vedantu.com/careers', NULL, TRUE),
('Classplus', 'STARTUP', 'NONE', 'https://classplus.co/careers', NULL, TRUE),
('Scaler', 'STARTUP', 'NONE', 'https://www.scaler.com/careers', NULL, TRUE),
('InterviewBit', 'STARTUP', 'NONE', 'https://www.interviewbit.com/careers', NULL, TRUE),
('Testbook', 'STARTUP', 'NONE', 'https://testbook.com/careers', NULL, TRUE),
('Toppr', 'STARTUP', 'NONE', 'https://www.toppr.com/careers', NULL, TRUE),
('Simplilearn', 'STARTUP', 'NONE', 'https://www.simplilearn.com/careers', NULL, TRUE),
('upGrad', 'UNICORN', 'NONE', 'https://www.upgrad.com/careers', NULL, TRUE);

-- GenAI / AI-focused companies
INSERT INTO target_company (name, category, ats_type, careers_url, api_slug, is_manual_watchlist) VALUES
('Sarvam AI', 'STARTUP', 'ASHBY', 'https://www.sarvam.ai/careers', 'sarvam-ai', FALSE),
('Krutrim', 'STARTUP', 'NONE', 'https://krutrim.com/careers', NULL, TRUE),
('Ola Krutrim', 'STARTUP', 'NONE', 'https://krutrim.com/careers', NULL, TRUE),
('Haptik', 'STARTUP', 'NONE', 'https://haptik.ai/careers', NULL, TRUE),
('Yellow.ai', 'STARTUP', 'GREENHOUSE', 'https://yellow.ai/careers', 'yellow-ai', FALSE),
('Observe.AI', 'STARTUP', 'GREENHOUSE', 'https://www.observe.ai/careers', 'observe-ai', FALSE),
('Uniphore', 'STARTUP', 'GREENHOUSE', 'https://www.uniphore.com/careers', 'uniphore', FALSE),
('Vernacular.ai', 'STARTUP', 'NONE', 'https://vernacular.ai/careers', NULL, TRUE),
('Mihup', 'STARTUP', 'NONE', 'https://mihup.com/careers', NULL, TRUE),
('Slang Labs', 'STARTUP', 'NONE', 'https://slanglabs.in/careers', NULL, TRUE),
('Cogno AI', 'STARTUP', 'NONE', 'https://cogno.ai/careers', NULL, TRUE),
('Senseforth', 'STARTUP', 'NONE', 'https://senseforth.ai/careers', NULL, TRUE),
('Gnani.ai', 'STARTUP', 'NONE', 'https://gnani.ai/careers', NULL, TRUE),
('Entropik Tech', 'STARTUP', 'NONE', 'https://entropik.io/careers', NULL, TRUE),
('Mad Street Den', 'STARTUP', 'NONE', 'https://www.madstreetden.com/careers', NULL, TRUE),
('Niramai', 'STARTUP', 'NONE', 'https://niramai.com/careers', NULL, TRUE),
('Sigmoid', 'STARTUP', 'NONE', 'https://www.sigmoid.com/careers', NULL, TRUE),
('Fractal Analytics', 'STARTUP', 'NONE', 'https://fractal.ai/careers', NULL, TRUE),
('Mu Sigma', 'STARTUP', 'NONE', 'https://www.mu-sigma.com/careers', NULL, TRUE),
('Quantiphi', 'STARTUP', 'NONE', 'https://quantiphi.com/careers', NULL, TRUE),
('Tredence', 'STARTUP', 'NONE', 'https://www.tredence.com/careers', NULL, TRUE),
('Tiger Analytics', 'STARTUP', 'NONE', 'https://www.tigeranalytics.com/careers', NULL, TRUE),
('Crayon Data', 'STARTUP', 'NONE', 'https://crayondata.ai/careers', NULL, TRUE),
('Arya.ai', 'STARTUP', 'NONE', 'https://arya.ai/careers', NULL, TRUE),
('Scoutflo', 'STARTUP', 'NONE', 'https://scoutflo.com/careers', NULL, TRUE);

-- Global product/tech companies with India offices
INSERT INTO target_company (name, category, ats_type, careers_url, api_slug, is_manual_watchlist) VALUES
('Stripe', 'PRODUCT', 'GREENHOUSE', 'https://stripe.com/jobs', 'stripe', FALSE),
('Twilio', 'PRODUCT', 'GREENHOUSE', 'https://www.twilio.com/company/jobs', 'twilio', FALSE),
('Cloudflare', 'PRODUCT', 'GREENHOUSE', 'https://www.cloudflare.com/careers', 'cloudflare', FALSE),
('Datadog', 'PRODUCT', 'GREENHOUSE', 'https://www.datadoghq.com/careers', 'datadog', FALSE),
('HashiCorp', 'PRODUCT', 'GREENHOUSE', 'https://www.hashicorp.com/jobs', 'hashicorp', FALSE),
('Confluent', 'PRODUCT', 'GREENHOUSE', 'https://www.confluent.io/careers', 'confluent', FALSE),
('MongoDB', 'PRODUCT', 'GREENHOUSE', 'https://www.mongodb.com/careers', 'mongodb', FALSE),
('Elastic', 'PRODUCT', 'GREENHOUSE', 'https://www.elastic.co/about/careers', 'elastic', FALSE),
('Grafana Labs', 'PRODUCT', 'GREENHOUSE', 'https://grafana.com/about/careers', 'grafana-labs', FALSE),
('Notion', 'PRODUCT', 'GREENHOUSE', 'https://www.notion.so/careers', 'notion', FALSE),
('Linear', 'PRODUCT', 'ASHBY', 'https://linear.app/careers', 'linear', FALSE),
('Vercel', 'PRODUCT', 'ASHBY', 'https://vercel.com/careers', 'vercel', FALSE),
('Supabase', 'PRODUCT', 'ASHBY', 'https://supabase.com/careers', 'supabase', FALSE),
('PlanetScale', 'PRODUCT', 'GREENHOUSE', 'https://planetscale.com/careers', 'planetscale', FALSE),
('Retool', 'PRODUCT', 'GREENHOUSE', 'https://retool.com/careers', 'retool', FALSE),
('Figma', 'PRODUCT', 'GREENHOUSE', 'https://www.figma.com/careers', 'figma', FALSE),
('Miro', 'PRODUCT', 'GREENHOUSE', 'https://miro.com/careers', 'miro', FALSE),
('Loom', 'PRODUCT', 'GREENHOUSE', 'https://www.loom.com/careers', 'loom', FALSE),
('Airtable', 'PRODUCT', 'GREENHOUSE', 'https://airtable.com/careers', 'airtable', FALSE),
('Webflow', 'PRODUCT', 'GREENHOUSE', 'https://webflow.com/careers', 'webflow', FALSE),
('Deel', 'PRODUCT', 'GREENHOUSE', 'https://www.deel.com/careers', 'deel', FALSE),
('Remote.com', 'PRODUCT', 'GREENHOUSE', 'https://remote.com/careers', 'remote', FALSE),
('Rippling', 'PRODUCT', 'GREENHOUSE', 'https://www.rippling.com/careers', 'rippling', FALSE),
('Gusto', 'PRODUCT', 'GREENHOUSE', 'https://gusto.com/about/careers', 'gusto', FALSE),
('Brex', 'PRODUCT', 'GREENHOUSE', 'https://www.brex.com/careers', 'brex', FALSE),
('Plaid', 'PRODUCT', 'LEVER', 'https://plaid.com/careers', 'plaid', FALSE),
('Segment', 'PRODUCT', 'GREENHOUSE', 'https://segment.com/careers', 'segment', FALSE),
('Amplitude', 'PRODUCT', 'GREENHOUSE', 'https://amplitude.com/careers', 'amplitude', FALSE),
('Mixpanel', 'PRODUCT', 'GREENHOUSE', 'https://mixpanel.com/jobs', 'mixpanel', FALSE),
('Contentful', 'PRODUCT', 'GREENHOUSE', 'https://www.contentful.com/careers', 'contentful', FALSE);

-- YC-backed Indian startups
INSERT INTO target_company (name, category, ats_type, careers_url, api_slug, is_manual_watchlist) VALUES
('Khatabook', 'STARTUP', 'NONE', 'https://khatabook.com/careers', NULL, TRUE),
('OkCredit', 'STARTUP', 'NONE', 'https://okcredit.in/careers', NULL, TRUE),
('Slice', 'STARTUP', 'NONE', 'https://sliceit.com/careers', NULL, TRUE),
('Jupiter Money', 'STARTUP', 'NONE', 'https://jupiter.money/careers', NULL, TRUE),
('Fi Money', 'STARTUP', 'NONE', 'https://fi.money/careers', NULL, TRUE),
('Smallcase', 'STARTUP', 'NONE', 'https://smallcase.com/careers', NULL, TRUE),
('Niyo', 'STARTUP', 'NONE', 'https://www.goniyo.com/careers', NULL, TRUE),
('Open Financial', 'STARTUP', 'NONE', 'https://open.money/careers', NULL, TRUE),
('Cashfree Payments', 'STARTUP', 'NONE', 'https://www.cashfree.com/careers', NULL, TRUE),
('Juspay', 'STARTUP', 'NONE', 'https://juspay.in/careers', NULL, TRUE),
('Decentro', 'STARTUP', 'NONE', 'https://decentro.tech/careers', NULL, TRUE),
('Setu (Pine Labs)', 'STARTUP', 'NONE', 'https://setu.co/careers', NULL, TRUE),
('Hyperface', 'STARTUP', 'NONE', 'https://hyperface.co/careers', NULL, TRUE),
('Zeta', 'STARTUP', 'NONE', 'https://www.zeta.tech/careers', NULL, TRUE),
('Perfios', 'STARTUP', 'NONE', 'https://www.perfios.com/careers', NULL, TRUE),
('Signzy', 'STARTUP', 'NONE', 'https://signzy.com/careers', NULL, TRUE),
('Bureau', 'STARTUP', 'NONE', 'https://bureau.id/careers', NULL, TRUE),
('Spenmo', 'STARTUP', 'NONE', 'https://spenmo.com/careers', NULL, TRUE),
('Volopay', 'STARTUP', 'NONE', 'https://volopay.com/careers', NULL, TRUE),
('Recko', 'STARTUP', 'NONE', 'https://recko.io/careers', NULL, TRUE);
