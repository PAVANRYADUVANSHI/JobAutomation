import React from 'react';
import ReactDOM from 'react-dom/client';
import { registerSW } from 'virtual:pwa-register';
import App from './App';
import './index.css';

// Auto-reload when a new SW version is available
registerSW({
  immediate: true,
  onNeedRefresh() {
    // New content available — reload silently (autoUpdate handles this)
    window.location.reload();
  },
  onOfflineReady() {
    console.log('App ready to work offline');
  },
});

ReactDOM.createRoot(document.getElementById('root')!).render(<App />);
