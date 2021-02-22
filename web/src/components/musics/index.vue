<template>
    <div>
        <sidebar :activated="'4'"/>
        <file-list :fileType="'musics'"/>
    </div>
</template>

<script>
import NavbarTop from '../NavbarTop.vue'
import Sidebar from '../Sidebar.vue'
import FileList from '../FileList.vue'
export default {
    name: "MyMusics",
    components:{
        NavbarTop,
        Sidebar,
        FileList
    },
    data(){
        return{

        }
    }

}
</script>
<style>

</style>