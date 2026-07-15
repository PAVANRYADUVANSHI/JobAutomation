import React, { useEffect, useRef, useState } from 'react';

const roles = ['Full Stack Developer', 'Java Developer', 'React Developer', 'GenAI Builder'];

const skills = [
  { name: 'Java', icon: '☕', level: 85, color: '#f89820' },
  { name: 'React.js', icon: '⚛️', level: 80, color: '#61dafb' },
  { name: 'JavaScript', icon: '🟨', level: 78, color: '#f7df1e' },
  { name: 'Spring Boot', icon: '🍃', level: 60, color: '#6db33f' },
  { name: 'SQL', icon: '🗄️', level: 75, color: '#336791' },
  { name: 'Tailwind CSS', icon: '💨', level: 70, color: '#38bdf8' },
  { name: 'Git & GitHub', icon: '🐙', level: 80, color: '#f05032' },
  { name: 'REST APIs', icon: '🔗', level: 75, color: '#9333ea' },
];

const projects = [
  { title: 'NeuroCity 🏙️', desc: 'Sentient smart city platform — 3D holographic map, conversational city AI, live IoT streams, predictive intelligence engine.', tech: ['Next.js 14', 'Spring Boot', 'Three.js', 'OpenAI', 'WebSockets'], color: '#00F5FF', badge: '🏙️ Smart City Gen AI', github: 'https://github.com/PAVANRYADUVANSHI/NeuroCity', live: 'https://pavan-neurocity.netlify.app' },
  { title: 'NeuralForge 🧠', desc: 'Autonomous AI Dev Platform — generates full-stack features from plain English, predicts bugs, self-heals production errors.', tech: ['Java 21', 'Spring Boot 3', 'React', 'GPT-4o', 'Docker'], color: '#6366f1', badge: '🧠 Gen AI Flagship', github: 'https://github.com/PAVANRYADUVANSHI/NeuralForge', live: 'https://pavan-neuralforge.netlify.app' },
  { title: 'HelpDesk AI 🤖', desc: 'Enterprise RAG chatbot — employees ask questions about company policies, answers grounded in admin-uploaded PDFs with source citations.', tech: ['Spring Boot 3', 'pgvector', 'Groq', 'HuggingFace', 'JWT'], color: '#3b82f6', badge: '🤖 Enterprise RAG', github: 'https://github.com/PAVANRYADUVANSHI/HELPDESK-AI', live: '#' },
  { title: 'FAST Food 🍔', desc: 'Swiggy/Zomato-like delivery app — 500+ dishes, JWT auth, Stripe + UPI + COD, AI chatbot, real-time tracking, admin dashboard.', tech: ['React', 'Spring Boot', 'MySQL', 'Stripe', 'Gen AI'], color: '#fc8019', badge: '⭐ Main Project', github: 'https://github.com/PAVANRYADUVANSHI/FAST-Food-Delivery-App', live: 'https://fast-food-delivery-srinidhi.netlify.app' },
  { title: 'Bake My Cake 🎂', desc: 'React SPA for online cake shop — category filtering, flip-card gallery, cart, checkout, 54 products via json-server REST API.', tech: ['React', 'Axios', 'Framer Motion', 'json-server'], color: '#DAA520', github: 'https://github.com/PAVANRYADUVANSHI/bake-my-cake-phase-1', live: 'https://bake-my-cake-srinidhi.netlify.app' },
  { title: 'ShopNow 🛒', desc: 'Amazon-like eCommerce — JWT auth, Stripe payments, product reviews, wishlist, admin dashboard, Cloudinary image uploads.', tech: ['Next.js', 'Node.js', 'MongoDB', 'Stripe'], color: '#10b981', github: 'https://github.com/PAVANRYADUVANSHI/shopnow-ecommerce', live: 'https://pavan-shopnow-ecommerce.netlify.app' },
];

function TypedText() {
  const ref = useRef<HTMLSpanElement>(null);
  useEffect(() => {
    let i = 0, j = 0, del = false;
    const tick = () => {
      const cur = roles[i % roles.length];
      if (ref.current) ref.current.textContent = del ? cur.substring(0, j--) : cur.substring(0, j++);
      let speed = del ? 60 : 100;
      if (!del && j === cur.length + 1) { speed = 1500; del = true; }
      if (del && j === 0) { del = false; i++; speed = 300; }
      setTimeout(tick, speed);
    };
    tick();
  }, []);
  return <span ref={ref} style={{ color: '#ffd54f' }} />;
}

