import React, { useEffect, useState } from 'react';

export default function InstallBanner() {
  const [prompt, setPrompt] = useState<any>(null);
  const [show, setShow] = useState(false);

  useEffect(() => {
    const handler = (e: any) => { e.preventDefault(); setPrompt(e); setShow(true); };
    window.addEventListener('beforeinstallprompt', handler);
    return () => window.removeEventListener('beforeinstallprompt', handler);
  }, []);

  if (!show) return null;

  return (
    <div className="fixed bottom-20 left-4 right-4 md:bottom-4 md:left-auto md:right-4 md:w-80 z-50 bg-gray-900 border border-indigo-500/40 rounded-2xl p-4 shadow-2xl flex items-center gap-3">
      <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-indigo-500 to-cyan-500 flex items-center justify-center font-black text-white text-sm flex-shrink-0">JA</div>
      <div className="flex-1 min-w-0">
        <p className="text-white text-sm font-semibold">Install JOBAUTO</p>
        <p className="text-gray-400 text-xs">Add to home screen for app experience</p>
      </div>
      <div className="flex gap-2">
        <button onClick={() => setShow(false)} className="text-gray-500 text-xs px-2 py-1 rounded-lg hover:bg-gray-800">✕</button>
        <button
          onClick={() => { prompt?.prompt(); setShow(false); }}
          className="bg-indigo-600 text-white text-xs px-3 py-1.5 rounded-lg font-medium"
        >
          Install
        </button>
      </div>
    </div>
  );
}
