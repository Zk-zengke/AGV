import vue from '@vitejs/plugin-vue'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
proxy: {
      // 联调直连 service-agv：网关对带请求体的 POST 转发存在 502 问题，
      // 开发阶段由本地代理直连业务服务，并去掉 /api 前缀；网关与 Nacos 仍正常运行
      '/api': {
        target: 'http://localhost:8000',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
    },
  },
})
