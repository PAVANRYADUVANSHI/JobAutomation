import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { logout } from '../../store/slices/authSlice';

const NAV = [
  { to: '/', label: '⬛ Dashboard', exact: true },
  { to: '/queue', label: '📋 Queue' },
  { to: '/tracker', label: '📊 Tracker' },
  { to: '/watchlist', label: '👁 Watchlist' },
  { to: '/analytics', label: '📈 Analytics' },
  { to: '/settings', label: '⚙️ Settings' },
];

export default function Sidebar() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleLogout = () => {
    dispatch(logout());
    navigate('/login');
  };

  return (
    <aside className="w-56 bg-gray-900 border-r border-gray-800 flex flex-col h-screen sticky top-0">
      <div className="p-5 border-b border-gray-800">
        <p className="text-white font-bold text-sm">Job Automation</p>
        <p className="text-gray-500 text-xs">Pavan R · Fresher</p>
      </div>
      <nav className="flex-1 p-3 space-y-1">
        {NAV.map(({ to, label, exact }) => (
          <NavLink
            key={to}
            to={to}
            end={exact}
            className={({ isActive }) =>
              `block px-3 py-2 rounded-lg text-sm transition ${
                isActive ? 'bg-indigo-600 text-white' : 'text-gray-400 hover:bg-gray-800 hover:text-white'
              }`
            }
          >
            {label}
          </NavLink>
        ))}
      </nav>
      <div className="p-3 border-t border-gray-800">
        <button
          onClick={handleLogout}
          className="w-full text-left px-3 py-2 text-gray-400 hover:text-white hover:bg-gray-800 rounded-lg text-sm transition"
        >
          🚪 Logout
        </button>
      </div>
    </aside>
  );
}
