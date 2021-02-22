import Vue from 'vue'
import Router from 'vue-router'
import AllFiles from '../components/AllFiles/index'
import MyShares from '../components/MyShares/index'
import Recyclebin from '../components/Recyclebin/index'
import LoginForm from '../components/login/index'
import RegisterForm from '../components/register/index'
import AllPictures from '../components/pictures/index'
import MyDocuments from '../components/documents/index'
import MyMusics from '../components/musics/index'
Vue.use(Router)


const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/files',
      name: 'AllFiles',
      component: AllFiles
    },
    {
      path: '/share',
      name: 'MyShares',
      component: MyShares
    },
    {
      path: '/recyclebin',
      name: 'RecycleBin',
      component: Recyclebin
    },
    {
      path: '/login',
      name: 'LoginForm',
      component: LoginForm
    },
    {
      path: '/register',
      name: 'RegisterForm',
      component: RegisterForm
    },
    {
      path: '/pictures',
      name: "AllPictures",
      component: AllPictures
    },
    {
      path: '/documents',
      name: 'AllDocuments',
      component: MyDocuments
    },
    {
      path: '/musics',
      name: 'AllMusics',
      component: MyMusics
    }
  ]
})

export default router
