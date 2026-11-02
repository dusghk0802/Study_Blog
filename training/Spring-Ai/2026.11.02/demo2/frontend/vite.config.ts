import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    // 개발 중에는 /api 요청을 스프링부트(8090)로 전달
    proxy: {
      '/api': 'http://localhost:8090'
    }
  },
  build: {
    // 빌드 결과를 스프링부트 static 폴더에 넣어서 8090 하나로 실행
    outDir: '../src/main/resources/static',
    emptyOutDir: true
  }
})
