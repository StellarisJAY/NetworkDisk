<template>
    <el-table :data="files">
        <el-table-column type="selection"></el-table-column>
        <el-table-column prop="filename" label="文件名" fixed sortable>
            <template slot-scope="scope">
                <el-avatar shape="square" style="background-color:white" :size="20" :src="getFileIcon(scope.row.type)"></el-avatar>
                <a role="link" v-if="scope.row.directory==1" @click="enterFolder(scope.row.fileId, scope.row.filename)">{{scope.row.filename}}</a>
                <a v-else>{{scope.row.filename}}</a>
            </template>
        </el-table-column>
        <el-table-column prop="type" label="类型" fixed sortable></el-table-column>
        <el-table-column prop="size" label="大小" fixed sortable>
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
</template>

<script>
export default {
    name:"FileTable",
    data(){
        return{
            files:[],
            selections: []

        }
    }
}
</script>

<style>

</style>