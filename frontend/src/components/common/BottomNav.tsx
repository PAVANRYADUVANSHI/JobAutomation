import React from 'react';
import { NavLink } from 'react-router-dom';

const TABS = [
  { to: '/dashboard', icon: '⬛', label: 'Home' },
  { to: '/queue',     icon: '📋', label: 'Queue' },
  { to: '/tracker',   icon: '📊', label: 'Tracker' },
  { to: '/analytics', icon: '📈', label: 'Stats' },
  { to: '/settings',  icon: '⚙️', label: 'Settings' },
];

export default function BottomNav() {
  return (
    <nav className="fixed bottom-0 left-0 right-0 z-50 md:hidden bg-gray-950/95 backdrop-blur-xl border-t border-gray-800/60 flex safe-bottom">
      {TABS.map(({ to, icon, label }) => (
        <NavLink
          key={to}
          to={to}
          className={({ isActive }) =>
            `flex-1 flex flex-col items-center justify-center py-2 gap-0.5 text-[10px] font-medium transition-colors ${
              isActive ? 'text-indigo-400' : 'text-gray-500'
            }`
          }
        >
          <span className="text-lg leading-none">{icon}</span>
          {label}
        </NavLink>
      ))}
    </nav>
  );
}
