<template>
    <div>
        <sidebar :activated="'5'"/>
        <file-list :fileType="'documents'"/>
    </div>
</template>

<script>
import NavbarTop from '../NavbarTop.vue'
import Sidebar from '../Sidebar.vue'
import FileList from '../FileList.vue'
export default {
    name: "MyDocuments",
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