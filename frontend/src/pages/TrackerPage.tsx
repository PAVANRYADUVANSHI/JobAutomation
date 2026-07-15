import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { AppDispatch, RootState } from '../store';
import { fetchAll, updateStatus } from '../store/slices/applicationSlice';

const STATUSES = ['ALL', 'SHORTLISTED', 'APPLIED', 'RESPONSE', 'INTERVIEW', 'REJECTED', 'OFFER'];

const STATUS_COLORS: Record<string, string> = {
  SHORTLISTED: 'bg-yellow-900 text-yellow-300',
  APPLIED: 'bg-blue-900 text-blue-300',
  RESPONSE: 'bg-indigo-900 text-indigo-300',
  INTERVIEW: 'bg-green-900 text-green-300',
  REJECTED: 'bg-red-900 text-red-300',
  OFFER: 'bg-emerald-900 text-emerald-300',
};

export default function TrackerPage() {
  const dispatch = useDispatch<AppDispatch>();
  const { all } = useSelector((s: RootState) => s.applications);
  const [statusFilter, setStatusFilter] = useState('ALL');
  const [trackFilter, setTrackFilter] = useState('ALL');

  useEffect(() => { dispatch(fetchAll()); }, [dispatch]);

  const filtered = all
    .filter(a => statusFilter === 'ALL' || a.status === statusFilter)
    .filter(a => trackFilter === 'ALL' || a.track === trackFilter);

  const moveStatus = (id: number, status: string) => dispatch(updateStatus({ id, status }));

  return (
    <div className="p-6 space-y-4">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold text-white">Application Tracker</h1>
        <div className="flex gap-2">
          <select
            value={trackFilter}
            onChange={e => setTrackFilter(e.target.value)}
            className="bg-gray-800 border border-gray-700 text-white text-sm rounded-lg px-3 py-2"
          >
            <option value="ALL">All Tracks</option>
            <option value="JAVA_FULLSTACK">Java Full-Stack</option>
            <option value="GENAI">GenAI</option>
          </select>
          <select
            value={statusFilter}
            onChange={e => setStatusFilter(e.target.value)}
            className="bg-gray-800 border border-gray-700 text-white text-sm rounded-lg px-3 py-2"
          >
            {STATUSES.map(s => <option key={s} value={s}>{s}</option>)}
          </select>
        </div>
      </div>

      <div className="overflow-x-auto">
        <table className="w-full text-sm">
          <thead>
            <tr className="text-gray-500 border-b border-gray-800">
              <th className="text-left py-3 px-2">Job</th>
              <th className="text-left py-3 px-2">Company</th>
              <th className="text-left py-3 px-2">Track</th>
              <th className="text-left py-3 px-2">Resume</th>
              <th className="text-left py-3 px-2">Score</th>
              <th className="text-left py-3 px-2">Status</th>
              <th className="text-left py-3 px-2">Applied</th>
              <th className="text-left py-3 px-2">Actions</th>
            </tr>
          </thead>
          <tbody>
            {filtered.map(app => (
              <tr key={app.id} className="border-b border-gray-800/50 hover:bg-gray-800/30 transition">
                <td className="py-3 px-2">
                  <p className="text-white font-medium max-w-xs truncate">{app.jobListing.title}</p>
                  <p className="text-gray-500 text-xs">{app.jobListing.source}</p>
                </td>
                <td className="py-3 px-2 text-gray-300">{app.jobListing.company}</td>
                <td className="py-3 px-2">
                  <span className={`text-xs px-2 py-0.5 rounded-full ${app.track === 'GENAI' ? 'bg-purple-900 text-purple-300' : 'bg-blue-900 text-blue-300'}`}>
                    {app.track === 'GENAI' ? 'GenAI' : 'Java FS'}
                  </span>
                </td>
                <td className="py-3 px-2 text-gray-400 text-xs">
                  {app.resumeVersion === 'genAILeaning' ? 'GenAI Resume' : 'Java FS Resume'}
                </td>
                <td className="py-3 px-2 text-green-400 font-mono">{Math.round(app.matchScore * 100)}%</td>
                <td className="py-3 px-2">
                  <span className={`text-xs px-2 py-0.5 rounded-full ${STATUS_COLORS[app.status] || 'bg-gray-700 text-gray-300'}`}>
                    {app.status}
                  </span>
                </td>
                <td className="py-3 px-2 text-gray-400 text-xs">
                  {app.submittedAt ? new Date(app.submittedAt).toLocaleDateString() : '—'}
                </td>
                <td className="py-3 px-2">
                  <select
                    value={app.status}
                    onChange={e => moveStatus(app.id, e.target.value)}
                    className="bg-gray-800 border border-gray-700 text-white text-xs rounded px-2 py-1"
                  >
                    {STATUSES.filter(s => s !== 'ALL').map(s => <option key={s} value={s}>{s}</option>)}
                  </select>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        {filtered.length === 0 && (
          <p className="text-gray-500 text-center py-8">No applications found.</p>
        )}
      </div>
    </div>
  );
}
