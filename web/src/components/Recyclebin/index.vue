<template>
    <div>
        <recyclebin-table/>
    </div>
</template>

<script>
import NavbarTop from '../NavbarTop.vue'
import Sidebar from '../Sidebar.vue'
import RecyclebinTable from './RecyclebinTable.vue'

export default {
    name: 'Recyclebin',
    components: {
        NavbarTop,
        Sidebar,
        RecyclebinTable
    },
    data(){
        return {
            
        }
    }
}
</script>

<style>

</style>