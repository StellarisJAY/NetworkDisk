<template>
    <el-col :span="20">
        <el-header style="padding-top:10px" class="toolbar">
            <el-col :span="2">
                <el-button type="primary" icon="el-icon-upload" @click="uploadFormVisible=true">上传</el-button>
            </el-col>
            <el-col :span="6">
                <el-button type="primary" icon="el-icon-folder-add" @click="openCreateFolder"></el-button>
            </el-col>
            <el-col :span="8">
                <br v-if="selected==false">
                <el-button type="primary" icon="el-icon-download" v-if="selected==true" @click="downloadSelected">下载</el-button>
                <el-button type="primary" icon="el-icon-delete" v-if="selected==true" @click="deleteSelected">删除</el-button>
            </el-col>
            <el-col :span="8">
                <el-input placeholder="搜索我的文件" v-model="searchInput" @keyup.enter.native="search" maxlength="20">
                    <el-button type="primary" slot="append" icon="el-icon-search" @click="search"></el-button>
                </el-input>
            </el-col>
        </el-header>
        <el-breadcrumb separator="/" style="padding-left:20px">
            <el-col :span="20">
                <el-breadcrumb-item><a role="link" @click="back()" v-if="currentPath!=1">返回</a></el-breadcrumb-item>
                <el-breadcrumb-item ><a role="link" @click="home()">全部文件</a></el-breadcrumb-item>
                <el-breadcrumb-item v-for="path in pathStack" :key="path.fileId"><a>{{path.name}}</a></el-breadcrumb-item>
            </el-col>
            <el-col :span="4">
                
            </el-col>
        </el-breadcrumb>
        <el-main>
            <el-table :data="files" height="500px" @selection-change="handleSelectionChange" v-if="displayMode=='list'" v-loading="loading" element-loading-text="加载中...">
                <el-table-column type="selection"></el-table-column>
                <el-table-column prop="filename" label="文件名" width="600em" fixed sortable>
                    <template slot-scope="scope">
                        <el-avatar shape="square" style="background-color:white" :size="20" :src="getFileIcon(scope.row.type)"></el-avatar>
                        <a role="link" v-if="scope.row.directory==1" @click="enterFolder(scope.row.fileId, scope.row.filename)">{{scope.row.filename}}</a>
                        <a v-else>{{scope.row.filename}}</a>
                    </template>
                </el-table-column>
                <el-table-column prop="size" label="大小" width="100em" fixed sortable>
                    <template slot-scope="scope">
                        {{displaySize(scope.row)}}
                    </template>
                </el-table-column>
                <el-table-column prop="updateDate" label="修改日期" fixed sortable></el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button type="info" size="mini" icon="el-icon-download" @click="download(scope.row)"></el-button>
                        <el-button type="warning" size="mini" icon="el-icon-delete" @click="deleteFile(scope.row.fileId, scope.row.directory, scope.row.size)"></el-button>
                        <el-button type="primary" size="mini" icon="el-icon-share"></el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-main>
        <el-dialog title="上传文件列表" :visible.sync="uploadFormVisible">
            <el-upload
                ref="upload"
                :action="baseUrl + '/file/upload/' + currentPath"
                multiple
                :file-list="uploadList"
                :auto-upload="false"
                :headers="uploadHeaders"
                :beforeUpload="beforeUpload"
                :on-success="onUploadSuccess">
                <el-button slot="trigger" size="small" type="primary">选择文件</el-button>
                <el-button size="small" type="warning" @click="$refs.upload.submit()">上传</el-button>
                <el-button size="small" type="danger" @click="uploadList = []">清空</el-button>
            </el-upload>
            
        </el-dialog>
    </el-col>
</template>

