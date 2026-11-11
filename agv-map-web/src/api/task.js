import { get, post } from './request'

// 下发运输任务，返回规划路径节点
export function dispatchTask(payload) {
  return post('/agv/path', payload)
}

// 取消指定任务
export function cancelTask(taskId) {
  return get('/task/cancel/' + taskId)
}
