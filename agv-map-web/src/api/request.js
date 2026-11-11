// 统一请求封装：所有接口走 Vite 代理的 /api 前缀，返回业务 data
const BASE = '/api'

async function request(path, options = {}) {
  const res = await fetch(BASE + path, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  })
  if (!res.ok) {
    throw new Error('HTTP ' + res.status + ' ' + path)
  }
  const body = await res.json()
  if (body.code !== 200) {
    throw new Error(body.msg || '请求失败')
  }
  return body.data
}

export function get(path) {
  return request(path, { method: 'GET' })
}

export function post(path, payload) {
  return request(path, {
    method: 'POST',
    body: payload === undefined ? undefined : JSON.stringify(payload),
  })
}
