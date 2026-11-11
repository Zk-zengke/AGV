<template>
  <div class="app">
    <header class="topbar">
      <div class="title">
        <span class="title-main">排策智演</span>
        <span class="title-sub">车间 AGV 集群路径规划仿真与调度可视化系统</span>
      </div>
      <div class="meta">
        <span class="poll-dot" :class="{ on: !error }"></span>
        <span class="meta-text">{{ error ? '连接异常' : '定时轮询中 · 1 秒/次' }}</span>
        <span class="meta-text" v-if="lastUpdate">
          最后更新 {{ formatTime(lastUpdate) }}
        </span>
      </div>
    </header>

    <main class="content">
      <div class="map-wrap">
        <div v-if="loading" class="hint">地图加载中…</div>
        <div v-else-if="error" class="hint error">
          {{ error }}，请确认后端服务与网关已启动。
        </div>
        <MapView
          v-else
          :map-data="mapData"
          :car-list="carList"
          :path-nodes="livePaths"
          @map-saved="onMapSaved"
        />
      </div>
      <aside class="side">
        <AlgorithmSwitch />
        <TaskPanel
          :car-list="carList"
          :stations="mapData ? mapData.stations : []"
          @dispatched="onDispatched"
          @cancelled="onCancelled"
        />
        <CarPanel :car-list="carList" />
      </aside>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import MapView from './components/MapView.vue'
import CarPanel from './components/CarPanel.vue'
import TaskPanel from './components/TaskPanel.vue'
import AlgorithmSwitch from './components/AlgorithmSwitch.vue'
import { getCurrentMap } from './api/map'
import { listAgv } from './api/agv'

const mapData = ref(null)
const carList = ref([])
const activeAgvCodes = ref([])
const loading = ref(true)
const error = ref('')
const lastUpdate = ref(null)
let timer = null

// 多车路径：为每辆任务车辆分配颜色，绑定其实时 plannedPath
const PATH_COLORS = ['#38e1c6', '#a78bfa', '#fb923c', '#f472b6', '#60a5fa']
const livePaths = computed(() =>
  activeAgvCodes.value
    .map((code, i) => {
      const car = carList.value.find((c) => c.agvCode === code)
      return {
        agvCode: code,
        color: PATH_COLORS[i % PATH_COLORS.length],
        nodes: car && car.plannedPath ? car.plannedPath : [],
      }
    })
    .filter((p) => p.nodes.length)
)

function onDispatched(agvCode) {
  if (!activeAgvCodes.value.includes(agvCode)) {
    activeAgvCodes.value.push(agvCode)
  }
}

function onCancelled(agvCode) {
  activeAgvCodes.value = activeAgvCodes.value.filter((c) => c !== agvCode)
}

async function onMapSaved() {
  try {
    mapData.value = await getCurrentMap()
  } catch (e) {
    error.value = e.message
  }
}

async function refreshCars() {
  try {
    carList.value = await listAgv()
    lastUpdate.value = new Date()
    error.value = ''
  } catch (e) {
    error.value = e.message
  }
}

function formatTime(d) {
  const p = (n) => String(n).padStart(2, '0')
  return p(d.getHours()) + ':' + p(d.getMinutes()) + ':' + p(d.getSeconds())
}

onMounted(async () => {
  try {
    mapData.value = await getCurrentMap()
    await refreshCars()
    timer = setInterval(refreshCars, 1000)
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
})

onBeforeUnmount(() => {
  if (timer) clearInterval(timer)
})
</script>

<style scoped>
.app {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background: #141c28;
}

.topbar {
  height: 58px;
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  background: #1a2433;
  border-bottom: 1px solid #2c3a4f;
}
.title {
  display: flex;
  align-items: baseline;
  gap: 12px;
}
.title-main {
  font-size: 18px;
  font-weight: 800;
  color: #38e1c6;
  letter-spacing: 1px;
}
.title-sub {
  font-size: 13px;
  color: #9fb0c6;
}
.meta {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 12px;
  color: #9fb0c6;
}
.poll-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: #f87171;
}
.poll-dot.on {
  background: #34d399;
  box-shadow: 0 0 0 3px rgba(52, 211, 153, 0.15);
}

.content {
  flex: 1;
  display: flex;
  min-height: 0;
}
.map-wrap {
  flex: 1;
  position: relative;
  min-width: 0;
}
.hint {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #9fb0c6;
  font-size: 14px;
}
.hint.error { color: #f87171; }

.side {
  width: 300px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  min-height: 0;
  background: #1d2939;
  border-left: 1px solid #2c3a4f;
  overflow-y: auto;
}
</style>
