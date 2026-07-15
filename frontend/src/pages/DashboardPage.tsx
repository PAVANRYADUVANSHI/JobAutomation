import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { AppDispatch, RootState } from '../store';
import { fetchAnalytics, fetchSchedulerLogs } from '../store/slices/analyticsSlice';
import { fetchQueue } from '../store/slices/applicationSlice';
import { schedulerService } from '../services/jobService';
import StatsCard from '../components/dashboard/StatsCard';
import TrackSplit from '../components/analytics/TrackSplit';
import DailyChart from '../components/analytics/DailyChart';
import PipelineBar from '../components/dashboard/PipelineBar';

export default function DashboardPage() {
  const dispatch = useDispatch<AppDispatch>();
  const { data, logs, loading } = useSelector((s: RootState) => s.analytics);
  const { queue } = useSelector((s: RootState) => s.applications);

  useEffect(() => {
    dispatch(fetchAnalytics());
    dispatch(fetchSchedulerLogs());
    dispatch(fetchQueue());
  }, [dispatch]);

  const [pipelineRunning, setPipelineRunning] = React.useState(false);

  const runPipeline = async () => {
    setPipelineRunning(true);
    await schedulerService.run();
    // pipeline runs async on backend (~2 min), poll every 15s for 3 min
    let polls = 0;
    const interval = setInterval(() => {
      dispatch(fetchAnalytics());
      dispatch(fetchQueue());
      if (++polls >= 12) { clearInterval(interval); setPipelineRunning(false); }
    }, 15000);
  };

  const d = data ?? { applied: 0, responses: 0, interviews: 0, offers: 0, shortlisted: 0, rejected: 0, javaTrackTotal: 0, genaiTrackTotal: 0, javaTrackApplied: 0, genaiTrackApplied: 0, dailyCounts: {}, totalApplications: 0 };

  return (
    <div className="p-6 space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3">
        <div>
          <h1 className="text-xl font-bold" style={{background:'linear-gradient(135deg,#6366f1,#8b5cf6,#06b6d4)',WebkitBackgroundClip:'text',WebkitTextFillColor:'transparent',backgroundClip:'text'}}>JOBAUTO Dashboard</h1>
          <p className="text-gray-400 text-xs">Pavan R · Fresher Full-Stack + GenAI · 50 jobs/day</p>
        </div>
        <button
          onClick={runPipeline}
          disabled={pipelineRunning}
          className="bg-indigo-600 hover:bg-indigo-500 text-white px-4 py-2 rounded-lg text-sm font-medium transition disabled:opacity-60 w-full sm:w-auto"
        >
          {pipelineRunning ? '⟳ Running... (~2 min)' : '▶ Run Pipeline Now'}
        </button>
      </div>

      {loading && <p className="text-indigo-400 text-xs">⟳ Syncing with backend...</p>}

      {/* Stats Row */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
        <StatsCard label="Total Applied" value={d.applied} color="indigo" />
        <StatsCard label="Responses" value={d.responses} color="yellow" />
        <StatsCard label="Interviews" value={d.interviews} color="green" />
        <StatsCard label="Offers" value={d.offers} color="emerald" />
      </div>

      {/* Pipeline */}
      <PipelineBar data={d} />

      {/* Track Split + Daily Chart */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <TrackSplit data={d} />
        <DailyChart dailyCounts={d.dailyCounts} />
      </div>

      {/* Today's Queue Preview */}
      <div className="bg-gray-900 border border-gray-800 rounded-xl p-4">
        <h2 className="text-white font-semibold mb-3">Today's Queue ({queue.length} shortlisted)</h2>
        {queue.length === 0 ? (
          <p className="text-gray-500 text-sm">No jobs shortlisted yet. Run the pipeline to fetch and score jobs.</p>
        ) : (
          <div className="space-y-2 max-h-64 overflow-y-auto">
            {queue.slice(0, 5).map(app => (
              <div key={app.id} className="flex items-center justify-between bg-gray-800 rounded-lg px-3 py-2">
                <div>
                  <p className="text-white text-sm font-medium">{app.jobListing.title}</p>
                  <p className="text-gray-400 text-xs">{app.jobListing.company} · {app.jobListing.location}</p>
                </div>
                <div className="flex items-center gap-2">
                  <span className={`text-xs px-2 py-0.5 rounded-full ${app.track === 'GENAI' ? 'bg-purple-900 text-purple-300' : 'bg-blue-900 text-blue-300'}`}>
                    {app.track === 'GENAI' ? 'GenAI' : 'Java'}
                  </span>
                  <span className="text-green-400 text-xs font-mono">{Math.round(app.matchScore * 100)}%</span>
                </div>
              </div>
            ))}
            {queue.length > 5 && <p className="text-gray-500 text-xs text-center">+{queue.length - 5} more in queue</p>}
          </div>
        )}
      </div>

      {/* Scheduler Logs */}
      <div className="bg-gray-900 border border-gray-800 rounded-xl p-4">
        <h2 className="text-white font-semibold mb-3">Recent Scheduler Runs</h2>
        {logs.length === 0 ? (
          <p className="text-gray-500 text-sm">No runs yet.</p>
        ) : (
          <div className="space-y-1">
            {logs.slice(0, 5).map((log: any) => (
              <div key={log.id} className="flex items-center justify-between text-sm">
                <span className="text-gray-400">{new Date(log.runAt).toLocaleString()}</span>
                <span className="text-gray-300">{log.summary}</span>
                <span className={`text-xs px-2 py-0.5 rounded-full ${log.mode === 'AUTO' ? 'bg-orange-900 text-orange-300' : 'bg-gray-700 text-gray-300'}`}>
                  {log.mode}
                </span>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
