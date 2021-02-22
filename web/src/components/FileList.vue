<template>
    <el-col :span="20">
         <el-header style="padding-top:10px" class="toolbar">
             <el-row>
                <el-col :span="8">
                    <el-button type="primary" v-if="selected==true" @click="downloadSelected">下载</el-button>
                    <el-button type="danger" v-if="selected==true" @click="deleteSelected">删除</el-button>
                    <p v-else></p>
                </el-col>
                <el-col :span="8">
                    <music-player v-if="fileType=='musics'"/>
                    <p v-else></p>
                </el-col>
                <el-col :span="8">
                    <el-input placeholder="搜索我的文件" v-model="searchInput" @keyup.enter.native="search" maxlength="20">
                        <el-button type="primary" slot="append" icon="el-icon-search" @click="search"></el-button>
                    </el-input>
                </el-col>
            </el-row>
        </el-header>
        <el-breadcrumb style="padding-left:20px">
            <el-col :span="20">
                <el-breadcrumb-item><a role="link" @click="getFiles()" v-if="inSearchMode==true">显示全部</a></el-breadcrumb-item>
                <el-breadcrumb-item ><a role="link" v-if="inSearchMode==true">搜索结果</a></el-breadcrumb-item>
            </el-col>
        </el-breadcrumb>
        <el-main>
            <!--数据表格-->
            <el-table :data="files" height="500px" @selection-change="handleSelectionChange">
                <el-table-column type="selection"></el-table-column>
                <el-table-column label="文件名" width="600em">
                    <template slot-scope="scope">
                        <el-avatar slot="reference" shape="square" style="background-color:white" :size="20" :src="getFileIcon(scope.row.type)"></el-avatar>
                        <!-- 音乐类型文件，弹出播放等按钮-->
                        <a slot="reference" @click="playMusic(scope.row)" v-if="fileType=='musics'">{{scope.row.filename}}</a>
                        <!-- 普通文件，无提示框-->
                        <a v-else>{{scope.row.filename}}</a>
                    </template>
                </el-table-column>
                <el-table-column label="大小" width="100em">
                    <template slot-scope="scope">
                        {{displaySize(scope.row)}}
                    </template>
                </el-table-column>
                <el-table-column label="修改日期" prop="updateDate"></el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button type="success" size="mini" icon="el-icon-download" @click="downloadFile(scope.row)"></el-button>
                        <el-button type="warning" size="mini" icon="el-icon-delete" @click="deleteFile(scope.row.fileId, scope.row.size)"></el-button>
                        <el-button type="primary" size="mini" icon="el-icon-share"></el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination
                background
                @size-change="handlePageSizeChange"
                @current-change="handleCurrentPageChange"
                :current-page.sync="pageNum"
                :page-sizes="[8, 20, 50, 100]"
                :page-size="pageSize"
                layout="sizes, prev, pager, next, total"
                :page-count="totalPage" align="center"
                :total="total"
                v-if="inSearchMode==false">
            </el-pagination>
        </el-main>
    </el-col>
</template>

<script>
import request from '../utils/request'
import eventBus from '../utils/eventBus'
import Config from '../settings'
import musicPlayer from './musics/musicPlayer.vue'
export default {
  components: { musicPlayer },
    name:"FileList",
    props:[
        "fileType"
    ],
    data(){
        return{
            files: [],
            selections: [],
            selected: false,
            pageSize: 8,
            pageNum: 1,
            totalPage: 0,
            total: 0,
            searchInput: "",
            inSearchMode: false,
            music: {
                isPlay: false,
                currentTime: 0,
                maxTime: 0,
                volume: 0.5,
                url: ""
            }
        }
    },
    mounted(){
        this.getFiles()
        console.log(this.fileType)
    },
    methods:{
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
        getFiles(){
            let that = this
            this.inSearchMode = false
            request.get("/file/"+this.fileType+"/"+this.pageNum+"/"+this.pageSize)
            .then((response)=>{
                that.total = response.total
                that.files = response.list
                that.totalPage = response.pages
            })
        },
        handlePageSizeChange(val){
            this.pageSize = val
            this.getFiles()
        },
        handleCurrentPageChange(){
            this.getFiles()
        },
        getFileIcon(type){
            return Config.getIconUrl(type)
        },
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

                link.download = file.filename + "." + file.type   
                document.body.appendChild(link)
                link.click()
            })
        },


        deleteFile(fileId, size){
            let that = this
            if(confirm("是否删除该文件？删除后将无法找回") == true){
                request.delete("/file/"+fileId)
                .then((response)=>{
                    eventBus.$emit("refreshUsedSpace", -size)
                    that.getFiles()
                    console.log(response)
                    that.notifiy(response.message, null, response.data.status==true?"success":"error")
                })
                .catch((error)=>{
                    console.log(error)
                    that.notifiy("删除出错", null, "error")
                })
            }
        },

        
        handleSelectionChange(val){
            this.selections = val
            if(this.selections.length > 0){
                this.selected = true
            }
            else{
                this.selected = false
            }
        },



        deleteSelected(){
            let that = this
            if(confirm("是否删除所选的文件(无法找回)")==true){
                for(let i=0;i<this.selections.length;i++){
                    let file = this.selections[i]
                    request.delete("/file/" + file.fileId)
                    .then(response=>{
                        if(response.data.status==false){
                            that.notifiy("删除失败", "文件名：" + file.filename, "error")
                        }
                        eventBus.$emit("refreshUsedSpace", -file.size)
                        that.getFiles()
                    })
                    .catch(error=>{
                        that.notifiy("删除出错", "文件名："+file.filename, "error")                        
                    })
                }
                this.notifiy("删除完成", null, "success")
            }
        },


        downloadSelected(){
            let downloadList = []
            for(let i = 0; i < this.selections.length; i++){
                downloadList[i] = this.selections[i].fileId
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


        search(){
            let that = this
            this.inSearchMode = true
            request.get("/file/search/"+this.searchInput+"/"+this.fileType)
            .then(data=>{
                that.files = data              
            })
            .catch(error=>{
                this.notifiy("搜索出错", null, "error")
                console.log(error)
            })
        },
        playMusic(file){
            eventBus.$emit("playMusic", file)
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