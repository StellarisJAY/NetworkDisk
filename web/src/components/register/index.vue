<template>
    <div>
        <el-main style="background-color:#EBEEF5;height:100vh">
            <el-col :span="9"><p></p></el-col>
            <el-col :span="6">
                <el-card class="box-card">
                    <el-form label-width="80px" label-position="left">
                        <el-form-item label="用户名">
                            <el-input placeholder="6-10位英文字符或数字" v-model="username" maxlength="10" show-word-limit></el-input>
                        </el-form-item>
                        <el-form-item label="密码">
                            <el-input placeholder="6-15位英文字符或数字" v-model="password1" show-password maxlength="15"></el-input>
                        </el-form-item>
                        <el-form-item label="确认密码">
                            <el-input placeholder="重复上面密码" v-model="password2" show-password maxlength="15"></el-input>
                        </el-form-item>
                        <el-form-item label="邮箱">
                            <el-input v-model="email" placeholder="验证用邮箱"></el-input>
                        </el-form-item>
                        <el-form-item label="手机号">
                            <el-input v-model="phone"></el-input>
                        </el-form-item>
                        <el-form-item label="邀请码">
                            <el-input v-model="invitation" placeholder="10位数邀请码" maxlength="10"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="login">注册</el-button>
                            <el-button type="primary" onclick="window.location='/login'">返回</el-button>
                        </el-form-item>
                     </el-form>
                </el-card>
            </el-col>
        </el-main>
    </div>
</template>

<script>
import navbarTop from '../NavbarTop'
export default {
    name: "registerForm",
    components: {
        navbarTop
    },
    data(){
        return{
            username: null,
            password1: null,
            password2: null,
            email: null,
            phone: null,
            invitation: null
        }
    },
    methods:{

    }
}
</script>

<style>

</style>