<template>
    <div>
        <sidebar :activated="'1'"/>
        <file-area/>
    </div>
</template>

<script>
import NavbarTop from '../NavbarTop.vue'
import Sidebar from '../Sidebar.vue'
import FileArea from './FileArea.vue'
export default {
    name: 'AllFiles',
    components: {
        NavbarTop,
        Sidebar,
        FileArea
    },
    data(){
        return {

        }
    }
}
</script>

<style>

</style>