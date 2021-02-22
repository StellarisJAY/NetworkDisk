<template>
    <div>
        <sidebar :activated="'2'"/>
        <el-col :span="20">
            <el-tabs v-model="activeTag" type="card">
                <el-tab-pane label="智能分类" name="classfication">
                    <classfication/>
                </el-tab-pane>
                <el-tab-pane label="图片列表" name="list">
                       <picture-list/>
                </el-tab-pane>
                <el-tab-pane label="最近上传" name="recent">
                    最近上传
                 </el-tab-pane>
            </el-tabs>
        </el-col>
    </div>
</template>

<script>
import NavbarTop from '../NavbarTop.vue'
import Sidebar from '../Sidebar.vue'
import Classfication from './classfication.vue'
import PictureList from './picture_list.vue'
export default {
  components: {
        NavbarTop,
        Sidebar,
        Classfication,
        PictureList
  },
  data(){
      return{
          activeTag:"list"
      }
  }

}
</script>
<style>

</style>