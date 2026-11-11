import * as THREE from 'three'
import { gridToWorld } from './buildMap'

// 状态语义色
const STATUS_COLOR = {
  idle: '#93a4bb',
  running: '#34d399',
  charging: '#fbbf24',
  stopping: '#f87171',
  waiting: '#facc15',
}

// 车辆 mesh 的创建与全量同步
export class CarManager {
  constructor(group, mapData) {
    this.group = group
    this.mapData = mapData
    this.cars = new Map()
  }

  _color(status) {
    return STATUS_COLOR[status] || '#93a4bb'
  }

  _buildCar(agv) {
    const holder = new THREE.Group()

    const body = new THREE.Mesh(
      new THREE.BoxGeometry(0.7, 0.45, 0.7),
      new THREE.MeshStandardMaterial({
        color: this._color(agv.status),
        roughness: 0.55,
        metalness: 0.2,
      })
    )
    body.position.y = 0.3
    body.name = 'body'
    holder.add(body)

    // 顶部状态灯
    const lamp = new THREE.Mesh(
      new THREE.SphereGeometry(0.12, 16, 12),
      new THREE.MeshStandardMaterial({
        color: this._color(agv.status),
        emissive: this._color(agv.status),
        emissiveIntensity: 0.5,
      })
    )
    lamp.position.y = 0.62
    lamp.name = 'lamp'
    holder.add(lamp)

    this._place(holder, agv)
    return holder
  }

  _place(holder, agv) {
    const p = gridToWorld(agv.x, agv.y, this.mapData.width, this.mapData.height)
    holder.position.set(p.x, 0, p.z)
  }

  // 按接口返回的车辆列表全量同步位置、状态与数量
  sync(agvList) {
    const codes = new Set()
    agvList.forEach((agv) => {
      codes.add(agv.agvCode)
      let holder = this.cars.get(agv.agvCode)
      if (!holder) {
        holder = this._buildCar(agv)
        this.cars.set(agv.agvCode, holder)
        this.group.add(holder)
      } else {
        this._place(holder, agv)
      }
      const color = this._color(agv.status)
      holder.getObjectByName('body').material.color.set(color)
      const lamp = holder.getObjectByName('lamp')
      lamp.material.color.set(color)
      lamp.material.emissive.set(color)
    })

    // 移除接口中已不存在的车辆
    Array.from(this.cars.keys()).forEach((code) => {
      if (codes.has(code)) return
      const holder = this.cars.get(code)
      this.group.remove(holder)
      holder.traverse((c) => {
        if (c.geometry) c.geometry.dispose()
        if (c.material) c.material.dispose()
      })
      this.cars.delete(code)
    })
  }
}
