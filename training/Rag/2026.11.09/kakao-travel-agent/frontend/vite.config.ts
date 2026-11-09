import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    // /api 요청은 스프링부트(8060)로 전달
    proxy: {
      '/api': 'http://localhost:8060'
    }
  }
})
