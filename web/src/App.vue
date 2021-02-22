<template>
  <div id="app" style="height:100%">
    <navbar-top/>
    <el-row>
      <router-view/>
    </el-row>
  </div>
</template>

<script>
import NavbarTop from './components/NavbarTop.vue';
import Sidebar from './components/Sidebar.vue'
export default {
  name: "App",
  data() {
    return {};
  },
  components: {
    Sidebar,
    NavbarTop
  }
};
</script>

<style>
body{
  margin:0px;
  overflow-y: hidden;
}
#app{
  overflow: hidden;
}
</style>
