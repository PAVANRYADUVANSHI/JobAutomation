import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { AppDispatch, RootState } from '../store';
import { fetchQueue, updateStatus, triggerShortlist } from '../store/slices/applicationSlice';
import { Application } from '../types';

const STATUS_COLORS: Record<string, string> = {
  SHORTLISTED: 'bg-yellow-900 text-yellow-300',
  APPLIED: 'bg-blue-900 text-blue-300',
  RESPONSE: 'bg-indigo-900 text-indigo-300',
  INTERVIEW: 'bg-green-900 text-green-300',
  REJECTED: 'bg-red-900 text-red-300',
  OFFER: 'bg-emerald-900 text-emerald-300',
};

export default function QueuePage() {
  const dispatch = useDispatch<AppDispatch>();
  const { queue, loading } = useSelector((s: RootState) => s.applications);
  const [expanded, setExpanded] = useState<number | null>(null);
  const [trackFilter, setTrackFilter] = useState<string>('ALL');

  useEffect(() => { dispatch(fetchQueue()); }, [dispatch]);

  const filtered = trackFilter === 'ALL' ? queue : queue.filter(a => a.track === trackFilter);

  const [coverLetters, setCoverLetters] = useState<Record<number, string>>({});
  const [regenerating, setRegenerating] = useState<number | null>(null);
  const [actionMsg, setActionMsg] = useState<{ id: number; msg: string; ok: boolean } | null>(null);
  const [actioning, setActioning] = useState<number | null>(null);

  const flash = (id: number, msg: string, ok: boolean) => {
    setActionMsg({ id, msg, ok });
    setTimeout(() => setActionMsg(null), 4000);
  };

  const regenerateCoverLetter = async (app: Application) => {
    setRegenerating(app.id);
    try {
      const res = await import('../services/api').then(m => m.default.post(`/api/applications/${app.id}/regenerate-cover-letter`));
      setCoverLetters(prev => ({ ...prev, [app.id]: res.data.coverLetter }));
    } catch { flash(app.id, 'Failed to regenerate cover letter', false); }
    setRegenerating(null);
  };

  const getCoverLetter = (app: Application) => coverLetters[app.id] ?? app.coverLetter;

  const approve = async (app: Application) => {
    setActioning(app.id);
    const res = await dispatch(updateStatus({ id: app.id, status: 'APPLIED' }));
    if (updateStatus.rejected.match(res)) flash(app.id, 'Failed to mark as applied', false);
    setActioning(null);
  };

  const reject = async (app: Application) => {
    setActioning(app.id);
    const res = await dispatch(updateStatus({ id: app.id, status: 'REJECTED' }));
    if (updateStatus.rejected.match(res)) flash(app.id, 'Failed to skip', false);
    setActioning(null);
  };

  const emailApply = async (app: Application) => {
    setActioning(app.id);
    try {
      const api = await import('../services/api').then(m => m.default);
      const res = await api.post(`/api/applications/${app.id}/email-apply`);
      if (res.data.sent) {
        dispatch(updateStatus({ id: app.id, status: 'APPLIED' }));
        flash(app.id, '✓ Email sent to HR', true);
      } else {
        flash(app.id, res.data.message || 'No HR email configured for this company', false);
      }
    } catch (e: any) {
      flash(app.id, e?.response?.data?.message || 'Email failed — check mail config', false);
    }
    setActioning(null);
  };

  return (
    <div className="p-6 space-y-4">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3">
        <div>
          <h1 className="text-xl font-bold text-white">Review Queue</h1>
          <p className="text-gray-400 text-xs">{queue.length} jobs shortlisted today</p>
        </div>
        <div className="flex gap-2">
          <select
            value={trackFilter}
            onChange={e => setTrackFilter(e.target.value)}
            className="bg-gray-800 border border-gray-700 text-white text-sm rounded-lg px-3 py-2 flex-1 sm:flex-none"
          >
            <option value="ALL">All Tracks</option>
            <option value="JAVA_FULLSTACK">Java FS</option>
            <option value="GENAI">GenAI</option>
          </select>
          <button
            onClick={() => dispatch(triggerShortlist())}
            className="bg-indigo-600 hover:bg-indigo-500 text-white px-3 py-2 rounded-lg text-sm font-medium transition flex-1 sm:flex-none"
          >
            Re-Shortlist
          </button>
        </div>
      </div>

      {loading && <p className="text-gray-400">Loading...</p>}

      {actionMsg && (
        <p className={`text-sm px-4 py-2 rounded-lg ${actionMsg.ok ? 'bg-green-900/40 text-green-400' : 'bg-red-900/40 text-red-400'}`}>
          {actionMsg.msg}
        </p>
      )}

      {filtered.length === 0 && !loading && (
        <div className="bg-gray-900 border border-gray-800 rounded-xl p-8 text-center">
          <p className="text-gray-400">Queue is empty. Run the pipeline to fetch and shortlist jobs.</p>
        </div>
      )}

      <div className="space-y-3">
        {filtered.map(app => (
          <div key={app.id} className="bg-gray-900 border border-gray-800 rounded-xl overflow-hidden">
            <div
              className="flex flex-col sm:flex-row sm:items-center justify-between p-4 cursor-pointer hover:bg-gray-800 transition gap-3"
              onClick={() => setExpanded(expanded === app.id ? null : app.id)}
            >
              <div className="flex-1 min-w-0">
                <div className="flex items-center gap-2 mb-1">
                  <span className={`text-xs px-2 py-0.5 rounded-full ${app.track === 'GENAI' ? 'bg-purple-900 text-purple-300' : 'bg-blue-900 text-blue-300'}`}>
                    {app.track === 'GENAI' ? 'GenAI' : 'Java FS'}
                  </span>
                  <span className={`text-xs px-2 py-0.5 rounded-full ${STATUS_COLORS[app.status] || 'bg-gray-700 text-gray-300'}`}>
                    {app.status.charAt(0) + app.status.slice(1).toLowerCase()}
                  </span>
                  <span className="text-green-400 text-xs font-mono ml-auto">{Math.round(app.matchScore * 100)}%</span>
                </div>
                <p className="text-white font-medium truncate">{app.jobListing.title}</p>
                <p className="text-gray-400 text-sm">{app.jobListing.company} · {app.jobListing.location}</p>
              </div>
              <div className="flex gap-2" onClick={e => e.stopPropagation()}>
                <button
                  onClick={() => emailApply(app)}
                  disabled={actioning === app.id}
                  className="bg-indigo-700 hover:bg-indigo-600 text-white text-xs px-3 py-2 rounded-lg transition flex-1 sm:flex-none disabled:opacity-50"
                >
                  {actioning === app.id ? '...' : '✉ Email'}
                </button>
                <button
                  onClick={() => approve(app)}
                  disabled={actioning === app.id}
                  className="bg-green-700 hover:bg-green-600 text-white text-xs px-3 py-2 rounded-lg transition flex-1 sm:flex-none disabled:opacity-50"
                >
                  ✓ Apply
                </button>
                <button
                  onClick={() => reject(app)}
                  disabled={actioning === app.id}
                  className="bg-red-900 hover:bg-red-800 text-red-300 text-xs px-3 py-2 rounded-lg transition flex-1 sm:flex-none disabled:opacity-50"
                >
                  ✕ Skip
                </button>
              </div>
            </div>

            {expanded === app.id && (
              <div className="border-t border-gray-800 p-4 space-y-3">
                <div className="grid grid-cols-2 gap-4 text-sm">
                  <div>
                    <p className="text-gray-500 text-xs mb-1">Source</p>
                    <p className="text-gray-300">{app.jobListing.source}</p>
                  </div>
                  <div>
                    <p className="text-gray-500 text-xs mb-1">Posted</p>
                    <p className="text-gray-300">{app.jobListing.postedDate}</p>
                  </div>
                </div>
                {app.jobListing.description && (
                  <div>
                    <p className="text-gray-500 text-xs mb-1">Description</p>
                    <p className="text-gray-300 text-sm line-clamp-4">{app.jobListing.description}</p>
                  </div>
                )}
                <div>
                  <div className="flex items-center justify-between mb-1">
                    <p className="text-gray-500 text-xs">Cover Letter Preview</p>
                    <button
                      onClick={() => regenerateCoverLetter(app)}
                      disabled={regenerating === app.id}
                      className="text-xs text-indigo-400 hover:text-indigo-300 disabled:opacity-50 transition"
                    >
                      {regenerating === app.id ? '↺ Regenerating...' : '↺ Regenerate'}
                    </button>
                  </div>
                  <pre className="text-gray-300 text-xs bg-gray-800 rounded-lg p-3 whitespace-pre-wrap max-h-40 overflow-y-auto">
                    {getCoverLetter(app)}
                  </pre>
                </div>
                {app.jobListing.applyUrl && (
                  <a
                    href={app.jobListing.applyUrl}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="inline-block text-indigo-400 hover:text-indigo-300 text-sm underline"
                  >
                    Open Job Listing ↗
                  </a>
                )}
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}
