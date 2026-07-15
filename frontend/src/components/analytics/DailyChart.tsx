import React from 'react';

interface Props { dailyCounts: Record<string, number>; }

export default function DailyChart({ dailyCounts }: Props) {
  const entries = Object.entries(dailyCounts).slice(-14).reverse();
  const max = Math.max(...entries.map(([, v]) => v), 1);

  return (
    <div className="bg-gray-900 border border-gray-800 rounded-xl p-4">
      <h2 className="text-white font-semibold mb-4">Daily Applications (last 14 days)</h2>
      {entries.length === 0 ? (
        <p className="text-gray-500 text-sm">No data yet.</p>
      ) : (
        <div className="flex items-end gap-1 h-32">
          {entries.map(([date, count]) => (
            <div key={date} className="flex-1 flex flex-col items-center gap-1">
              <span className="text-white text-xs font-mono">{count}</span>
              <div
                className="w-full bg-indigo-500 rounded-t transition-all duration-500"
                style={{ height: `${(count / max) * 80}px`, minHeight: count > 0 ? '4px' : '0' }}
              />
              <span className="text-gray-600 text-xs" style={{ fontSize: '9px' }}>
                {date.slice(5)}
              </span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
