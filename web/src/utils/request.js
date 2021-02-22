import axios from 'axios'
import TokenUtil from './tokenUtil'
import Config from '../settings'

// 设置axios的baseUrl和请求超时时间
var service = axios.create({
    baseURL: Config.baseUrl,
    timeout: Config.timeout
})

// 设置请求拦截器，在请求中自动添加token
service.interceptors.request.use((config)=>{
    if(TokenUtil.getToken()){
        config.headers.token = TokenUtil.getToken()
    }
    return config
},
(error)=>{
    Promise.reject(error)
})

service.interceptors.response.use(
    response=>{
        return response.data
    },
    error=>{
        console.log(error)
        if(error.response.status == 401){
            alert("请重新登录")
            window.location = "/login"
        }
    }
)
export default service


