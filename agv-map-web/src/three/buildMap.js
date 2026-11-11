import * as THREE from 'three'

// 网格坐标转 Three.js 世界坐标（地图整体居中于原点）
export function gridToWorld(x, y, width, height) {
  return {
    x: x - width / 2 + 0.5,
    z: y - height / 2 + 0.5,
  }
}

// 世界坐标反算网格坐标（点击拾取时使用），自动对齐到格子
export function worldToGrid(worldX, worldZ, width, height) {
  const gx = Math.round(worldX + width / 2 - 0.5)
  const gy = Math.round(worldZ + height / 2 - 0.5)
  return { x: gx, y: gy }
}

// 构建地面底板、网格线与工位
export function buildGround(mapData) {
  const group = new THREE.Group()
  const width = mapData.width
  const height = mapData.height
  const stations = mapData.stations || []

  // 地面底板
  const ground = new THREE.Mesh(
    new THREE.BoxGeometry(width, 0.2, height),
    new THREE.MeshStandardMaterial({ color: '#1b2635', roughness: 0.95, metalness: 0.05 })
  )
  ground.position.y = -0.1
  ground.userData = { type: 'ground' }
  group.add(ground)

  // 网格线
  const grid = new THREE.GridHelper(
    Math.max(width, height),
    Math.max(width, height),
    '#3a4d66',
    '#263448'
  )
  grid.position.y = 0.012
  group.add(grid)

  // 工位：充电点琥珀色，普通工位蓝色，用发光圆柱标识
  stations.forEach((s) => {
    const p = gridToWorld(s.x, s.y, width, height)
    const color = s.type === 'charge' ? '#fbbf24' : '#3b82f6'
    const marker = new THREE.Mesh(
      new THREE.CylinderGeometry(0.34, 0.34, 0.18, 24),
      new THREE.MeshStandardMaterial({
        color,
        emissive: color,
        emissiveIntensity: 0.28,
        roughness: 0.5,
      })
    )
    marker.position.set(p.x, 0.09, p.z)
    marker.userData = { type: 'station', x: s.x, y: s.y }
    group.add(marker)
  })

  return group
}

// 构建障碍物立方体（深灰蓝），每个立方体记录网格坐标供拾取识别
export function buildObstacles(obstacles, mapData) {
  const group = new THREE.Group()
  const width = mapData.width
  const height = mapData.height
  const obsMat = new THREE.MeshStandardMaterial({ color: '#4d5d75', roughness: 0.85 })
  const seen = new Set()

  obstacles.forEach((o) => {
    const key = o.x + ',' + o.y
    if (seen.has(key)) return
    seen.add(key)
    const p = gridToWorld(o.x, o.y, width, height)
    const box = new THREE.Mesh(new THREE.BoxGeometry(0.96, 0.9, 0.96), obsMat)
    box.position.set(p.x, 0.45, p.z)
    box.userData = { type: 'obstacle', x: o.x, y: o.y }
    group.add(box)
  })

  return group
}

// 根据规划路径节点构建路径线，并标记起点与终点；lineColor 区分不同车辆
export function buildPath(nodes, mapData, lineColor = '#38e1c6') {
  const group = new THREE.Group()

  // 去除连续重复的节点
  const clean = []
  nodes.forEach((n) => {
    const last = clean[clean.length - 1]
    if (!last || last.x !== n.x || last.y !== n.y) clean.push(n)
  })

  const points = clean.map((n) => {
    const p = gridToWorld(n.x, n.y, mapData.width, mapData.height)
    return new THREE.Vector3(p.x, 0.14, p.z)
  })

  if (points.length >= 2) {
    const line = new THREE.Line(
      new THREE.BufferGeometry().setFromPoints(points),
      new THREE.LineBasicMaterial({ color: lineColor })
    )
    group.add(line)
  }

  // 起点绿色、终点琥珀色，用发光小球标记
  const ends = [
    { p: points[0], color: '#34d399' },
    { p: points[points.length - 1], color: '#fbbf24' },
  ]
  ends.forEach((e) => {
    const ball = new THREE.Mesh(
      new THREE.SphereGeometry(0.2, 16, 12),
      new THREE.MeshStandardMaterial({
        color: e.color,
        emissive: e.color,
        emissiveIntensity: 0.5,
      })
    )
    ball.position.set(e.p.x, 0.2, e.p.z)
    group.add(ball)
  })

  return group
}
