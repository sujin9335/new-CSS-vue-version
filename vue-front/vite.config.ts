import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  server: {
    host: '0.0.0.0', //모든 ip에서 접속가능하게
    port: 25003, // 원하는 포트 변경
    proxy: { //axios 설정
      "/a": {
        target: "http://localhost:25002/",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/a/, ""),
      },
      // "/": {
      //   target: "http://localhost:28000/",
      //   changeOrigin: true,
      //   rewrite: (path) => path.replace(/^\/SpApI/, ""),
      // },
      
      
    },
  },
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },

})
