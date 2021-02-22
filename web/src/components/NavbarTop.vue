<template>
  <el-menu
      :default-active="activeIndex"
      class="el-menu-demo"
      mode="horizontal"
      @select="handleSelect"
      background-color="#304156"
    >
    <el-row>
      <el-col :span="3">
        <el-menu-item index="1" style="color:white"><i class="el-icon-cloudy"/>cloud</el-menu-item>
      </el-col>
      <el-col :span="12">
        <p></p>
      </el-col>
      <el-col :span="6">
      </el-col>
      <el-col :span="3">
        
      </el-col>
    </el-row>
    
      
    </el-menu>
</template>

<script>
import musicPlayer from './musics/musicPlayer.vue'
export default {
  components: { musicPlayer },
    name: 'NavbarTop',
    data(){
        return {
            
        }
    }
}
</script>

<style>
</style>