export default function PortfolioHome() {
  const [form, setForm] = useState({ name: '', email: '', message: '' });
  const [sent, setSent] = useState(false);

  const submit = (e: React.FormEvent) => {
    e.preventDefault();
    setSent(true);
    setTimeout(() => setSent(false), 3000);
    setForm({ name: '', email: '', message: '' });
  };

  return (
    <div style={{ background: '#060a12', color: '#e3f2fd', fontFamily: "'Segoe UI', system-ui, sans-serif", minHeight: '100vh' }}>

      {/* HERO */}
      <section id="hero" style={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '120px 6% 60px', gap: 40, position: 'relative', overflow: 'hidden' }}>
        <div style={{ position: 'absolute', inset: 0, background: 'radial-gradient(ellipse at 20% 50%, rgba(99,102,241,0.12) 0%, transparent 60%), radial-gradient(ellipse at 80% 20%, rgba(6,182,212,0.08) 0%, transparent 60%)' }} />
        <div style={{ position: 'relative', zIndex: 1, maxWidth: 580 }}>
          <p style={{ color: '#ffd54f', fontSize: '0.9rem', letterSpacing: 3, textTransform: 'uppercase', fontWeight: 600, marginBottom: 12 }}>👋 Hello, I'm</p>
          <h1 style={{ fontSize: 'clamp(2.2rem,5vw,3.8rem)', fontWeight: 900, lineHeight: 1.1, marginBottom: 16, background: 'linear-gradient(135deg,#fff 0%,#6366f1 40%,#06b6d4 100%)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent', backgroundClip: 'text', letterSpacing: 2 }}>
            PAVAN R YADAV
          </h1>
          <h2 style={{ fontSize: 'clamp(1.1rem,2.5vw,1.6rem)', fontWeight: 600, color: '#90caf9', marginBottom: 20, minHeight: '2rem' }}>
            <TypedText /><span style={{ color: '#42a5f5', animation: 'blink 1s step-end infinite' }}>|</span>
          </h2>
          <p style={{ color: '#90caf9', lineHeight: 1.8, marginBottom: 32, fontSize: '1rem', maxWidth: 480 }}>
            Java Full Stack Developer skilled in React.js, Spring Boot &amp; MySQL. Building impactful applications with GenAI integration. Seeking entry-level Software Engineer roles.
          </p>
          <div style={{ display: 'flex', gap: 14, flexWrap: 'wrap', marginBottom: 28 }}>
            <a href="#projects" style={{ padding: '11px 26px', background: 'linear-gradient(135deg,#6366f1,#8b5cf6)', color: '#fff', borderRadius: 50, fontWeight: 700, fontSize: '0.9rem', textDecoration: 'none', boxShadow: '0 0 20px rgba(99,102,241,0.4)' }}>View Projects</a>
            <a href="#contact" style={{ padding: '11px 26px', border: '2px solid #42a5f5', color: '#42a5f5', borderRadius: 50, fontWeight: 600, fontSize: '0.9rem', textDecoration: 'none' }}>Contact Me</a>
            <a href="http://localhost:3001" target="_blank" rel="noreferrer" style={{ padding: '11px 26px', border: '1px solid rgba(99,102,241,0.4)', color: '#a5b4fc', borderRadius: 50, fontWeight: 600, fontSize: '0.9rem', textDecoration: 'none' }}>Portfolio v2 🚀</a>
          </div>
          <div style={{ display: 'flex', gap: 20 }}>
            {[['GitHub', 'https://github.com/PAVANRYADUVANSHI'], ['LinkedIn', 'https://linkedin.com'], ['Email', 'mailto:pavanryavdavkumsi25@gmail.com']].map(([l, h]) => (
              <a key={l} href={h} target="_blank" rel="noreferrer" style={{ color: '#90caf9', fontSize: '0.85rem', borderBottom: '1px solid transparent', textDecoration: 'none', transition: 'color 0.2s' }}
                onMouseEnter={e => { (e.target as HTMLElement).style.color = '#ffd54f'; (e.target as HTMLElement).style.borderColor = '#ffd54f'; }}
                onMouseLeave={e => { (e.target as HTMLElement).style.color = '#90caf9'; (e.target as HTMLElement).style.borderColor = 'transparent'; }}
              >{l}</a>
            ))}
          </div>
        </div>
        {/* Avatar */}
        <div style={{ position: 'relative', zIndex: 1, width: 300, height: 300, flexShrink: 0, display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
          <div style={{ width: 180, height: 180, borderRadius: '50%', background: 'linear-gradient(135deg,#ffd54f,#6366f1,#06b6d4)', display: 'flex', alignItems: 'center', justifyContent: 'center', boxShadow: '0 0 50px rgba(99,102,241,0.5), 0 0 100px rgba(6,182,212,0.2)', padding: 4, animation: 'avatarFloat 4s ease-in-out infinite' }}>
            <img src="http://localhost:3000/avatar.jpg" alt="Pavan R Yadav" style={{ width: '100%', height: '100%', borderRadius: '50%', objectFit: 'cover', objectPosition: 'center top' }}
              onError={(e) => { (e.target as HTMLImageElement).style.display = 'none'; }} />
            <div style={{ position: 'absolute', width: '100%', height: '100%', borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '2rem', fontWeight: 900, color: '#ffd54f', background: 'linear-gradient(135deg,#0d47a1,#1976d2)' }}>PRY</div>
          </div>
        </div>
      </section>

      {/* ABOUT */}
      <section id="about" style={{ padding: '80px 6%', maxWidth: 1100, margin: '0 auto' }}>
        <SectionTitle title="About" accent="Me" />
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1.6fr', gap: 60, alignItems: 'center' }}>
          <div style={{ width: 240, height: 240, borderRadius: 24, background: 'linear-gradient(135deg,#6366f1,#06b6d4)', margin: '0 auto', display: 'flex', alignItems: 'center', justifyContent: 'center', fontSize: '5rem', boxShadow: '0 20px 60px rgba(99,102,241,0.3)' }}>👨‍💻</div>
          <div>
            <h3 style={{ fontSize: '1.5rem', fontWeight: 700, marginBottom: 14, background: 'linear-gradient(135deg,#42a5f5,#ffd54f)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent', backgroundClip: 'text' }}>Java Full Stack Developer</h3>
            <p style={{ color: '#90caf9', lineHeight: 1.8, marginBottom: 12 }}>I'm <strong style={{ color: '#fff' }}>PAVAN R YADAV</strong>, a Java Full Stack Developer with hands-on experience in React.js, Core Java, Spring Boot, and MySQL. B.E. in Mechanical Engineering from JNNCE Shivamogga, currently completing Java Full Stack Developer Certification at NIIT (2025–2026).</p>
            <p style={{ color: '#90caf9', lineHeight: 1.8, marginBottom: 24 }}>Built production-grade applications with JWT auth, payment gateways, AI chatbots, and admin dashboards. Actively seeking entry-level Software Engineer roles.</p>
            <div style={{ display: 'flex', gap: 16, flexWrap: 'wrap', marginBottom: 24 }}>
              {[['5+', 'Projects Built'], ['10+', 'Technologies'], ['3+', 'Certifications']].map(([v, l]) => (
                <div key={l} style={{ background: '#0c1524', border: '1px solid rgba(99,102,241,0.25)', borderRadius: 12, padding: '14px 22px', textAlign: 'center' }}>
                  <span style={{ display: 'block', fontSize: '1.6rem', fontWeight: 800, background: 'linear-gradient(135deg,#6366f1,#06b6d4)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent', backgroundClip: 'text' }}>{v}</span>
                  <span style={{ fontSize: '0.75rem', color: '#90caf9' }}>{l}</span>
                </div>
              ))}
            </div>
            <a href="http://localhost:3000/PAVANRYADUVANSHI RESUME 2-6-26.pdf" download style={{ padding: '11px 26px', background: 'linear-gradient(135deg,#6366f1,#8b5cf6)', color: '#fff', borderRadius: 50, fontWeight: 700, fontSize: '0.9rem', textDecoration: 'none' }}>Download Resume ↓</a>
          </div>
        </div>
      </section>

      {/* SKILLS */}
      <section id="skills" style={{ padding: '80px 6%', maxWidth: 1100, margin: '0 auto' }}>
        <SectionTitle title="My" accent="Skills" />
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill,minmax(160px,1fr))', gap: 16 }}>
          {skills.map((s, i) => (
            <div key={s.name} style={{ background: '#0c1524', border: `1px solid rgba(99,102,241,0.2)`, borderRadius: 16, padding: '22px 16px', textAlign: 'center', transition: 'transform 0.3s, border-color 0.3s', cursor: 'default', animationDelay: `${i * 0.05}s` }}
              onMouseEnter={e => { (e.currentTarget as HTMLElement).style.transform = 'translateY(-6px)'; (e.currentTarget as HTMLElement).style.borderColor = s.color; }}
              onMouseLeave={e => { (e.currentTarget as HTMLElement).style.transform = 'translateY(0)'; (e.currentTarget as HTMLElement).style.borderColor = 'rgba(99,102,241,0.2)'; }}
            >
              <div style={{ fontSize: '2rem', marginBottom: 8 }}>{s.icon}</div>
              <h4 style={{ fontSize: '0.9rem', fontWeight: 600, marginBottom: 10 }}>{s.name}</h4>
              <div style={{ width: '100%', height: 4, background: 'rgba(255,255,255,0.08)', borderRadius: 2, overflow: 'hidden', marginBottom: 4 }}>
                <div style={{ height: '100%', width: `${s.level}%`, background: s.color, borderRadius: 2 }} />
              </div>
              <span style={{ fontSize: '0.72rem', color: '#90caf9' }}>{s.level}%</span>
            </div>
          ))}
        </div>
      </section>

      {/* PROJECTS */}
      <section id="projects" style={{ padding: '80px 6%', maxWidth: 1100, margin: '0 auto' }}>
        <SectionTitle title="My" accent="Projects" />
        <div style={{ display: 'flex', flexDirection: 'column', gap: 24 }}>
          {projects.map(p => (
            <div key={p.title} style={{ background: '#0c1524', border: `2px solid ${p.color}`, borderRadius: 20, padding: '32px 36px', position: 'relative', overflow: 'hidden', boxShadow: `0 0 30px ${p.color}22` }}>
              {p.badge && <span style={{ display: 'inline-block', background: p.color, color: '#000', fontSize: '0.7rem', fontWeight: 800, padding: '3px 12px', borderRadius: 20, marginBottom: 14, letterSpacing: 0.5 }}>{p.badge}</span>}
              <div style={{ display: 'grid', gridTemplateColumns: '1fr auto', gap: 20, alignItems: 'start' }}>
                <div>
                  <h3 style={{ fontSize: '1.4rem', fontWeight: 800, marginBottom: 8, background: `linear-gradient(135deg,#fff,${p.color})`, WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent', backgroundClip: 'text' }}>{p.title}</h3>
                  <p style={{ color: '#90caf9', fontSize: '0.9rem', lineHeight: 1.7, marginBottom: 14 }}>{p.desc}</p>
                  <div style={{ display: 'flex', gap: 8, flexWrap: 'wrap' }}>
                    {p.tech.map(t => <span key={t} style={{ fontSize: '0.72rem', padding: '3px 10px', borderRadius: 20, background: 'rgba(255,255,255,0.05)', border: '1px solid rgba(255,255,255,0.1)', color: '#90caf9' }}>{t}</span>)}
                  </div>
                </div>
                <div style={{ display: 'flex', flexDirection: 'column', gap: 8, minWidth: 120 }}>
                  <a href={p.github} target="_blank" rel="noreferrer" style={{ padding: '8px 18px', border: `2px solid ${p.color}`, color: p.color, borderRadius: 50, fontWeight: 600, fontSize: '0.82rem', textDecoration: 'none', textAlign: 'center', whiteSpace: 'nowrap' }}>GitHub ↗</a>
                  {p.live !== '#'
                    ? <a href={p.live} target="_blank" rel="noreferrer" style={{ padding: '8px 18px', background: p.color, color: '#000', borderRadius: 50, fontWeight: 700, fontSize: '0.82rem', textDecoration: 'none', textAlign: 'center', whiteSpace: 'nowrap' }}>Live ↗</a>
                    : <span style={{ padding: '8px 18px', border: '1px dashed rgba(255,255,255,0.15)', color: '#666', borderRadius: 50, fontSize: '0.75rem', textAlign: 'center' }}>Coming Soon</span>
                  }
                </div>
              </div>
              <div style={{ position: 'absolute', inset: 0, background: `radial-gradient(circle at 90% 50%, ${p.color}0a, transparent 60%)`, pointerEvents: 'none' }} />
            </div>
          ))}
        </div>
      </section>

      {/* CONTACT */}
      <section id="contact" style={{ padding: '80px 6%', maxWidth: 1100, margin: '0 auto' }}>
        <SectionTitle title="Get In" accent="Touch" />
        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1.2fr', gap: 60, alignItems: 'start' }}>
          <div>
            <h3 style={{ fontSize: '1.6rem', fontWeight: 700, marginBottom: 14, background: 'linear-gradient(135deg,#42a5f5,#ffd54f)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent', backgroundClip: 'text' }}>Let's work together</h3>
            <p style={{ color: '#90caf9', lineHeight: 1.8, marginBottom: 28 }}>I'm currently open to new opportunities. Whether you have a project, a question, or just want to say hi — my inbox is always open!</p>
            <div style={{ display: 'flex', flexDirection: 'column', gap: 12 }}>
              {[['📧', 'pavanryavdavkumsi25@gmail.com', 'mailto:pavanryavdavkumsi25@gmail.com'], ['📍', 'Bangalore, India', '#'], ['💼', 'LinkedIn Profile', 'https://linkedin.com'], ['🐙', 'GitHub Profile', 'https://github.com/PAVANRYADUVANSHI']].map(([icon, label, href]) => (
                <a key={label} href={href} target="_blank" rel="noreferrer" style={{ display: 'flex', alignItems: 'center', gap: 12, padding: '12px 16px', background: '#0c1524', border: '1px solid rgba(99,102,241,0.2)', borderRadius: 12, color: '#90caf9', textDecoration: 'none', fontSize: '0.9rem', transition: 'border-color 0.2s' }}
                  onMouseEnter={e => (e.currentTarget as HTMLElement).style.borderColor = '#42a5f5'}
                  onMouseLeave={e => (e.currentTarget as HTMLElement).style.borderColor = 'rgba(99,102,241,0.2)'}
                >
                  <span>{icon}</span>{label}
                </a>
              ))}
            </div>
          </div>
          <form onSubmit={submit} style={{ display: 'flex', flexDirection: 'column', gap: 14 }}>
            {(['name', 'email'] as const).map(f => (
              <input key={f} name={f} type={f === 'email' ? 'email' : 'text'} placeholder={f === 'name' ? 'Your Name' : 'Your Email'} value={form[f]} onChange={e => setForm({ ...form, [f]: e.target.value })} required
                style={{ background: '#0c1524', border: '1px solid rgba(99,102,241,0.25)', borderRadius: 12, padding: '13px 16px', color: '#fff', fontSize: '0.95rem', outline: 'none', fontFamily: 'inherit' }} />
            ))}
            <textarea name="message" placeholder="Your Message" rows={5} value={form.message} onChange={e => setForm({ ...form, message: e.target.value })} required
              style={{ background: '#0c1524', border: '1px solid rgba(99,102,241,0.25)', borderRadius: 12, padding: '13px 16px', color: '#fff', fontSize: '0.95rem', outline: 'none', resize: 'none', fontFamily: 'inherit' }} />
            <button type="submit" style={{ padding: '13px 28px', background: 'linear-gradient(135deg,#6366f1,#8b5cf6)', color: '#fff', borderRadius: 50, fontWeight: 700, fontSize: '0.95rem', border: 'none', cursor: 'pointer' }}>
              {sent ? '✅ Message Sent!' : 'Send Message →'}
            </button>
          </form>
        </div>
      </section>

      {/* FOOTER */}
      <footer style={{ textAlign: 'center', padding: '32px 5%', borderTop: '1px solid rgba(99,102,241,0.15)', color: '#90caf9', fontSize: '0.85rem' }}>
        <p>Built by <span style={{ background: 'linear-gradient(135deg,#6366f1,#06b6d4)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent', backgroundClip: 'text', fontWeight: 700 }}>PAVAN R YADAV</span> · Java Full Stack + GenAI Developer</p>
        <p style={{ marginTop: 6, opacity: 0.5, fontSize: '0.75rem' }}>Pavan R · Job Automation Platform</p>
      </footer>

      <style>{`
        @keyframes avatarFloat { 0%,100%{transform:translateY(0)} 50%{transform:translateY(-14px)} }
        @keyframes blink { 0%,100%{opacity:1} 50%{opacity:0} }
      `}</style>
    </div>
  );
}

function SectionTitle({ title, accent }: { title: string; accent: string }) {
  return (
    <div style={{ textAlign: 'center', marginBottom: 50 }}>
      <h2 style={{ fontSize: 'clamp(1.8rem,3.5vw,2.6rem)', fontWeight: 700 }}>
        {title} <span style={{ background: 'linear-gradient(135deg,#42a5f5,#ffd54f)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent', backgroundClip: 'text' }}>{accent}</span>
      </h2>
      <div style={{ width: 56, height: 4, background: 'linear-gradient(135deg,#6366f1,#06b6d4)', margin: '10px auto 0', borderRadius: 2 }} />
    </div>
  );
}
