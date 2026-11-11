# agv-map-web

排策智演 AGV 集群仿真系统的前端工程，基于 Vue 3、Vite 与 Three.js 构建，负责车间地图、车辆、路径与调度状态的三维可视化。

## 技术栈

- Vue 3（组合式 API，`<script setup>` 语法）
- Vite（开发服务器与构建工具）
- Three.js（三维场景渲染）

## 目录结构

```
src/
├── App.vue          # 主页面，负责定时轮询与数据组织
├── main.js          # 应用入口
├── api/             # 接口请求封装（车辆、任务、地图、算法）
├── components/      # 任务面板、车辆面板、算法切换、地图视图
└── three/           # 三维场景、地图、车辆、路径、障碍物编辑器
```

## 常用命令

```bash
npm install      # 安装依赖
npm run dev      # 启动开发服务器
npm run build    # 构建生产产物到 dist
npm run preview  # 本地预览构建产物
```

## 开发代理

开发环境通过 Vite 代理把 `/api` 前缀的请求转发到本地业务服务 `http://localhost:8000`，并在转发时去掉 `/api` 前缀。由于网关对带请求体的 POST 转发存在 502 问题，开发阶段直连业务服务，网关与 Nacos 仍正常运行。代理配置位于 `vite.config.js`。

## 数据刷新

前端采用定时轮询（1 秒/次）获取车辆与任务的实时状态，不使用 WebSocket。
