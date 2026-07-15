import React, { useState, useRef, useEffect } from 'react';
import { NavLink, useNavigate, useLocation } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { logout } from '../../store/slices/authSlice';
import JALogo from './JALogo';

const PROJECTS = [
  { name: 'NeuroCity 🏙️', desc: 'Smart City Gen AI', url: 'https://pavan-neurocity.netlify.app' },
  { name: 'NeuralForge 🧠', desc: 'AI Dev Platform', url: 'https://pavan-neuralforge.netlify.app' },
  { name: 'HelpDesk AI 🤖', desc: 'Enterprise RAG Chatbot', url: 'https://github.com/PAVANRYADUVANSHI/HELPDESK-AI' },
  { name: 'FAST Food 🍔', desc: 'Full-Stack Delivery App', url: 'https://fast-food-delivery-srinidhi.netlify.app' },
  { name: 'Bake My Cake 🎂', desc: 'React SPA Shop', url: 'https://bake-my-cake-srinidhi.netlify.app' },
  { name: 'Zomato Clone 🍕', desc: 'UI Clone', url: 'https://pavan-zomato-clone.netlify.app' },
  { name: 'Task Manager ✅', desc: 'Full-Stack CRUD', url: 'https://pavan-taskmanager-app.netlify.app' },
  { name: 'ShopNow 🛒', desc: 'eCommerce Platform', url: 'https://pavan-shopnow-ecommerce.netlify.app' },
  { name: 'Weather Dashboard 🌤️', desc: 'OpenWeatherMap API', url: 'https://pavan-weather-dashboard.netlify.app' },
];

const JOBAUTO_LINKS = [
  { to: '/dashboard', label: '⬛ Dashboard', exact: true },
  { to: '/queue', label: '📋 Queue' },
  { to: '/tracker', label: '📊 Tracker' },
  { to: '/watchlist', label: '👁 Watchlist' },
  { to: '/analytics', label: '📈 Analytics' },
  { to: '/settings', label: '⚙️ Settings' },
];

function Dropdown({ label, children }: { label: React.ReactNode; children: React.ReactNode }) {
  const [open, setOpen] = useState(false);
  const ref = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handler = (e: MouseEvent) => {
      if (ref.current && !ref.current.contains(e.target as Node)) setOpen(false);
    };
    document.addEventListener('mousedown', handler);
    return () => document.removeEventListener('mousedown', handler);
  }, []);

  return (
    <div ref={ref} className="relative">
      <button
        onClick={() => setOpen(o => !o)}
        className="flex items-center gap-1 px-3 py-1.5 text-sm text-gray-300 hover:text-white rounded-lg hover:bg-white/5 transition font-medium"
      >
        {label}
        <svg className={`w-3.5 h-3.5 transition-transform ${open ? 'rotate-180' : ''}`} fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
        </svg>
      </button>
      {open && (
        <div className="absolute top-full left-0 mt-1 w-56 bg-gray-900 border border-gray-700/60 rounded-xl shadow-2xl shadow-black/60 z-50 overflow-hidden backdrop-blur-xl">
          {children}
        </div>
      )}
    </div>
  );
}

