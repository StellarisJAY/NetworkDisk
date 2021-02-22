<template>
  <el-col :span="20">
        <el-main>
            <el-header style="padding:10px" class="toolbar">
                <el-col :span="8">
                    <el-button type="warning" v-if="selected==true">取消所选分享</el-button>
                    <p v-else></p>
                </el-col>
                <el-col :span="8">
                    <p></p>
                </el-col>
                <el-col :span="8" align="right">
                    <el-input placeholder="搜索我的分享" suffix-icon="el-icon-search"></el-input>
                </el-col>
            </el-header>
            <el-table :data="shareList" height="500px" @selection-change="handleSelectionChange">
                <el-table-column type="selection" ></el-table-column>
                <el-table-column label="文件名" prop="filename" sortable></el-table-column>
                <el-table-column label="保存次数" prop="saveTimes" sortable></el-table-column>
                <el-table-column label="下载次数" prop="downloadTimes" sortable></el-table-column>
                <el-table-column label="创建日期" prop="createDate" sortable></el-table-column>
                <el-table-column label="失效时间" prop="expireTime" sortable></el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button type="info" @click="showDetail(scope.row.fileId)" size="mini">详情</el-button>
                        <el-button type="warning" @click="cancleShare(scope.row.fileId)" size="mini">取消</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-main>
    </el-col>
</template>

<script>
export default {
    name: 'MyShareList',
    data(){
        return {
            shareList: [
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'},
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'},
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'},
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'},
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'},
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'},
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'},
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'},
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'},
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'},
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'},
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'},
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'},
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'},
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'},
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'},
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'},
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'},
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'},
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'},
                {fileId: 0, filename: 'test', saveTimes: 100, downloadTimes: 200, createDate: '2020-11-15', expireTime: '永久有效'}
            ],
            selections: [],
            selected: false
        }
    },
    methods: {
        handleSelectionChange(val){
            this.selections = val
            console.log(this.selections)
            if(this.selections.length == 0){
                this.selected = false
            }
            else{
                this.selected = true
            }
        }
    }
}
</script>

<style>
.toolbar{
    border-bottom: solid 1px #e6e6e6;
}
</style>