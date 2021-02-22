<template>
    <div>
        <button type="primary" @click="getThumbnail">缩略图</button>
        <div class="image-class__error">
            <div class="block">
                <el-image>
                    <div slot="error" class="image-slot">
                        <i class="el-icon-picture-outline"></i>
                    </div>
                </el-image>
            </div>
        </div>
        
    </div>
</template>

<script>
import request from '../../utils/request'
export default {
    name: "PictureList",
    data(){
        return {
            pictures: []
        }
    },
    methods: {
        getThumbnail(){
            request.get("/picture/thumbnail/750")
            .then((response)=>{
                let url = window.URL.createObjectURL(new Blob([response]))
                console.log(response)
                // 生成一个a标签
                let link = document.createElement("a")
                link.style.display = "none"
                link.href = url

                link.download = "picture.jpg" 
                document.body.appendChild(link)
                link.click()
            })
        }
    }
}
</script>

<style>

</style>