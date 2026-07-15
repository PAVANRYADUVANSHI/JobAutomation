import React from 'react';

export default function JALogo({ size = 36 }: { size?: number }) {
  const id = `ja-${size}`;
  return (
    <svg width={size} height={size} viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
      <defs>
        <linearGradient id={`${id}-g1`} x1="0" y1="0" x2="48" y2="48" gradientUnits="userSpaceOnUse">
          <stop offset="0%" stopColor="#6366f1" />
          <stop offset="50%" stopColor="#8b5cf6" />
          <stop offset="100%" stopColor="#06b6d4" />
        </linearGradient>
        <linearGradient id={`${id}-g2`} x1="48" y1="0" x2="0" y2="48" gradientUnits="userSpaceOnUse">
          <stop offset="0%" stopColor="#06b6d4" />
          <stop offset="100%" stopColor="#6366f1" />
        </linearGradient>
        <filter id={`${id}-glow`} x="-20%" y="-20%" width="140%" height="140%">
          <feGaussianBlur stdDeviation="1.8" result="blur" />
          <feMerge><feMergeNode in="blur" /><feMergeNode in="SourceGraphic" /></feMerge>
        </filter>
        <filter id={`${id}-shadow`} x="-30%" y="-30%" width="160%" height="160%">
          <feDropShadow dx="0" dy="0" stdDeviation="3" floodColor="#6366f1" floodOpacity="0.6" />
        </filter>
      </defs>

      {/* Outer glow ring */}
      <circle cx="24" cy="24" r="22" fill="none" stroke={`url(#${id}-g1)`} strokeWidth="0.5" opacity="0.3" />

      {/* Main hexagon background */}
      <path
        d="M24 3 L42 13.5 L42 34.5 L24 45 L6 34.5 L6 13.5 Z"
        fill={`url(#${id}-g1)`}
        opacity="0.12"
      />
      {/* Hexagon border */}
      <path
        d="M24 3 L42 13.5 L42 34.5 L24 45 L6 34.5 L6 13.5 Z"
        fill="none"
        stroke={`url(#${id}-g1)`}
        strokeWidth="1.5"
        filter={`url(#${id}-shadow)`}
      />
      {/* Inner hexagon accent */}
      <path
        d="M24 9 L38 17 L38 31 L24 39 L10 31 L10 17 Z"
        fill="none"
        stroke={`url(#${id}-g2)`}
        strokeWidth="0.4"
        opacity="0.35"
      />

      {/* J — bold, clean */}
      <text
        x="7"
        y="29"
        fontFamily="'Segoe UI', 'SF Pro Display', system-ui, sans-serif"
        fontWeight="900"
        fontSize="20"
        fill={`url(#${id}-g1)`}
        filter={`url(#${id}-glow)`}
        letterSpacing="-0.5"
      >J</text>

      {/* A — bold, clean */}
      <text
        x="22"
        y="29"
        fontFamily="'Segoe UI', 'SF Pro Display', system-ui, sans-serif"
        fontWeight="900"
        fontSize="20"
        fill={`url(#${id}-g2)`}
        filter={`url(#${id}-glow)`}
        letterSpacing="-0.5"
      >A</text>

      {/* JOBS AUTOMATION label */}
      <text
        x="24"
        y="38"
        textAnchor="middle"
        fontFamily="'Segoe UI', 'SF Pro Display', system-ui, sans-serif"
        fontWeight="700"
        fontSize="4.2"
        fill={`url(#${id}-g1)`}
        letterSpacing="0.6"
        opacity="0.95"
      >JOBS AUTOMATION</text>

      {/* Bottom accent bar */}
      <rect x="9" y="40" width="30" height="1.5" rx="1" fill={`url(#${id}-g1)`} opacity="0.7" />

      {/* Top-right dot accent */}
      <circle cx="38" cy="10" r="2" fill="#06b6d4" opacity="0.8" />
    </svg>
  );
}
