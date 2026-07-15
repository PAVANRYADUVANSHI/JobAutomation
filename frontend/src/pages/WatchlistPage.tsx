import React, { useEffect, useState } from 'react';
import { jobService } from '../services/jobService';
import { JobListing } from '../types';

export default function WatchlistPage() {
  const [jobs, setJobs] = useState<JobListing[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    jobService.watchlist().then(r => { setJobs(r.data); setLoading(false); });
  }, []);

  return (
    <div className="p-6 space-y-4">
      <div>
        <h1 className="text-2xl font-bold text-white">Manual Watchlist</h1>
        <p className="text-gray-400 text-sm">
          Companies with no public API — check their careers pages manually and apply directly.
        </p>
      </div>

      {loading && <p className="text-gray-400">Loading...</p>}

      <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
        {jobs.map(job => (
          <div key={job.id} className="bg-gray-900 border border-gray-800 rounded-xl p-4 flex items-center justify-between">
            <div>
              <p className="text-white font-medium">{job.company}</p>
              <p className="text-gray-500 text-xs mt-0.5">No public API — manual check required</p>
            </div>
            {job.applyUrl ? (
              <a
                href={job.applyUrl}
                target="_blank"
                rel="noopener noreferrer"
                className="bg-indigo-600 hover:bg-indigo-500 text-white text-xs px-3 py-1.5 rounded-lg transition whitespace-nowrap"
              >
                View Careers ↗
              </a>
            ) : (
              <span className="text-gray-600 text-xs">No URL</span>
            )}
          </div>
        ))}
        {jobs.length === 0 && !loading && (
          <p className="text-gray-500 col-span-2 text-center py-8">
            No watchlist entries yet. Run the pipeline to populate.
          </p>
        )}
      </div>
    </div>
  );
}
