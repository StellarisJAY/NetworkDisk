<template>
    <div>
        <my-share-list/>
    </div>
</template>

<script>
import NavbarTop from '../NavbarTop.vue'
import Sidebar from '../Sidebar.vue'
import MyShareList from './MyShareList.vue'
export default {
    name: 'MyShares',
    components: { NavbarTop, Sidebar, MyShareList },

}
</script>

<style>

</style>