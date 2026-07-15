import React from 'react';

const COLOR_MAP: Record<string, string> = {
  indigo: 'text-indigo-400',
  yellow: 'text-yellow-400',
  green: 'text-green-400',
  emerald: 'text-emerald-400',
  red: 'text-red-400',
  blue: 'text-blue-400',
};

interface Props { label: string; value: number; color: string; }

export default function StatsCard({ label, value, color }: Props) {
  return (
    <div className="bg-gray-900 border border-gray-800 rounded-xl p-4">
      <p className="text-gray-500 text-xs mb-1">{label}</p>
      <p className={`text-3xl font-bold ${COLOR_MAP[color] || 'text-white'}`}>{value}</p>
    </div>
  );
}
