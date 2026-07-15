import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { login } from '../store/slices/authSlice';
import { AppDispatch, RootState } from '../store';
import { useNavigate } from 'react-router-dom';

export default function LoginPage() {
  const dispatch = useDispatch<AppDispatch>();
  const navigate = useNavigate();
  const { loading, error } = useSelector((s: RootState) => s.auth);
  const [form, setForm] = useState({ username: '', password: '' });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const result = await dispatch(login(form));
    if (login.fulfilled.match(result)) {
      const seen = localStorage.getItem('onboarding_done');
      navigate(seen ? '/' : '/onboarding');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center" style={{background:'#060a12', backgroundImage:'radial-gradient(ellipse at 30% 40%, rgba(99,102,241,0.1) 0%, transparent 60%), radial-gradient(ellipse at 70% 70%, rgba(6,182,212,0.06) 0%, transparent 60%)'}}>
      <div style={{background:'#0c1524', border:'1px solid rgba(99,102,241,0.25)', borderRadius:20, padding:'40px 36px', width:'100%', maxWidth:380, boxShadow:'0 0 60px rgba(99,102,241,0.15)'}}>
        <div style={{display:'flex', alignItems:'center', gap:12, marginBottom:24}}>
          <div style={{width:44, height:44, borderRadius:12, background:'linear-gradient(135deg,#6366f1,#8b5cf6,#06b6d4)', display:'flex', alignItems:'center', justifyContent:'center', fontWeight:900, fontSize:'1rem', color:'#fff', letterSpacing:-1}}>JA</div>
          <div>
            <h1 style={{fontSize:'1.4rem', fontWeight:900, background:'linear-gradient(135deg,#6366f1,#8b5cf6,#06b6d4)', WebkitBackgroundClip:'text', WebkitTextFillColor:'transparent', backgroundClip:'text', margin:0}}>JOBAUTO</h1>
            <p style={{color:'#6b7280', fontSize:'0.75rem', margin:0}}>Pavan R · Fresher Full-Stack + GenAI</p>
          </div>
        </div>
        <form onSubmit={handleSubmit} className="space-y-4">
          <input
            className="w-full rounded-lg px-4 py-2.5 text-white placeholder-gray-500 focus:outline-none"
            style={{background:'#060a12', border:'1px solid rgba(99,102,241,0.3)'}}
            placeholder="Username"
            value={form.username}
            onChange={e => setForm(f => ({ ...f, username: e.target.value }))}
          />
          <input
            type="password"
            className="w-full rounded-lg px-4 py-2.5 text-white placeholder-gray-500 focus:outline-none"
            style={{background:'#060a12', border:'1px solid rgba(99,102,241,0.3)'}}
            placeholder="Password"
            value={form.password}
            onChange={e => setForm(f => ({ ...f, password: e.target.value }))}
          />
          {error && <p className="text-red-400 text-sm">{error}</p>}
          <button
            type="submit"
            disabled={loading}
            style={{background:'linear-gradient(135deg,#6366f1,#8b5cf6)', border:'none', cursor:'pointer'}}
            className="w-full text-white font-semibold py-2.5 rounded-lg transition disabled:opacity-50"
          >
            {loading ? 'Signing in...' : 'Sign In →'}
          </button>
        </form>
      </div>
    </div>
  );
}