<script>
import request from '../../utils/request'
import Config from '../../settings'
import tokenUtil from '../../utils/tokenUtil'
import SidebarVue from '../Sidebar.vue'
import eventBus from '../../utils/eventBus'
export default {
    name: "FileArea",
    data(){
        return{
            selected: false,
            files: [],
            currentPath: 1,
            pathStack: [],
            selections: [],
            displayMode: 'list',
            searchInput: '',
            uploadFormVisible: false,
            uploadList: [],
            uploadHeaders: {
                token: tokenUtil.getToken()
            },
            uploadFinished: 0,
            baseUrl: Config.baseUrl,
        }
    },
    mounted(){
        this.getFilesAtCurrentPath()
    },
    methods: {
        getFilesAtCurrentPath(){
            let that = this
            request.get("/file/dir/"+this.currentPath)
            .then((data)=>{
                that.files = data
            })
        },
        getFileIcon(type){
            return Config.getIconUrl(type)
        },
        /**
         * 进入文件夹
         */
        enterFolder(fileId, foldername){
            this.pathStack.push({fileId: fileId, name: foldername})
            this.currentPath = fileId
            this.getFilesAtCurrentPath()
        },
        /**
         * 返回根目录
         */
        home(){
            this.currentPath = 1
            this.pathStack = []
            this.getFilesAtCurrentPath()
        },
        /**
         * 返回上级目录
         */
        back(){
            this.pathStack.pop()
            if(this.pathStack.length == 0){
                this.currentPath = 1
            }
            else{
                this.currentPath = this.pathStack[this.pathStack.length - 1].fileId
            }
            if(this.currentPath == 0){
                this.search()
            }
            else{
                this.getFilesAtCurrentPath()
            }
        },

        /**
         * 上传前检查
         */
        beforeUpload(file){
            let usedSpace = localStorage.getItem("usedSpace")
            let maxSpace = localStorage.getItem("maxSpace")
            if(file.size > maxSpace - usedSpace || file.size > 250 * 1024 * 1024){
                this.message("上传文件过大", "warning")
                return Promise.reject()
            }
            for(let i=0; i<this.files.length; i++){
                if(file.name==this.files[i].filename + '.' + this.files[i].type){
                    this.message("上传文件重名", "warning")
                    return Promise.reject()
                }
            }
        },
        /**
         * 上传成功提示
         */
        onUploadSuccess(response, file, fileList){
            eventBus.$emit("refreshUsedSpace", file.size)
            this.uploadFinished += 1
            if(this.uploadFinished == fileList.length){
                this.notifiy(response.message, null, response.data.status==true?"success":"error")
                this.getFilesAtCurrentPath()
                this.uploadFinished = 0
            }
        },
        /**
         * 下载选择的文件
         */
        downloadSelected(){
            let downloadList = []
            for(let i = 0; i < this.selections.length; i++){
                if(this.selections[i].directory == 0){
                    downloadList[i] = this.selections[i].fileId
                }else{
                    this.message("暂不支持下载文件夹", "warning")
                    return 0
                }
            }
            request.get("/file/download/packed", {
                params: {
                    fileIds: downloadList + ''
                },
                responseType: "blob"
            })
            .then((response)=>{
                let url = window.URL.createObjectURL(new Blob([response], { type: 'application/octet-stream' }))
                // 生成一个a标签
                let link = document.createElement("a")
                link.style.display = "none"
                link.href = url

                link.download = "[批量下载]"+this.selections[0].filename+"等.zip" 
                document.body.appendChild(link)
                link.click()

            })
        },
        /**
         * 下载，根据文件是否是文件夹调用不同的下载方法
         */
        download(file){
            if(file.directory == 0){
                this.downloadFile(file)
            }
            else{
                this.message("暂不支持下载文件夹", "warning")
            }
        },
        /**
         * 下载单个文件
         */
        downloadFile(file){
            request.get("/file/download/"+file.fileId, {
                responseType: "blob"
            })
            .then((res)=>{
                let url = window.URL.createObjectURL(new Blob([res], { type: 'application/octet-stream' }))
                // 生成一个a标签
                let link = document.createElement("a")
                link.style.display = "none"
                link.href = url

                link.download = file.filename  
                document.body.appendChild(link)
                link.click()
            })
        },
        /**
         * 删除文件或文件夹
         */
        deleteFile(fileId, directory, size){
            let that = this
            if(confirm("是否删除该文件？删除后将无法找回") == true){
                request.delete("/" + (directory==1?"folder/":"file/") +fileId)
                .then((response)=>{
                    eventBus.$emit("refreshUsedSpace", -size)
                    that.getFilesAtCurrentPath()
                    that.notifiy(response.message, null, response.data.status==true?"success":"error")
                })
                .catch((error)=>{
                    that.notifiy("删除出错", null, "error")
                })
            }
        },
        /**
         * 文件大小显示--单位转换
         */
        displaySize(file){
            let size = file.size
            if(file.directory == 1){
                return '-'
            }
            if(size < 1024){
                return size + 'B'
            }
            else if(size < 1024 * 1024){
                return Math.round(size / 1024) + 'KB'
            }
            else if(size < 1024 * 1024 * 1024){
                return Math.round(size / (1024*1024)) + 'MB'
            }
            else{
                return Math.round(size / (1024*1024*1024)) + 'GB'
            }
        },
        /**
         * 新建文件夹弹出框
         */
        openCreateFolder(){
            let that = this
            this.$prompt('输入文件夹名称', '新建文件夹', {
                confirmButtonText: "创建",
                cancelButtonText: "取消",
                inputValidator: this.folderNameValidator,
                inputErrorMessage: "文件夹重名或不符合命名规范（15字符以内）"
            }).then(({value})=>{
                that.createFolder(value)      
            })
        },
        /**
         * 文件夹命名规范检查
         */
        folderNameValidator(input){
            if(input.length > 15){
                return false
            }
            for(let i=0;i<this.files.length;i++){
                if(input == this.files[i].filename && this.files[i].directory==1) {
                    return false
                }
            }
            return true
        },

        /**
         * 新建文件夹
         */
        createFolder(name){
            let that = this
            request.post("/folder", null, {params: {
                folderName: name,
                path: this.currentPath
            }})
            .then((response)=>{
                that.getFilesAtCurrentPath()
                that.notifiy(response.message, null, response.data.status==true?"success":"error")
            })
            .catch((error)=>{
                that.notifiy("创建文件夹出错", null, "error")
            })
        },
        /**
         * 上传文件
         */
        uploadFile(params){
            let that = this
            const formData = new FormData()
            formData.append("file", params.file)

            request.post("/file/upload/"+this.currentPath, formData, {
                onUploadProgress: (event)=>{
                    console.log(event.loaded / event.total)
                }
            })
            .then((response)=>{
                that.getFilesAtCurrentPath()
                SidebarVue.methods.getUsedSpace()
                that.notifiy(response.message, null, response.data.status==true?"success":"error")
            })
            .catch((error)=>{
                that.notifiy("上传文件出错", null, "error")
            })

        },
        /**
         * 多选框处理
         */
        handleSelectionChange(val){
            this.selections = val
            if(this.selections.length > 0){
                this.selected = true
            }
            else{
                this.selected = false
            }
        },
        /**
         * 删除所选
         */
        deleteSelected(){
            let that = this
            if(confirm("是否删除所选的文件(无法找回)")==true){
                for(let i=0;i<this.selections.length;i++){
                    let file = this.selections[i]
                    request.delete("/" + (file.directory==1?"folder":"file") + "/" + file.fileId)
                    .then(response=>{
                        if(response.data.status==false){
                            that.notifiy("删除失败", "文件名：" + file.filename, "error")
                        }
                        eventBus.$emit("refreshUsedSpace", -file.size)
                        that.getFilesAtCurrentPath()
                    })
                    .catch(error=>{
                        that.notifiy("删除出错", "文件名："+file.filename, "error")                        
                    })
                }
                this.notifiy("删除完成", null, "success")
            }
        },
        /**
         * 侧边通知
         */
        notifiy(titile, message, type){
            this.$notify({
                title: titile,
                message: message,
                type: type
            })
        },

        /**
         * 顶部消息
         */
        message(message, type){
            this.$message({
                message: message,
                type: type,
                duration:2500
            })
        },

        /**
         * 搜索全部文件
         */
        search(){
            let that = this
            request.get("/file/search/"+this.searchInput+"/all")
            .then(data=>{
                that.files = data
                if(that.currentPath != 0){
                    that.pathStack = []
                    that.currentPath = 0
                    that.pathStack.push({fileId: 0, name:"搜索结果"})
                }                
            })
            .catch(error=>{
                this.notifiy("搜索出错", null, "error")
                console.log(error)
            })
        },
        dummy(){

        }
    }
}
</script>

<style>
.toolbar{
    border-bottom: solid 1px #e6e6e6;
}
a:hover{
    cursor: pointer;
    color:blue;
}
</style>