<template>
  <el-col :span="4">
    <el-menu
      class="el-menu-vertical-demo"
      :default-active="activated"
      background-color="#EBEEF5"
    >
      <el-menu-item index="1" onclick="location='/files'">
        <i class="el-icon-folder-opened"></i>
        <span slot="title"><a>全部文件</a></span>
      </el-menu-item>
      <el-menu-item index="2" onclick="location='/pictures'">
        <i class="el-icon-picture-outline"></i>
        <span slot="title">图片</span>
      </el-menu-item>
      <el-menu-item index="3">
        <i class="el-icon-video-play"></i>
        <span slot="title">视频</span>
      </el-menu-item>
      <el-menu-item index="4" onclick="location='/musics'">
        <i class="el-icon-headset"></i>
        <span slot="title">音乐</span>
      </el-menu-item>
      <el-menu-item index="5" onclick="location='/documents'">
        <i class="el-icon-document"></i>
        <span slot="title">文档</span>
      </el-menu-item>
      <br /><br />
      <el-menu-item index="6" onclick="location='/share'">
        <i class="el-icon-share"></i>
        <span slot="title">我的分享</span>
      </el-menu-item>
      <el-menu-item index="7" onclick="location='/recyclebin'">
        <i class="el-icon-delete"></i>
        <span slot="title">回收站</span>
      </el-menu-item>
      <br />

      <el-tooltip :content="getUsedSpaceAndMaxSpace()" placement="bottom">
        <el-progress
          :percentage="getPercentage()"
          style="margin: 10px"
          :color="customColors"
        ></el-progress>
      </el-tooltip>

      <br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br /><br />
    </el-menu>
  </el-col>
</template>

<script>
import request from "../utils/request";
import eventBus from "../utils/eventBus"
export default {
  name: "Sidebar",
  props:['activated'],
  data() {
    return {
      usedSpace: 0,
      maxSpace: 524288000,
      percentage: 0,
      customColors: [
        { color: "#909399", percentage: 80 },
        { color: "#e6a23c", percentage: 90 },
        { color: "red", percentage: 100 },
      ],
    };
  },
  created(){
    let that = this
    eventBus.$on("refreshUsedSpace", (size)=>{
      localStorage.setItem("usedSpace", that.usedSpace + size)
      that.usedSpace += size
    })
  },
  mounted() {
    this.getMaxSpace();
    this.getUsedSpace();
  },
  methods: {
    getUsedSpace() {
      let that = this;
      console.log("getting usedsPACE")
      request
        .get("/user/usedSpace")
        .then((response) => {
          that.usedSpace = response
          localStorage.setItem("usedSpace", response)
        })
        .catch((error) => {
          console.error(error);
        });
    },
    getMaxSpace() {
      let that = this;
      request
        .get("/user/maxSpace")
        .then((response) => {
          that.maxSpace = response;
          localStorage.setItem("maxSpace", response)
        })
        .catch((error) => {
          console.error(error);
        });
    },
    getPercentage() {
      this.percentage = Math.round(this.usedSpace/this.maxSpace * 100)
      return this.percentage;
    },
    displaySize(size) {
      if (size < 1024) {
        return size + "B";
      } else if (size < 1024 * 1024) {
        return Math.round(size / 1024) + "KB";
      } else if (size < 1024 * 1024 * 1024) {
        return Math.round(size / (1024 * 1024)) + "MB";
      } else {
        return Math.round(size / (1024 * 1024 * 1024)) + "GB";
      }
    },
    getUsedSpaceAndMaxSpace(){
      return this.displaySize(this.usedSpace) + " / " + this.displaySize(this.maxSpace)
    }
  },
};
</script>

<style>
a {
  text-decoration: none;
}
</style>