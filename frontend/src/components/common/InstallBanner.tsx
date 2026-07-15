import React, { useEffect, useState } from 'react';

function isIOS() {
  return /iphone|ipad|ipod/i.test(navigator.userAgent);
}

function isInStandaloneMode() {
  return ('standalone' in window.navigator && (window.navigator as any).standalone) ||
    window.matchMedia('(display-mode: standalone)').matches;
}

export default function InstallBanner() {
  const [deferredPrompt, setDeferredPrompt] = useState<any>(null);
  const [showAndroid, setShowAndroid] = useState(false);
  const [showIOS, setShowIOS] = useState(false);

  useEffect(() => {
    // Already installed — don't show
    if (isInStandaloneMode()) return;

    // Android/Chrome — wait for browser prompt
    const handler = (e: any) => {
      e.preventDefault();
      setDeferredPrompt(e);
      setShowAndroid(true);
    };
    window.addEventListener('beforeinstallprompt', handler);

    // iOS Safari — show manual instructions after 3s delay
    if (isIOS()) {
      const t = setTimeout(() => setShowIOS(true), 3000);
      return () => { clearTimeout(t); window.removeEventListener('beforeinstallprompt', handler); };
    }

    return () => window.removeEventListener('beforeinstallprompt', handler);
  }, []);

  const handleInstall = async () => {
    if (!deferredPrompt) return;
    deferredPrompt.prompt();
    const { outcome } = await deferredPrompt.userChoice;
    setDeferredPrompt(null);
    setShowAndroid(false);
  };

  const dismiss = () => { setShowAndroid(false); setShowIOS(false); };

  if (!showAndroid && !showIOS) return null;

  return (
    <div className="fixed bottom-20 left-4 right-4 md:bottom-6 md:left-auto md:right-6 md:w-84 z-50
      bg-gray-900 border border-indigo-500/40 rounded-2xl p-4 shadow-2xl shadow-black/60">

      <div className="flex items-start gap-3">
        {/* Icon */}
        <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-indigo-500 to-cyan-500
          flex items-center justify-center font-black text-white text-sm flex-shrink-0">
          PR
        </div>

        <div className="flex-1 min-w-0">
          <p className="text-white text-sm font-semibold">Install PAVAN R App</p>

          {showAndroid && (
            <p className="text-gray-400 text-xs mt-0.5">
              Add to home screen for the full app experience — works offline too.
            </p>
          )}

          {showIOS && (
            <p className="text-gray-400 text-xs mt-0.5 leading-relaxed">
              Tap <span className="text-white font-medium">Share</span> <span className="text-lg leading-none">⎙</span> then{' '}
              <span className="text-white font-medium">"Add to Home Screen"</span> to install.
            </p>
          )}
        </div>

        <button
          onClick={dismiss}
          className="text-gray-500 hover:text-gray-300 text-lg leading-none flex-shrink-0 -mt-0.5"
          aria-label="Dismiss"
        >
          ✕
        </button>
      </div>

      {showAndroid && (
        <button
          onClick={handleInstall}
          className="mt-3 w-full bg-indigo-600 hover:bg-indigo-500 text-white text-sm
            font-semibold py-2 rounded-xl transition"
        >
          Install App
        </button>
      )}
    </div>
  );
}
