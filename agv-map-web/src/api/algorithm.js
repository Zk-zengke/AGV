import { get, post } from './request'

// 获取当前调度算法模式：spatial 或 timewindow
export function getAlgorithmMode() {
  return get('/algorithm/mode')
}

// 切换调度算法模式
export function setAlgorithmMode(mode) {
  return post('/algorithm/mode/' + mode)
}
