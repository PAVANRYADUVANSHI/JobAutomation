import React, { useEffect, useState } from 'react';
import { profileService } from '../services/jobService';
import { CandidateProfile } from '../types';

export default function SettingsPage() {
  const [profile, setProfile] = useState<CandidateProfile | null>(null);
  const [saving, setSaving] = useState(false);
  const [saved, setSaved] = useState(false);

  useEffect(() => {
    profileService.get().then(r => setProfile(r.data.candidate));
  }, []);

  const toggleMode = async () => {
    if (!profile) return;
    setSaving(true);
    const newMode = profile.reviewMode === 'MANUAL' ? 'AUTO' : 'MANUAL';
    const res = await profileService.updateReviewMode(newMode);
    setProfile(res.data);
    setSaving(false);
    setSaved(true);
    setTimeout(() => setSaved(false), 2000);
  };

  if (!profile) return (
    <div className="p-6 max-w-2xl">
      <h1 className="text-2xl font-bold text-white mb-4">Settings</h1>
      <div className="bg-gray-900 border border-gray-800 rounded-xl p-8 text-center">
        <p className="text-gray-500 text-sm">Connecting to backend...</p>
      </div>
    </div>
  );

  return (
    <div className="p-6 max-w-2xl space-y-6">
      <h1 className="text-2xl font-bold text-white">Settings</h1>

      {/* Candidate Info */}
      <div className="bg-gray-900 border border-gray-800 rounded-xl p-5 space-y-3">
        <h2 className="text-white font-semibold">Candidate Profile</h2>
        <div className="grid grid-cols-2 gap-3 text-sm">
          {[
            ['Name', profile.name],
            ['Phone', profile.phone],
            ['LinkedIn', profile.linkedin],
            ['GitHub', profile.githubHandle],
            ['Portfolio', profile.portfolio],
            ['Location', profile.location],
            ['Experience', profile.experienceLevel],
          ].map(([label, value]) => (
            <div key={label}>
              <p className="text-gray-500 text-xs">{label}</p>
              <p className="text-gray-200">{value || '—'}</p>
            </div>
          ))}
        </div>
      </div>

      {/* Review Mode Toggle */}
      <div className="bg-gray-900 border border-gray-800 rounded-xl p-5">
        <h2 className="text-white font-semibold mb-2">Review Mode</h2>
        <p className="text-gray-400 text-sm mb-4">
          <strong className="text-yellow-400">MANUAL (recommended):</strong> Review each application before submitting.<br />
          <strong className="text-orange-400">AUTO:</strong> Automatically submits all 25 shortlisted applications daily.
          <span className="block mt-1 text-red-400 text-xs">
            ⚠️ AUTO mode submits without human review. Risk of platform bans and low-quality applications. Start with MANUAL.
          </span>
        </p>
        <div className="flex items-center gap-4">
          <div className={`px-4 py-2 rounded-lg text-sm font-semibold ${profile.reviewMode === 'MANUAL' ? 'bg-green-700 text-white' : 'bg-gray-800 text-gray-400'}`}>
            MANUAL
          </div>
          <button
            onClick={toggleMode}
            disabled={saving}
            className="relative w-14 h-7 rounded-full transition-colors duration-200 focus:outline-none"
            style={{ backgroundColor: profile.reviewMode === 'AUTO' ? '#f97316' : '#374151' }}
          >
            <span
              className="absolute top-0.5 left-0.5 w-6 h-6 bg-white rounded-full shadow transition-transform duration-200"
              style={{ transform: profile.reviewMode === 'AUTO' ? 'translateX(28px)' : 'translateX(0)' }}
            />
          </button>
          <div className={`px-4 py-2 rounded-lg text-sm font-semibold ${profile.reviewMode === 'AUTO' ? 'bg-orange-700 text-white' : 'bg-gray-800 text-gray-400'}`}>
            AUTO
          </div>
          {saved && <span className="text-green-400 text-sm">Saved ✓</span>}
        </div>
      </div>

      {/* Daily Target */}
      <div className="bg-gray-900 border border-gray-800 rounded-xl p-5">
        <h2 className="text-white font-semibold mb-1">Daily Shortlist Target</h2>
        <p className="text-gray-300 text-2xl font-bold">{profile.dailyShortlistTarget}</p>
        <p className="text-gray-500 text-sm">applications per day</p>
      </div>
    </div>
  );
}
