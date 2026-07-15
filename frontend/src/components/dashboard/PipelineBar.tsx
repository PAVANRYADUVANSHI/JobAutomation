import React from 'react';
import { AnalyticsData } from '../../types';

const STAGES = [
  { key: 'shortlisted', label: 'Shortlisted', color: 'bg-yellow-500' },
  { key: 'applied', label: 'Applied', color: 'bg-blue-500' },
  { key: 'responses', label: 'Response', color: 'bg-indigo-500' },
  { key: 'interviews', label: 'Interview', color: 'bg-green-500' },
  { key: 'rejected', label: 'Rejected', color: 'bg-red-500' },
  { key: 'offers', label: 'Offer', color: 'bg-emerald-500' },
];

export default function PipelineBar({ data }: { data: AnalyticsData }) {
  const max = Math.max(data.shortlisted, 1);
  return (
    <div className="bg-gray-900 border border-gray-800 rounded-xl p-4">
      <h2 className="text-white font-semibold mb-4">Application Pipeline</h2>
      <div className="space-y-2">
        {STAGES.map(s => {
          const val = (data as any)[s.key] as number;
          return (
            <div key={s.key} className="flex items-center gap-3">
              <span className="text-gray-400 text-xs w-20 text-right">{s.label}</span>
              <div className="flex-1 h-5 bg-gray-800 rounded-full overflow-hidden">
                <div
                  className={`h-5 ${s.color} rounded-full transition-all duration-500`}
                  style={{ width: `${(val / max) * 100}%` }}
                />
              </div>
              <span className="text-white text-sm font-mono w-8">{val}</span>
            </div>
          );
        })}
      </div>
    </div>
  );
}
