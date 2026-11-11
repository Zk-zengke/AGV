<template>
  <section class="algo-card">
    <h3 class="algo-title">调度算法</h3>
    <div class="algo-btns">
      <button
        class="algo-btn"
        :class="{ active: mode === 'spatial' }"
        @click="switchMode('spatial')"
      >
        A* 空间避让
      </button>
      <button
        class="algo-btn"
        :class="{ active: mode === 'timewindow' }"
        @click="switchMode('timewindow')"
      >
        时间窗避障
      </button>
    </div>
    <p class="algo-desc">{{ mode === 'timewindow' ? '按时间窗预约，冲突时原地等待' : '按空间占点，冲突时绕行避让' }}</p>
    <p class="algo-error" v-if="error">{{ error }}</p>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAlgorithmMode, setAlgorithmMode } from '../api/algorithm'

const mode = ref('spatial')
const error = ref('')

onMounted(async () => {
  try {
    const data = await getAlgorithmMode()
    mode.value = data.mode
  } catch (e) {
    error.value = e.message
  }
})

async function switchMode(next) {
  if (next === mode.value) return
  error.value = ''
  try {
    const data = await setAlgorithmMode(next)
    mode.value = data.mode
  } catch (e) {
    error.value = e.message
  }
}
</script>

<style scoped>
.algo-card {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin: 14px 14px 0;
  padding: 12px;
  background: #172231;
  border: 1px solid #2a3850;
  border-radius: 6px;
}
.algo-title {
  margin: 0;
  font-size: 13px;
  font-weight: 700;
  color: #cdd9e8;
  letter-spacing: 0.5px;
}
.algo-btns {
  display: flex;
  gap: 8px;
}
.algo-btn {
  flex: 1;
  padding: 8px 6px;
  font-size: 12px;
  color: #9fb0c6;
  background: #1a2636;
  border: 1px solid #2c3a4f;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.15s ease;
}
.algo-btn:hover {
  border-color: #38e1c6;
  color: #cdd9e8;
}
.algo-btn.active {
  color: #0f1825;
  background: #38e1c6;
  border-color: #38e1c6;
  font-weight: 700;
}
.algo-desc {
  margin: 0;
  font-size: 11px;
  color: #7d8fa8;
}
.algo-error {
  margin: 0;
  font-size: 11px;
  color: #f87171;
}
</style>
