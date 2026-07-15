import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';

const STEPS = ['API Keys', 'Resumes', 'Companies', 'Find Roles'];

export default function OnboardingPage() {
  const navigate = useNavigate();
  const [step, setStep] = useState(0);
  const [adzunaId, setAdzunaId] = useState('');
  const [adzunaKey, setAdzunaKey] = useState('');
  const [javaFile, setJavaFile] = useState<File | null>(null);
  const [genaiFile, setGenaiFile] = useState<File | null>(null);
  const [companies, setCompanies] = useState('');
  const [busy, setBusy] = useState(false);
  const [pipelineDone, setPipelineDone] = useState(false);

  const next = () => setStep(s => s + 1);

  const saveKeys = async () => {
    if (adzunaId && adzunaKey) {
      setBusy(true);
      try { await api.put('/api/profile/api-keys', { adzunaAppId: adzunaId, adzunaAppKey: adzunaKey }); } catch (_) {}
      setBusy(false);
    }
    next();
  };

  const uploadResumes = async () => {
    setBusy(true);
    try {
      if (javaFile) { const f = new FormData(); f.append('file', javaFile); f.append('version', 'javaFullStack'); await api.post('/api/profile/resume', f); }
      if (genaiFile) { const f = new FormData(); f.append('file', genaiFile); f.append('version', 'genAILeaning'); await api.post('/api/profile/resume', f); }
    } catch (_) {}
    setBusy(false);
    next();
  };

  const saveCompanies = async () => {
    const names = companies.split('\n').map(s => s.trim()).filter(Boolean);
    if (names.length) { setBusy(true); try { await api.post('/api/profile/companies', { names }); } catch (_) {} setBusy(false); }
    next();
  };

  const runPipeline = async () => {
    setBusy(true);
    try { await api.post('/api/scheduler/run'); } catch (_) {}
    setBusy(false);
    localStorage.setItem('onboarding_done', '1');
    setPipelineDone(true);
  };

  return (
    <div className="min-h-screen flex flex-col items-center px-4 py-10"
      style={{ background: '#060a12', backgroundImage: 'radial-gradient(ellipse at 30% 20%, rgba(99,102,241,0.12) 0%, transparent 60%)' }}>

      {/* Logo */}
      <div className="flex items-center gap-3 mb-8">
        <div className="w-10 h-10 rounded-xl flex items-center justify-center font-black text-white text-sm"
          style={{ background: 'linear-gradient(135deg,#6366f1,#8b5cf6,#06b6d4)' }}>JA</div>
        <div>
          <p className="font-black text-lg leading-none"
            style={{ background: 'linear-gradient(135deg,#6366f1,#8b5cf6,#06b6d4)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent', backgroundClip: 'text' }}>PAVAN R</p>
          <p className="text-gray-500 text-[10px] tracking-widest uppercase">Get Started</p>
        </div>
      </div>

      {/* Step bar */}
      <div className="flex items-center gap-1 mb-8 w-full max-w-sm">
        {STEPS.map((label, i) => (
          <React.Fragment key={i}>
            <div className="flex flex-col items-center gap-1">
              <div className={`w-7 h-7 rounded-full flex items-center justify-center text-xs font-bold ${
                i < step ? 'bg-indigo-600 text-white' : i === step ? 'bg-indigo-600 text-white ring-4 ring-indigo-600/25' : 'bg-gray-800 text-gray-500'}`}>
                {i < step ? '✓' : i + 1}
              </div>
              <span className={`text-[9px] ${i === step ? 'text-indigo-400' : 'text-gray-600'}`}>{label}</span>
            </div>
            {i < STEPS.length - 1 && <div className={`flex-1 h-0.5 mb-4 ${i < step ? 'bg-indigo-600' : 'bg-gray-800'}`} />}
          </React.Fragment>
        ))}
      </div>

      <div className="w-full max-w-sm bg-gray-900 border border-gray-800 rounded-2xl p-6 space-y-5">

        {/* Step 0 — API Keys */}
        {step === 0 && <>
          <div>
            <h2 className="text-white font-bold text-lg">Add API Keys</h2>
            <p className="text-gray-400 text-sm mt-1">All sources are <span className="text-green-400 font-medium">100% free</span>. Only Adzuna needs a key (optional).</p>
          </div>
          <div className="space-y-2">
            {['RemoteOK', 'Greenhouse', 'Lever', 'Ashby'].map(s => (
              <div key={s} className="flex items-center justify-between bg-gray-800/60 rounded-xl px-4 py-2.5">
                <span className="text-gray-300 text-sm">{s}</span>
                <span className="text-green-400 text-xs bg-green-400/10 px-2 py-0.5 rounded-full">✓ No key needed</span>
              </div>
            ))}
          </div>
          <div className="border border-indigo-500/30 rounded-xl p-4 space-y-3">
            <div className="flex items-center justify-between">
              <span className="text-white text-sm font-semibold">Adzuna <span className="text-gray-500 font-normal text-xs">(optional · 250 req/day free)</span></span>
              <a href="https://developer.adzuna.com" target="_blank" rel="noreferrer" className="text-indigo-400 text-xs underline">Get free key ↗</a>
            </div>
            <input className="w-full bg-gray-950 border border-gray-700 rounded-lg px-3 py-2 text-white text-sm placeholder-gray-600 focus:outline-none focus:border-indigo-500"
              placeholder="App ID" value={adzunaId} onChange={e => setAdzunaId(e.target.value)} />
            <input className="w-full bg-gray-950 border border-gray-700 rounded-lg px-3 py-2 text-white text-sm placeholder-gray-600 focus:outline-none focus:border-indigo-500"
              placeholder="App Key" value={adzunaKey} onChange={e => setAdzunaKey(e.target.value)} />
          </div>
          <button onClick={saveKeys} disabled={busy} className="w-full py-3 rounded-xl font-semibold text-white disabled:opacity-60"
            style={{ background: 'linear-gradient(135deg,#6366f1,#8b5cf6)' }}>{busy ? 'Saving...' : 'Continue →'}</button>
          <button onClick={next} className="w-full text-gray-500 text-sm py-1">Skip</button>
        </>}

        {/* Step 1 — Resumes */}
        {step === 1 && <>
          <div>
            <h2 className="text-white font-bold text-lg">Upload Resumes</h2>
            <p className="text-gray-400 text-sm mt-1">Upload one or both resume tracks. PDF only.</p>
          </div>
          {[
            { label: 'Java Full-Stack Resume', sub: 'Java / Spring / React roles', file: javaFile, set: setJavaFile },
            { label: 'GenAI Resume', sub: 'LLM / AI / chatbot roles', file: genaiFile, set: setGenaiFile },
          ].map(({ label, sub, file, set }) => (
            <label key={label} className={`block border-2 border-dashed rounded-xl p-4 cursor-pointer transition ${file ? 'border-indigo-500 bg-indigo-500/5' : 'border-gray-700 hover:border-gray-600'}`}>
              <input type="file" accept=".pdf" className="hidden" onChange={e => set(e.target.files?.[0] || null)} />
              <div className="flex items-center gap-3">
                <span className="text-2xl">{file ? '✅' : '📄'}</span>
                <div>
                  <p className="text-white text-sm font-medium">{label}</p>
                  <p className="text-gray-500 text-xs">{file ? file.name : sub}</p>
                </div>
              </div>
            </label>
          ))}
          <button onClick={uploadResumes} disabled={busy} className="w-full py-3 rounded-xl font-semibold text-white disabled:opacity-60"
            style={{ background: 'linear-gradient(135deg,#6366f1,#8b5cf6)' }}>{busy ? 'Uploading...' : 'Continue →'}</button>
          <button onClick={next} className="w-full text-gray-500 text-sm py-1">Skip</button>
        </>}

        {/* Step 2 — Companies */}
        {step === 2 && <>
          <div>
            <h2 className="text-white font-bold text-lg">Target Companies</h2>
            <p className="text-gray-400 text-sm mt-1">100+ companies already loaded. Add more below.</p>
          </div>
          <div className="bg-gray-800/60 rounded-xl p-3 space-y-1">
            {['Google · Greenhouse', 'Razorpay · Lever', 'Freshworks · Ashby', 'Swiggy · Lever', '+ 96 more pre-loaded'].map(c => (
              <div key={c} className="flex items-center gap-2 text-xs text-gray-400"><span className="text-green-400">✓</span>{c}</div>
            ))}
          </div>
          <textarea className="w-full bg-gray-950 border border-gray-700 rounded-xl px-3 py-2.5 text-white text-sm placeholder-gray-600 focus:outline-none focus:border-indigo-500 resize-none"
            rows={4} placeholder={"Notion\nVercel\nLinear"} value={companies} onChange={e => setCompanies(e.target.value)} />
          <button onClick={saveCompanies} disabled={busy} className="w-full py-3 rounded-xl font-semibold text-white disabled:opacity-60"
            style={{ background: 'linear-gradient(135deg,#6366f1,#8b5cf6)' }}>{busy ? 'Saving...' : 'Continue →'}</button>
          <button onClick={next} className="w-full text-gray-500 text-sm py-1">Skip</button>
        </>}

        {/* Step 3 — Find Roles */}
        {step === 3 && <>
          <div>
            <h2 className="text-white font-bold text-lg">Find Roles</h2>
            <p className="text-gray-400 text-sm mt-1">Searches live across all sources — no copy-pasting, no scraping.</p>
          </div>
          <div className="space-y-2">
            {[
              ['🔍', 'Fetches fresher Java + GenAI roles live'],
              ['🎯', 'Scores each job against your resume'],
              ['✉️', 'Auto-generates personalised cover letter'],
              ['✅', 'You approve → click Apply Now'],
            ].map(([icon, text]) => (
              <div key={text} className="flex items-center gap-3 bg-gray-800/50 rounded-xl px-4 py-2.5">
                <span className="text-lg">{icon}</span>
                <span className="text-gray-300 text-sm">{text}</span>
              </div>
            ))}
          </div>
          {!pipelineDone ? (
            <button onClick={runPipeline} disabled={busy} className="w-full py-3 rounded-xl font-bold text-white disabled:opacity-70 flex items-center justify-center gap-2"
              style={{ background: 'linear-gradient(135deg,#6366f1,#8b5cf6)' }}>
              {busy ? <><span className="animate-spin inline-block">⟳</span> Searching... (~2 min)</> : '🚀 Find Roles Now'}
            </button>
          ) : (
            <div className="space-y-3">
              <div className="bg-green-500/10 border border-green-500/30 rounded-xl p-4 text-center">
                <p className="text-green-400 font-semibold">✓ Pipeline started!</p>
                <p className="text-gray-400 text-xs mt-1">Jobs appear in Queue in ~2 minutes.</p>
              </div>
              <button onClick={() => navigate('/queue')} className="w-full py-3 rounded-xl font-bold text-white"
                style={{ background: 'linear-gradient(135deg,#6366f1,#8b5cf6)' }}>Go to Queue → Review & Apply</button>
            </div>
          )}
          <button onClick={() => { localStorage.setItem('onboarding_done','1'); navigate('/dashboard'); }} className="w-full text-gray-500 text-sm py-1">Go to Dashboard</button>
        </>}

      </div>
    </div>
  );
}
