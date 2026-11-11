<template>
  <div ref="containerRef" class="map-container">
    <div class="edit-toolbar">
      <template v-if="!editing">
        <button class="tb-btn" @click="startEdit">编辑障碍物</button>
      </template>
      <template v-else>
        <span class="edit-tip">编辑中：点击空格添加、点击障碍删除 · 草稿 {{ draftCount }} 个</span>
        <button class="tb-btn primary" :disabled="saving" @click="save">
          {{ saving ? '保存中…' : '保存' }}
        </button>
        <button class="tb-btn" :disabled="saving" @click="discard">放弃</button>
      </template>
    </div>
    <p v-if="editError" class="edit-error">{{ editError }}</p>
  </div>
</template>

<script setup>
import { onMounted, onBeforeUnmount, ref, watch } from 'vue'
import * as THREE from 'three'
import { AgvScene } from '../three/AgvScene'
import { buildGround, buildObstacles, buildPath } from '../three/buildMap'
import { CarManager } from '../three/CarManager'
import { MapEditor } from '../three/MapEditor'
import { saveMap } from '../api/map'

const props = defineProps({
  mapData: { type: Object, default: null },
  carList: { type: Array, default: () => [] },
  pathNodes: { type: Array, default: () => [] },
})
const emit = defineEmits(['map-saved'])

const containerRef = ref(null)
const editing = ref(false)
const saving = ref(false)
const draftCount = ref(0)
const editError = ref('')

let scene = null
let groundGroup = null
let obstacleGroup = null
let pathGroup = null
let carManager = null
let editor = null

function renderGround() {
  scene.clearGroup(groundGroup)
  groundGroup.add(buildGround(props.mapData))
}

function renderObstacles() {
  scene.clearGroup(obstacleGroup)
  obstacleGroup.add(buildObstacles(props.mapData.obstacles || [], props.mapData))
}

function renderPath() {
  if (!pathGroup || !props.mapData) return
  scene.clearGroup(pathGroup)
  if (props.pathNodes && props.pathNodes.length) {
    props.pathNodes.forEach((p) => {
      if (p.nodes && p.nodes.length) {
        pathGroup.add(buildPath(p.nodes, props.mapData, p.color))
      }
    })
  }
}

function rebuildCarManager() {
  carManager = new CarManager(scene.carGroup, props.mapData)
  carManager.sync(props.carList)
}

function startEdit() {
  if (!props.mapData) return
  editError.value = ''
  editor = new MapEditor(scene, props.mapData, obstacleGroup, (draft) => {
    draftCount.value = draft.length
  })
  draftCount.value = editor.draft.length
  editor.start()
  editing.value = true
}

function finishEdit() {
  if (editor) {
    editor.stop()
    editor = null
  }
  editing.value = false
}

async function save() {
  if (!editor) return
  saving.value = true
  editError.value = ''
  try {
    await saveMap({
      width: props.mapData.width,
      height: props.mapData.height,
      cellSize: props.mapData.cellSize,
      description: props.mapData.description,
      obstacles: editor.draft,
      stations: props.mapData.stations || [],
    })
    finishEdit()
    emit('map-saved')
  } catch (e) {
    editError.value = '保存失败：' + e.message
  } finally {
    saving.value = false
  }
}

function discard() {
  finishEdit()
  renderObstacles() // 重新拉取当前地图的障碍物，还原草稿
}

onMounted(() => {
  scene = new AgvScene(containerRef.value)
  groundGroup = new THREE.Group()
  obstacleGroup = new THREE.Group()
  pathGroup = new THREE.Group()
  scene.scene.add(groundGroup)
  scene.scene.add(obstacleGroup)
  scene.scene.add(pathGroup)
  scene.start()
  if (props.mapData) {
    renderGround()
    renderObstacles()
    rebuildCarManager()
    renderPath()
  }
})

watch(
  () => props.mapData,
  () => {
    if (!scene || !props.mapData) return
    renderGround()
    renderObstacles()
    rebuildCarManager()
    renderPath()
  }
)

watch(
  () => props.carList,
  (list) => {
    if (carManager) carManager.sync(list)
  },
  { deep: true }
)

watch(
  () => props.pathNodes,
  () => renderPath(),
  { deep: true }
)

onBeforeUnmount(() => {
  if (editor) editor.stop()
  if (scene) scene.dispose()
  scene = null
})
</script>

<style scoped>
.map-container {
  position: relative;
  width: 100%;
  height: 100%;
  min-height: 420px;
}

.edit-toolbar {
  position: absolute;
  top: 14px;
  left: 14px;
  z-index: 5;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 12px;
  background: rgba(29, 41, 57, 0.92);
  border: 1px solid #2c3a4f;
  border-radius: 8px;
}

.edit-tip {
  font-size: 12px;
  color: #93a4bb;
}

.tb-btn {
  padding: 6px 14px;
  font-size: 13px;
  color: #c6d2e1;
  background: #2c3a4f;
  border: 1px solid #3a4d66;
  border-radius: 6px;
  cursor: pointer;
}

.tb-btn:hover {
  border-color: #38e1c6;
}

.tb-btn.primary {
  color: #0b1118;
  background: #38e1c6;
  border-color: #38e1c6;
}

.tb-btn:disabled {
  opacity: 0.55;
  cursor: not-allowed;
}

.edit-error {
  position: absolute;
  top: 64px;
  left: 14px;
  z-index: 5;
  margin: 0;
  padding: 6px 12px;
  font-size: 12px;
  color: #f87171;
  background: rgba(29, 41, 57, 0.92);
  border: 1px solid #f87171;
  border-radius: 6px;
}
</style>
