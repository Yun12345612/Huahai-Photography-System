import axios from 'axios'
import router from '../router/index'
import toolUtil from '@/utils/toolUtil'
import config from '@/utils/config'
import { ElMessage } from 'element-plus'

const http = axios.create({
    timeout: 1000 * 86400,
    withCredentials: true,
    baseURL: process.env.VUE_APP_BASE_API,
    headers: {
        'Content-Type': 'application/json; charset=utf-8'
    }
})
// 请求拦截
http.interceptors.request.use(config => {
    config.headers['Token'] = toolUtil.storageGet('frontToken') // 请求头带上token
    return config
}, error => {
    return Promise.reject(error)
})
function responseError(response) {
    const error = new Error(response?.data?.msg || "请求失败，请稍后重试")
    error.response = response
    error.data = response?.data
    return error
}
// 响应拦截
http.interceptors.response.use(response => {
    if (response.data && response.data.code == 401) { // 401, token失效
		toolUtil.storageClear()
		toolUtil.storageSet('toPath',window.history.state.current)
		ElMessage.error({
            message:response.data.msg,
            grouping:true,
            repeatNum:-99,
        })
        setTimeout(()=>{
            router.push('/login')
        },100)
		return Promise.reject(responseError(response))
    }
	else if(response.data && response.data.code == 0){
		return response
	}else{
		ElMessage.error({
            message:response.data.msg,
            grouping:true,
            repeatNum:-99,
        })
		return Promise.reject(responseError(response))
	}
    
}, error => {
    return Promise.reject(error)
})
export default http