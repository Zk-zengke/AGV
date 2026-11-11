import * as THREE from 'three'
import { worldToGrid, gridToWorld, buildObstacles } from './buildMap'

// 地图障碍物编辑器：编辑模式下点击网格切换障碍物，含 hover 预览与工位保护
export class MapEditor {
  constructor(agvScene, mapData, obstacleGroup, onChange) {
    this.agvScene = agvScene
    this.scene = agvScene.scene
    this.camera = agvScene.camera
    this.controls = agvScene.controls
    this.dom = agvScene.renderer.domElement
    this.mapData = mapData
    this.obstacleGroup = obstacleGroup
    this.onChange = onChange

    // 草稿：深拷贝当前障碍物，编辑期间不改动原始地图数据
    this.draft = (mapData.obstacles || []).map((o) => ({ x: o.x, y: o.y }))
    this.stationKeys = new Set(
      (mapData.stations || []).map((s) => s.x + ',' + s.y)
    )

    this.raycaster = new THREE.Raycaster()
    this.pointer = new THREE.Vector2()
    this.groundPlane = new THREE.Plane(new THREE.Vector3(0, 1, 0), 0)

    // hover 预览方块
    this.preview = new THREE.Mesh(
      new THREE.BoxGeometry(0.96, 0.9, 0.96),
      new THREE.MeshBasicMaterial({ transparent: true, opacity: 0.45 })
    )
    this.preview.visible = false

    this.active = false
    this._move = this.onPointerMove.bind(this)
    this._click = this.onPointerClick.bind(this)
  }

  start() {
    this.active = true
    this.controls.enabled = false // 关闭轨道旋转，避免与编辑冲突
    this.scene.add(this.preview)
    this.dom.addEventListener('pointermove', this._move)
    this.dom.addEventListener('click', this._click)
    this.dom.style.cursor = 'crosshair'
    this.renderDraft()
  }

  stop() {
    this.active = false
    this.controls.enabled = true
    this.scene.remove(this.preview)
    this.preview.visible = false
    this.dom.removeEventListener('pointermove', this._move)
    this.dom.removeEventListener('click', this._click)
    this.dom.style.cursor = ''
  }

  // 鼠标位置 Raycast 到 y=0 平面，返回对齐后的网格坐标
  pickGrid(event) {
    const rect = this.dom.getBoundingClientRect()
    this.pointer.x = ((event.clientX - rect.left) / rect.width) * 2 - 1
    this.pointer.y = -((event.clientY - rect.top) / rect.height) * 2 + 1
    this.raycaster.setFromCamera(this.pointer, this.camera)

    const point = new THREE.Vector3()
    if (!this.raycaster.ray.intersectPlane(this.groundPlane, point)) return null
    const g = worldToGrid(point.x, point.z, this.mapData.width, this.mapData.height)
    if (g.x < 0 || g.y < 0 || g.x >= this.mapData.width || g.y >= this.mapData.height) {
      return null
    }
    return g
  }

  hasObstacle(x, y) {
    return this.draft.some((o) => o.x === x && o.y === y)
  }

  onPointerMove(event) {
    const g = this.pickGrid(event)
    if (!g || this.stationKeys.has(g.x + ',' + g.y)) {
      this.preview.visible = false
      return
    }
    const w = gridToWorld(g.x, g.y, this.mapData.width, this.mapData.height)
    this.preview.position.set(w.x, 0.45, w.z)
    this.preview.visible = true
    // 已有障碍显示红色（点击将删除），空格显示青色（点击将添加）
    this.preview.material.color.set(this.hasObstacle(g.x, g.y) ? '#f87171' : '#38e1c6')
  }

  onPointerClick(event) {
    const g = this.pickGrid(event)
    if (!g || this.stationKeys.has(g.x + ',' + g.y)) return // 工位保护
    const idx = this.draft.findIndex((o) => o.x === g.x && o.y === g.y)
    if (idx >= 0) {
      this.draft.splice(idx, 1)
    } else {
      this.draft.push({ x: g.x, y: g.y })
    }
    this.renderDraft()
    if (this.onChange) this.onChange(this.draft)
  }

  renderDraft() {
    while (this.obstacleGroup.children.length) {
      const obj = this.obstacleGroup.children.pop()
      obj.traverse((c) => {
        if (c.geometry) c.geometry.dispose()
        if (c.material) c.material.dispose()
      })
    }
    this.obstacleGroup.add(buildObstacles(this.draft, this.mapData))
  }
}
