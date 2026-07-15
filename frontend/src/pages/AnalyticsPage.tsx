import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { AppDispatch, RootState } from '../store';
import { fetchAnalytics } from '../store/slices/analyticsSlice';
import TrackSplit from '../components/analytics/TrackSplit';
import DailyChart from '../components/analytics/DailyChart';
import StatsCard from '../components/dashboard/StatsCard';

export default function AnalyticsPage() {
  const dispatch = useDispatch<AppDispatch>();
  const { data } = useSelector((s: RootState) => s.analytics);

  useEffect(() => { dispatch(fetchAnalytics()); }, [dispatch]);

  const d = data ?? { applied: 0, responses: 0, interviews: 0, offers: 0, shortlisted: 0, rejected: 0, javaTrackTotal: 0, genaiTrackTotal: 0, javaTrackApplied: 0, genaiTrackApplied: 0, dailyCounts: {}, totalApplications: 0 };

  const javaConversionRate = d.javaTrackTotal > 0 ? Math.round((d.javaTrackApplied / d.javaTrackTotal) * 100) : 0;
  const genaiConversionRate = d.genaiTrackTotal > 0 ? Math.round((d.genaiTrackApplied / d.genaiTrackTotal) * 100) : 0;

  return (
    <div className="p-6 space-y-6">
      <h1 className="text-2xl font-bold text-white">Analytics</h1>

      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <StatsCard label="Total Applied" value={d.applied} color="indigo" />
        <StatsCard label="Responses" value={d.responses} color="yellow" />
        <StatsCard label="Interviews" value={d.interviews} color="green" />
        <StatsCard label="Offers" value={d.offers} color="emerald" />
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div className="bg-gray-900 border border-gray-800 rounded-xl p-4">
          <h2 className="text-white font-semibold mb-4">Track Conversion Rate</h2>
          <div className="space-y-4">
            <div>
              <div className="flex justify-between text-sm mb-1">
                <span className="text-blue-400">Java Full-Stack</span>
                <span className="text-white">{javaConversionRate}% applied</span>
              </div>
              <div className="h-2 bg-gray-800 rounded-full">
                <div className="h-2 bg-blue-500 rounded-full" style={{ width: `${javaConversionRate}%` }} />
              </div>
              <p className="text-gray-500 text-xs mt-1">{d.javaTrackApplied} / {d.javaTrackTotal} shortlisted</p>
            </div>
            <div>
              <div className="flex justify-between text-sm mb-1">
                <span className="text-purple-400">GenAI</span>
                <span className="text-white">{genaiConversionRate}% applied</span>
              </div>
              <div className="h-2 bg-gray-800 rounded-full">
                <div className="h-2 bg-purple-500 rounded-full" style={{ width: `${genaiConversionRate}%` }} />
              </div>
              <p className="text-gray-500 text-xs mt-1">{d.genaiTrackApplied} / {d.genaiTrackTotal} shortlisted</p>
            </div>
          </div>
        </div>
        <TrackSplit data={d} />
      </div>

      <DailyChart dailyCounts={d.dailyCounts} />

      <div className="bg-gray-900 border border-gray-800 rounded-xl p-4">
        <h2 className="text-white font-semibold mb-4">Full Pipeline Breakdown</h2>
        <div className="grid grid-cols-3 md:grid-cols-6 gap-3">
          {[
            { label: 'Shortlisted', value: d.shortlisted, color: 'yellow' },
            { label: 'Applied', value: d.applied, color: 'blue' },
            { label: 'Response', value: d.responses, color: 'indigo' },
            { label: 'Interview', value: d.interviews, color: 'green' },
            { label: 'Rejected', value: d.rejected, color: 'red' },
            { label: 'Offer', value: d.offers, color: 'emerald' },
          ].map(item => (
            <div key={item.label} className="bg-gray-800 rounded-lg p-3 text-center">
              <p className="text-2xl font-bold text-white">{item.value}</p>
              <p className="text-gray-400 text-xs mt-1">{item.label}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
