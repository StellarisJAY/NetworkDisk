<template>
    <div>
        <el-main style="background-color:#EBEEF5;height:100vh">
            <el-col :span="9"><p></p></el-col>
            <el-col :span="6">
                <el-card class="box-card">
                    <el-form label-width="80px" label-position="left">
                        <el-form-item label="用户名">
                            <el-input placeholder="请输入用户名" v-model="username"></el-input>
                        </el-form-item>
                        <el-form-item label="密码">
                            <el-input placeholder="请输入密码" v-model="password" show-password></el-input>
                        </el-form-item>
                        <el-form-item label="服务类型">
                            <el-select v-model="serviceType" placeholder="请选择">
                                <el-option label="公用服务" value="1"></el-option>
                                <el-option label="私有服务" value="2"></el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="服务主机" v-if="serviceType==2">
                            <el-input placeholder="请输入服务器主机ip" v-model="hostIp"></el-input>
                            <el-input placeholder="请输入服务端口" v-model="hostPort"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="login">登录</el-button>
                            <el-button type="info" v-if="serviceType==1" onclick="window.location='/register'">注册</el-button>
                            <el-button type="info" v-if="serviceType==2" @click="testConnection">测试连接</el-button>
                        </el-form-item>
                     </el-form>
                </el-card>
            </el-col>
        </el-main>
    </div>
</template>

<script>
import NavbarTop from '../NavbarTop.vue'
import Config from '../../settings'
import request from '../../utils/request'
import TokenUtil from '../../utils/tokenUtil'
import Axios from 'axios'
export default {
    name: 'LoginForm',
    components: { 
        NavbarTop,
    },
    data(){
        return {
            username: 'jay',
            password: '991228',
            hostIp: 'localhost',
            hostPort: 8001,
            serviceType: null
        }
    },
    methods:{
        login(){
            let that = this
            TokenUtil.removeToken()
            if(this.serviceType == 2) {
                Config.baseUrl = "http://" + this.hostIp + ":" + this.hostPort
            }
            else{
                Config.baseUrl = "http://localhost:8001"
            }
            request.defaults.baseURL = Config.baseUrl
            request.post("/login/" + this.username + "/" + this.password)
            .then((response)=>{
                if(response == "" || response==null){
                    that.message("登录失败，请检查用户名和密码", "error")
                }
                else{
                    TokenUtil.setToken(response)
                    window.location = "/files"
                }
            }).catch((error)=>{
                that.message("登录出错", "error")
            })
        },
        testConnection(){
            let that = this
            Config.baseUrl = "http://" + this.hostIp + ":" + this.hostPort
            request.defaults.baseURL = Config.baseUrl
            request.get("/connection")
            .then((response)=>{
                that.message(response==true?"连接成功":"连接无效", response==true?"success":"error")
            })
            .catch((error)=>{
                console.log(error)
                that.message("连接失败", "error")
            })
        },
        message(message, type){
            this.$message({
                message: message,
                type: type,
                duration: 2000
            })
        }
    }
}
</script>

<style>

</style>