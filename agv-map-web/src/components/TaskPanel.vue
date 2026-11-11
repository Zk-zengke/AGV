<template>
  <section class="task-panel">
    <h3 class="block-title">任务下发</h3>

    <label class="field">
      <span>车辆</span>
      <select v-model="selectedCar">
        <option value="" disabled>请选择车辆</option>
        <option v-for="car in carList" :key="car.agvCode" :value="car.agvCode">
          {{ car.agvCode }}（{{ statusText(car.status) }}）
        </option>
      </select>
    </label>

    <label class="field">
      <span>起点工位</span>
      <select v-model="selectedStart">
        <option value="" disabled>请选择起点</option>
        <option v-for="s in stations" :key="s.id" :value="s.id">
          {{ s.id }}（{{ s.type === 'charge' ? '充电点' : '工位' }}）
        </option>
      </select>
    </label>

    <label class="field">
      <span>终点工位</span>
      <select v-model="selectedEnd">
        <option value="" disabled>请选择终点</option>
        <option v-for="s in stations" :key="s.id" :value="s.id">
          {{ s.id }}（{{ s.type === 'charge' ? '充电点' : '工位' }}）
        </option>
      </select>
    </label>

    <div class="actions">
      <button class="btn primary" :disabled="submitting" @click="submit">
        {{ submitting ? '下发中…' : '下发任务' }}
      </button>
      <button class="btn" :disabled="!activeTask" @click="cancel">取消任务</button>
    </div>

    <p v-if="message" class="message" :class="{ error: isError }">{{ message }}</p>
  </section>
</template>

<script setup>
import { ref } from 'vue'
import { dispatchTask, cancelTask } from '../api/task'

const props = defineProps({
  carList: { type: Array, default: () => [] },
  stations: { type: Array, default: () => [] },
})
const emit = defineEmits(['dispatched', 'cancelled'])

const selectedCar = ref('')
const selectedStart = ref('')
const selectedEnd = ref('')
const submitting = ref(false)
const message = ref('')
const isError = ref(false)
const activeTask = ref(null)

const STATUS_TEXT = { idle: '空闲', running: '运行中', charging: '充电中', stopping: '已停止' }
function statusText(s) {
  return STATUS_TEXT[s] || s
}

function notify(text, error) {
  message.value = text
  isError.value = !!error
}

async function submit() {
  const car = props.carList.find((c) => c.agvCode === selectedCar.value)
  if (!car) return notify('请选择车辆', true)
  if (!selectedStart.value || !selectedEnd.value) return notify('请选择起点和终点', true)
  if (selectedStart.value === selectedEnd.value) return notify('起点和终点不能相同', true)

  const start = props.stations.find((s) => s.id === selectedStart.value)
  const end = props.stations.find((s) => s.id === selectedEnd.value)
  const taskCode = 'T' + Date.now()
  const payload = {
    taskCode,
    agvCode: car.agvCode,
    currentPoint: { id: 'current', x: car.x, y: car.y },
    startPoint: { id: start.id, x: start.x, y: start.y },
    endPoint: { id: end.id, x: end.x, y: end.y },
  }

  submitting.value = true
  try {
    const nodes = await dispatchTask(payload)
    activeTask.value = { taskCode, agvCode: car.agvCode }
    notify('任务已下发，规划路径共 ' + nodes.length + ' 个节点', false)
    emit('dispatched', car.agvCode)
  } catch (e) {
    notify('下发失败：' + e.message, true)
  } finally {
    submitting.value = false
  }
}

async function cancel() {
  if (!activeTask.value) return
  const car = props.carList.find((c) => c.agvCode === activeTask.value.agvCode)
  if (!car || car.taskId === null) return notify('当前没有可取消的任务', true)
  try {
    const code = activeTask.value.agvCode
    await cancelTask(car.taskId)
    activeTask.value = null
    notify('任务已取消', false)
    emit('cancelled', code)
  } catch (e) {
    notify('取消失败：' + e.message, true)
  }
}
</script>

<style scoped>
.task-panel {
  padding: 14px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  border-bottom: 1px solid #2c3a4f;
}
.block-title {
  margin: 0;
  font-size: 13px;
  font-weight: 700;
  color: #cdd9e8;
}
.field {
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-size: 12px;
  color: #9fb0c6;
}
.field select {
  height: 32px;
  padding: 0 8px;
  background: #172231;
  color: #e5ebf3;
  border: 1px solid #2a3850;
  border-radius: 4px;
  font-family: inherit;
  font-size: 12px;
}
.field select:focus {
  outline: none;
  border-color: #38e1c6;
}
.actions {
  display: flex;
  gap: 8px;
  margin-top: 2px;
}
.btn {
  flex: 1;
  height: 34px;
  border: 1px solid #2a3850;
  background: #172231;
  color: #cdd9e8;
  border-radius: 4px;
  font-size: 13px;
  cursor: pointer;
}
.btn.primary {
  background: rgba(56, 225, 198, 0.16);
  border-color: #38e1c6;
  color: #38e1c6;
}
.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
.message {
  margin: 0;
  font-size: 12px;
  color: #34d399;
}
.message.error {
  color: #f87171;
}
</style>
