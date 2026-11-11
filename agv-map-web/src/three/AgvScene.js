import * as THREE from 'three'
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js'

// Three.js 场景：负责渲染器、相机、灯光、轨道控制与渲染循环
export class AgvScene {
  constructor(container) {
    this.container = container
    const w = container.clientWidth
    const h = container.clientHeight

    this.scene = new THREE.Scene()
    this.scene.background = new THREE.Color('#141c28')

    this.camera = new THREE.PerspectiveCamera(50, w / h, 0.1, 1000)
    this.camera.position.set(17, 21, 23)

    this.renderer = new THREE.WebGLRenderer({ antialias: true })
    this.renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2))
    this.renderer.setSize(w, h)
    container.appendChild(this.renderer.domElement)

    // 克制的灯光：环境光 + 单方向光，不做发光效果
    this.scene.add(new THREE.AmbientLight(0xffffff, 0.78))
    const dir = new THREE.DirectionalLight(0xffffff, 0.55)
    dir.position.set(12, 22, 10)
    this.scene.add(dir)

    this.controls = new OrbitControls(this.camera, this.renderer.domElement)
    this.controls.enableDamping = true
    this.controls.dampingFactor = 0.08
    this.controls.target.set(0, 0, 0)

    this.mapGroup = new THREE.Group()
    this.carGroup = new THREE.Group()
    this.scene.add(this.mapGroup)
    this.scene.add(this.carGroup)

    this._raf = null
    this._resize = this._onResize.bind(this)
    window.addEventListener('resize', this._resize)
  }

  start() {
    const loop = () => {
      this._raf = requestAnimationFrame(loop)
      this.controls.update()
      this.renderer.render(this.scene, this.camera)
    }
    loop()
  }

  _onResize() {
    const w = this.container.clientWidth
    const h = this.container.clientHeight
    this.camera.aspect = w / h
    this.camera.updateProjectionMatrix()
    this.renderer.setSize(w, h)
  }

  clearGroup(group) {
    while (group.children.length) {
      const obj = group.children.pop()
      obj.traverse((c) => {
        if (c.geometry) c.geometry.dispose()
        if (c.material) {
          const mats = Array.isArray(c.material) ? c.material : [c.material]
          mats.forEach((m) => m.dispose())
        }
      })
    }
  }

  dispose() {
    cancelAnimationFrame(this._raf)
    window.removeEventListener('resize', this._resize)
    this.clearGroup(this.mapGroup)
    this.clearGroup(this.carGroup)
    this.controls.dispose()
    this.renderer.dispose()
    if (this.renderer.domElement.parentElement === this.container) {
      this.container.removeChild(this.renderer.domElement)
    }
  }
}
