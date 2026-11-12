import vue from '@vitejs/plugin-vue'
import { defineConfig } from 'vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue()],
  server: {
    // 固定监听地址与端口，避免每次启动端口漂移
    host: '127.0.0.1',
    port: 26880,
    strictPort: true,
    proxy: {
      // 联调直连 service-agv：网关对带请求体的 POST 转发存在 502 问题，
      // 开发阶段由本地代理直连业务服务，并去掉 /api 前缀；网关与 Nacos 仍正常运行
      '/api': {
        target: 'http://localhost:8000',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, ''),
      },
      // WebSocket 代理：前端经 /ws 路径升级连接到 service-agv 的 /ws/agv
      '/ws': {
        target: 'http://localhost:8000',
        ws: true,
        changeOrigin: true,
      },
    },
  },
})
