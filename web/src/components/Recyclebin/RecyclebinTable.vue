<template>
    <el-col :span="20">
        <el-main>
            <el-header class="toolbar">
                <el-col :span="8">
                    <el-button type="warning">清空回收站</el-button>
                    <el-button type="warning">删除所选</el-button>
                    <el-button type="warning">还原所选</el-button>
                </el-col>
                <el-col :span="8"><p></p></el-col>
                <el-col :span="8">
                    <el-input placeholder="搜索回收站" suffix-icon="el-icon-search"></el-input>
                </el-col>
            </el-header>
            <el-table :data="list" height="500px">
                <el-table-column type="selection"></el-table-column>
                <el-table-column label="文件名" prop="filename" sortable></el-table-column>
                <el-table-column label="类型" prop="type" sortable></el-table-column>
                <el-table-column label="删除时间" prop="deletedTime" sortable></el-table-column>
                <el-table-column label="有效时间" prop="expireTime" sortable></el-table-column>
                <el-table-column label="操作">
                    <template slot-scope="scope">
                        <el-button type="info" @click="recover(scope.row.fileId)" size="mini">还原</el-button>
                        <el-button type="warning" @click="delete(scope.row.fileId)" size="mini">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-main>
    </el-col>
</template>

<script>
export default {
    name: 'RecycleTable',
    data(){
        return {
            list: [
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'},
                {fileId: 1, filename: 'test', type: 'txt', deletedTime: '2020-11-15:20:04:33', expireTime: '30天'}
            ]
        }
    }
}
</script>

<style>
.toolbar{
    border-bottom: solid 1px #e6e6e6;
}
</style>