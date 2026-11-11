import { post } from './request'

// 获取所有 AGV 小车当前状态（后端为 POST 接口）
export function listAgv() {
  return post('/agv/list')
}
