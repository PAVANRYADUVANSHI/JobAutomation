import React from 'react';
import { AnalyticsData } from '../../types';

export default function TrackSplit({ data }: { data: AnalyticsData }) {
  const total = data.javaTrackTotal + data.genaiTrackTotal || 1;
  const javaPct = Math.round((data.javaTrackTotal / total) * 100);
  const genaiPct = 100 - javaPct;

  return (
    <div className="bg-gray-900 border border-gray-800 rounded-xl p-4">
      <h2 className="text-white font-semibold mb-4">Track Split</h2>
      <div className="flex h-6 rounded-full overflow-hidden mb-3">
        <div className="bg-blue-500 transition-all duration-500" style={{ width: `${javaPct}%` }} />
        <div className="bg-purple-500 transition-all duration-500" style={{ width: `${genaiPct}%` }} />
      </div>
      <div className="flex justify-between text-sm">
        <div className="flex items-center gap-2">
          <span className="w-3 h-3 rounded-full bg-blue-500 inline-block" />
          <span className="text-gray-300">Java FS</span>
          <span className="text-white font-bold">{data.javaTrackTotal}</span>
          <span className="text-gray-500">({javaPct}%)</span>
        </div>
        <div className="flex items-center gap-2">
          <span className="w-3 h-3 rounded-full bg-purple-500 inline-block" />
          <span className="text-gray-300">GenAI</span>
          <span className="text-white font-bold">{data.genaiTrackTotal}</span>
          <span className="text-gray-500">({genaiPct}%)</span>
        </div>
      </div>
    </div>
  );
}
