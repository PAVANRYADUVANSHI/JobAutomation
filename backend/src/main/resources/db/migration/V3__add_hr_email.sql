-- V3: Add hr_email to target_company for email-based auto-apply

ALTER TABLE target_company ADD COLUMN hr_email VARCHAR(255) NULL;

-- MNCs
UPDATE target_company SET hr_email = 'careers-india@google.com'        WHERE name = 'Google';
UPDATE target_company SET hr_email = 'india_careers@microsoft.com'     WHERE name = 'Microsoft';
UPDATE target_company SET hr_email = 'amazon-india-jobs@amazon.com'    WHERE name = 'Amazon';
UPDATE target_company SET hr_email = 'university@infosys.com'          WHERE name = 'Infosys';
UPDATE target_company SET hr_email = 'careers@wipro.com'               WHERE name = 'Wipro';
UPDATE target_company SET hr_email = 'fresher.careers@tcs.com'         WHERE name = 'TCS';
UPDATE target_company SET hr_email = 'careers@hcltech.com'             WHERE name = 'HCL Technologies';
UPDATE target_company SET hr_email = 'careers@techmahindra.com'        WHERE name = 'Tech Mahindra';
UPDATE target_company SET hr_email = 'india.recruitment@capgemini.com' WHERE name = 'Capgemini';
UPDATE target_company SET hr_email = 'careers@cognizant.com'           WHERE name = 'Cognizant';
UPDATE target_company SET hr_email = 'careers@mphasis.com'             WHERE name = 'Mphasis';
UPDATE target_company SET hr_email = 'careers@persistent.com'          WHERE name = 'Persistent Systems';
UPDATE target_company SET hr_email = 'careers@hexaware.com'            WHERE name = 'Hexaware';
UPDATE target_company SET hr_email = 'recruitment@dxc.com'             WHERE name = 'DXC Technology';

-- Product / Unicorns
UPDATE target_company SET hr_email = 'careers@razorpay.com'            WHERE name = 'Razorpay';
UPDATE target_company SET hr_email = 'jobs@cred.club'                  WHERE name = 'CRED';
UPDATE target_company SET hr_email = 'careers@meesho.com'              WHERE name = 'Meesho';
UPDATE target_company SET hr_email = 'careers@swiggy.in'               WHERE name = 'Swiggy';
UPDATE target_company SET hr_email = 'careers@zomato.com'              WHERE name = 'Zomato';
UPDATE target_company SET hr_email = 'careers@phonepe.com'             WHERE name = 'PhonePe';
UPDATE target_company SET hr_email = 'careers@groww.in'                WHERE name = 'Groww';
UPDATE target_company SET hr_email = 'careers@upstox.com'              WHERE name = 'Upstox';
UPDATE target_company SET hr_email = 'talent@freshworks.com'           WHERE name = 'Freshworks';
UPDATE target_company SET hr_email = 'careers@zoho.com'                WHERE name = 'Zoho';
UPDATE target_company SET hr_email = 'jobs@postman.com'                WHERE name = 'Postman';
UPDATE target_company SET hr_email = 'careers@browserstack.com'        WHERE name = 'BrowserStack';
UPDATE target_company SET hr_email = 'careers@chargebee.com'           WHERE name = 'Chargebee';
UPDATE target_company SET hr_email = 'careers@clevertap.com'           WHERE name = 'Clevertap';
UPDATE target_company SET hr_email = 'careers@moengage.com'            WHERE name = 'MoEngage';
UPDATE target_company SET hr_email = 'careers@darwinbox.com'           WHERE name = 'Darwinbox';
UPDATE target_company SET hr_email = 'careers@innovaccer.com'          WHERE name = 'Innovaccer';
UPDATE target_company SET hr_email = 'careers@shiprocket.in'           WHERE name = 'Shiprocket';
UPDATE target_company SET hr_email = 'talent@unacademy.com'            WHERE name = 'Unacademy';

-- GenAI startups
UPDATE target_company SET hr_email = 'careers@sarvam.ai'               WHERE name = 'Sarvam AI';
UPDATE target_company SET hr_email = 'careers@yellow.ai'               WHERE name = 'Yellow.ai';
UPDATE target_company SET hr_email = 'careers@observe.ai'              WHERE name = 'Observe.AI';
UPDATE target_company SET hr_email = 'careers@uniphore.com'            WHERE name = 'Uniphore';
UPDATE target_company SET hr_email = 'careers@haptik.ai'               WHERE name = 'Haptik';
UPDATE target_company SET hr_email = 'careers@fractal.ai'              WHERE name = 'Fractal Analytics';
UPDATE target_company SET hr_email = 'careers@quantiphi.com'           WHERE name = 'Quantiphi';
UPDATE target_company SET hr_email = 'careers@sigmoid.com'             WHERE name = 'Sigmoid';
UPDATE target_company SET hr_email = 'careers@tredence.com'            WHERE name = 'Tredence';

-- Global product
UPDATE target_company SET hr_email = 'jobs@stripe.com'                 WHERE name = 'Stripe';
UPDATE target_company SET hr_email = 'careers@twilio.com'              WHERE name = 'Twilio';
UPDATE target_company SET hr_email = 'careers@cloudflare.com'          WHERE name = 'Cloudflare';
UPDATE target_company SET hr_email = 'careers@datadoghq.com'           WHERE name = 'Datadog';
UPDATE target_company SET hr_email = 'careers@mongodb.com'             WHERE name = 'MongoDB';
UPDATE target_company SET hr_email = 'careers@elastic.co'              WHERE name = 'Elastic';
UPDATE target_company SET hr_email = 'jobs@notion.so'                  WHERE name = 'Notion';
UPDATE target_company SET hr_email = 'careers@figma.com'               WHERE name = 'Figma';
UPDATE target_company SET hr_email = 'careers@deel.com'                WHERE name = 'Deel';
UPDATE target_company SET hr_email = 'careers@rippling.com'            WHERE name = 'Rippling';

-- YC Indian startups
UPDATE target_company SET hr_email = 'careers@khatabook.com'           WHERE name = 'Khatabook';
UPDATE target_company SET hr_email = 'careers@jupiter.money'           WHERE name = 'Jupiter Money';
UPDATE target_company SET hr_email = 'careers@fi.money'                WHERE name = 'Fi Money';
UPDATE target_company SET hr_email = 'careers@smallcase.com'           WHERE name = 'Smallcase';
UPDATE target_company SET hr_email = 'careers@juspay.in'               WHERE name = 'Juspay';
UPDATE target_company SET hr_email = 'careers@cashfree.com'            WHERE name = 'Cashfree Payments';
UPDATE target_company SET hr_email = 'careers@zeta.tech'               WHERE name = 'Zeta';
UPDATE target_company SET hr_email = 'careers@perfios.com'             WHERE name = 'Perfios';
UPDATE target_company SET hr_email = 'hr@signzy.com'                   WHERE name = 'Signzy';
