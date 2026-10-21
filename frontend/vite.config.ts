import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'
import fs from 'fs'

function getDynamicBackendTarget(): string {
  const candidates = [
    path.resolve(__dirname, '../.active_backend_port'),
    path.resolve(__dirname, '../backend-java/.active_port'),
    path.resolve(__dirname, './.active_backend_port'),
  ]
  for (const file of candidates) {
    if (fs.existsSync(file)) {
      try {
        const port = fs.readFileSync(file, 'utf-8').trim()
        if (port && !isNaN(Number(port))) {
          return `http://localhost:${port}`
        }
      } catch {}
    }
  }
  return 'http://localhost:5001'
}

export default defineConfig({
  plugins: [react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  server: {
    port: 5173,
    proxy: {
      '/api': {
        target: 'http://localhost:5001',
        changeOrigin: true,
        router: () => getDynamicBackendTarget(),
      },
    },
  },
})
