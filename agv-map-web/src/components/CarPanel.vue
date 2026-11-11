<template>
  <aside class="car-panel">
    <section class="block">
      <h3 class="block-title">图例</h3>
      <ul class="legend">
        <li><span class="dot obs"></span>障碍物</li>
        <li><span class="dot charge"></span>充电点</li>
        <li><span class="dot station"></span>工位</li>
        <li><span class="dot run"></span>运行</li>
        <li><span class="dot wait"></span>等待</li>
        <li><span class="dot idle"></span>空闲</li>
      </ul>
    </section>

    <section class="block grow">
      <h3 class="block-title">车辆状态（{{ carList.length }} 辆）</h3>
      <ul class="car-list">
        <li v-for="car in carList" :key="car.agvCode" class="car-card">
          <div class="car-head">
            <span class="car-code">{{ car.agvCode }}</span>
            <span class="status" :class="car.status">{{ statusText(car.status) }}</span>
          </div>
          <div class="car-row">
            <span>坐标</span>
            <span class="mono">({{ car.x }}, {{ car.y }})</span>
          </div>
          <div class="car-row">
            <span>朝向</span>
            <span class="mono">{{ car.direction }}°</span>
          </div>
          <div class="car-row">
            <span>任务</span>
            <span class="mono">{{ car.taskId === null ? '无' : '#' + car.taskId }}</span>
          </div>
          <div class="battery">
            <div class="battery-meta">
              <span>电量</span>
              <span class="mono">{{ car.battery.toFixed(1) }}%</span>
            </div>
            <div class="bar">
              <div
                class="bar-fill"
                :class="{ low: car.battery < 30 }"
                :style="{ width: car.battery + '%' }"
              ></div>
            </div>
          </div>
        </li>
      </ul>
    </section>
  </aside>
</template>

<script setup>
defineProps({
  carList: { type: Array, default: () => [] },
})

const STATUS_TEXT = {
  idle: '空闲',
  running: '运行中',
  charging: '充电中',
  stopping: '已停止',
  waiting: '等待中',
}

function statusText(status) {
  return STATUS_TEXT[status] || status
}
</script>

<style scoped>
.car-panel {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 14px;
}

.block {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.block.grow {
  flex: 1;
}
.block-title {
  margin: 0;
  font-size: 13px;
  font-weight: 700;
  color: #cdd9e8;
  letter-spacing: 0.5px;
}

.legend {
  list-style: none;
  margin: 0;
  padding: 0;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 8px 10px;
  font-size: 12px;
  color: #9fb0c6;
}
.dot {
  display: inline-block;
  width: 10px;
  height: 10px;
  border-radius: 2px;
  margin-right: 6px;
  vertical-align: middle;
}
.dot.obs { background: #4d5d75; }
.dot.charge { background: #fbbf24; border-radius: 50%; }
.dot.station { background: #3b82f6; border-radius: 50%; }
.dot.run { background: #34d399; border-radius: 50%; }
.dot.wait { background: #facc15; border-radius: 50%; }
.dot.idle { background: #93a4bb; border-radius: 50%; }

.car-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.car-card {
  padding: 10px;
  background: #172231;
  border: 1px solid #2a3850;
  border-radius: 6px;
}
.car-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}
.car-code {
  font-size: 13px;
  font-weight: 700;
  color: #e5ebf3;
}
.status {
  font-size: 11px;
  padding: 2px 8px;
  border-radius: 10px;
  font-weight: 600;
}
.status.idle { color: #93a4bb; background: rgba(147, 164, 187, 0.14); }
.status.running { color: #34d399; background: rgba(52, 211, 153, 0.14); }
.status.charging { color: #fbbf24; background: rgba(251, 191, 36, 0.14); }
.status.stopping { color: #f87171; background: rgba(248, 113, 113, 0.14); }
.status.waiting { color: #facc15; background: rgba(250, 204, 21, 0.14); }

.car-row {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #9fb0c6;
  margin-bottom: 4px;
}
.mono {
  font-family: Consolas, 'JetBrains Mono', monospace;
  color: #cdd9e8;
}

.battery { margin-top: 6px; }
.battery-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #9fb0c6;
  margin-bottom: 4px;
}
.bar {
  height: 6px;
  background: #0f1825;
  border-radius: 3px;
  overflow: hidden;
}
.bar-fill {
  height: 100%;
  background: #34d399;
  border-radius: 3px;
  transition: width 0.6s ease;
}
.bar-fill.low { background: #f87171; }
</style>
