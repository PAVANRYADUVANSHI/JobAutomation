import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { VitePWA } from 'vite-plugin-pwa';

export default defineConfig({
  plugins: [
    react(),
    VitePWA({
      registerType: 'autoUpdate',
      includeAssets: ['logo192.png', 'logo512.png'],
      manifest: {
        name: 'JOBAUTO — Pavan R Yadav',
        short_name: 'JOBAUTO',
        description: 'Job Application Automation — Pavan R Yadav',
        start_url: '/',
        display: 'standalone',
        background_color: '#060a12',
        theme_color: '#6366f1',
        orientation: 'portrait-primary',
        icons: [
          { src: '/logo192.png', sizes: '192x192', type: 'image/png', purpose: 'any maskable' },
          { src: '/logo512.png', sizes: '512x512', type: 'image/png', purpose: 'any maskable' },
        ],
      },
      workbox: {
        globPatterns: ['**/*.{js,css,html,ico,png,svg}'],
        runtimeCaching: [
          {
            urlPattern: /^https?:.*\/api\/.*/i,
            handler: 'NetworkFirst',
            options: { cacheName: 'api-cache', networkTimeoutSeconds: 10 },
          },
        ],
      },
    }),
  ],
  server: {
    port: 3000,
    proxy: {
      '/api': { target: 'http://localhost:8080', changeOrigin: true },
    },
  },
});
