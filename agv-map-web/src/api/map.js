import { get, post } from './request'

// 获取当前生效的车间地图
export function getCurrentMap() {
  return get('/map/current')
}

// 保存（编辑后的）地图，后端生成新版本
export function saveMap(payload) {
  return post('/map/save', payload)
}