export default function TopNav() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const location = useLocation();
  const [mobileOpen, setMobileOpen] = useState(false);

  const handleLogout = () => {
    dispatch(logout());
    navigate('/login');
  };

  const isJobAutoActive = JOBAUTO_LINKS.some(l =>
    l.exact ? location.pathname === l.to : location.pathname.startsWith(l.to)
  );

  return (
    <>
      <nav className="fixed top-0 left-0 right-0 z-50 h-14 bg-gray-950/90 backdrop-blur-xl border-b border-gray-800/60 flex items-center px-4 gap-2">
        {/* Logo + Brand */}
        <NavLink to="/" className="flex items-center gap-2.5 mr-4 group">
          <JALogo size={34} />
          <div className="flex flex-col leading-none">
            <span className="text-white font-black text-base tracking-tight" style={{
              background: 'linear-gradient(135deg, #6366f1, #8b5cf6, #06b6d4)',
              WebkitBackgroundClip: 'text',
              WebkitTextFillColor: 'transparent',
              backgroundClip: 'text',
            }}>PAVAN R</span>
            <span className="text-gray-500 text-[9px] tracking-widest uppercase font-medium">Job Automation</span>
          </div>
        </NavLink>

        {/* Divider */}
        <div className="h-6 w-px bg-gray-700/60 mx-1" />

        {/* Desktop Nav */}
        <div className="hidden md:flex items-center gap-1 flex-1">
          {/* Portfolio */}
          <NavLink
            to="/"
            end
            className={({ isActive }) =>
              `px-3 py-1.5 text-sm rounded-lg transition font-medium ${isActive ? 'bg-indigo-600/20 text-indigo-300' : 'text-gray-300 hover:text-white hover:bg-white/5'}`
            }
          >
            🏠 Portfolio
          </NavLink>

          {/* Projects Dropdown */}
          <Dropdown label="🚀 Projects">
            <div className="p-1">
              {PROJECTS.map(p => (
                <a
                  key={p.name}
                  href={p.url}
                  target="_blank"
                  rel="noreferrer"
                  className="flex items-start gap-2 px-3 py-2 rounded-lg hover:bg-indigo-600/15 transition group"
                >
                  <div>
                    <p className="text-white text-xs font-semibold group-hover:text-indigo-300 transition">{p.name}</p>
                    <p className="text-gray-500 text-[10px]">{p.desc}</p>
                  </div>
                </a>
              ))}
            </div>
          </Dropdown>

          {/* JOBAUTO Dropdown */}
          <Dropdown label={<span className={isJobAutoActive ? 'text-cyan-400' : ''}>⚡ JobAuto</span>}>
            <div className="p-1">
              <div className="px-3 py-1.5 text-[10px] text-gray-500 uppercase tracking-widest font-semibold">Job Automation</div>
              {JOBAUTO_LINKS.map(({ to, label, exact }) => (
                <NavLink
                  key={to}
                  to={to}
                  end={exact}
                  className={({ isActive }) =>
                    `block px-3 py-2 rounded-lg text-xs transition font-medium ${isActive ? 'bg-indigo-600/20 text-indigo-300' : 'text-gray-300 hover:bg-white/5 hover:text-white'}`
                  }
                >
                  {label}
                </NavLink>
              ))}
            </div>
          </Dropdown>

          {/* GitHub */}
          <a
            href="https://github.com/PAVANRYADUVANSHI"
            target="_blank"
            rel="noreferrer"
            className="px-3 py-1.5 text-sm text-gray-300 hover:text-white rounded-lg hover:bg-white/5 transition font-medium"
          >
            GitHub ↗
          </a>
        </div>

        {/* Right side */}
        <div className="hidden md:flex items-center gap-2 ml-auto">
          <div className="flex items-center gap-2 px-3 py-1.5 bg-gray-800/60 rounded-lg border border-gray-700/40">
            <div className="w-2 h-2 rounded-full bg-emerald-400 animate-pulse" />
            <span className="text-gray-300 text-xs font-medium">Pavan R</span>
          </div>
          <button
            onClick={handleLogout}
            className="px-3 py-1.5 text-xs text-gray-400 hover:text-white hover:bg-gray-800 rounded-lg transition"
          >
            Logout
          </button>
        </div>

        {/* Mobile hamburger */}
        <button
          className="md:hidden ml-auto p-2 text-gray-400 hover:text-white"
          onClick={() => setMobileOpen(o => !o)}
        >
          <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            {mobileOpen
              ? <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              : <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
            }
          </svg>
        </button>
      </nav>

      {/* Mobile Menu */}
      {mobileOpen && (
        <div className="fixed inset-0 z-40 bg-gray-950/98 pt-14 flex flex-col p-4 gap-1 md:hidden overflow-y-auto">
          <NavLink to="/" end className="px-4 py-3 text-white font-medium rounded-xl hover:bg-gray-800" onClick={() => setMobileOpen(false)}>🏠 Portfolio</NavLink>
          <div className="px-4 py-1 text-[10px] text-gray-500 uppercase tracking-widest mt-2">Projects</div>
          {PROJECTS.map(p => (
            <a key={p.name} href={p.url} target="_blank" rel="noreferrer" className="px-4 py-2.5 text-gray-300 rounded-xl hover:bg-gray-800 text-sm" onClick={() => setMobileOpen(false)}>
              {p.name}
            </a>
          ))}
          <div className="px-4 py-1 text-[10px] text-gray-500 uppercase tracking-widest mt-2">JobAuto</div>
          {JOBAUTO_LINKS.map(({ to, label, exact }) => (
            <NavLink key={to} to={to} end={exact} className="px-4 py-2.5 text-gray-300 rounded-xl hover:bg-gray-800 text-sm" onClick={() => setMobileOpen(false)}>
              {label}
            </NavLink>
          ))}
          <button onClick={handleLogout} className="mt-4 px-4 py-3 text-red-400 text-sm text-left rounded-xl hover:bg-gray-800">🚪 Logout</button>
        </div>
      )}
    </>
  );
}
