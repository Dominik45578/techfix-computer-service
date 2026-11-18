import react, { reactCompilerPreset } from '@vitejs/plugin-react'
import babel from '@rolldown/plugin-babel'
import { defineConfig, loadEnv } from 'vite'

// Konfiguracja czytana z głównego pliku .env (katalog nadrzędny), żeby docker i dev
// korzystały z jednego źródła ustawień.
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, '..', '')
  const backend = env.BACKEND_URL || `http://localhost:${env.BACKEND_DEV_PORT || 8080}`

  // Całe API backendu siedzi pod /api; to samo proxuje nginx w kontenerze (frontend/nginx.conf).
  const proxy = { '/api': { target: backend } }

  return {
    plugins: [react(), babel({ presets: [reactCompilerPreset()] })],
    server: {
      port: Number(env.FRONTEND_DEV_PORT) || 5173,
      strictPort: true,
      proxy,
    },
  }
})
