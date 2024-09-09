import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/login.vue'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: Login
    },
    {
      path: '/test',
      name: 'test',
      component: () => import('../views/test.vue')
    },
    {
      path: '/test2',
      name: 'test2',
      component: () => import('../views/test2.vue')
    },
    {
      path: '/test3',
      name: 'test3',
      component: () => import('../views/test3.vue')
    },
    {
      path: '/test4',
      name: 'test4',
      component: () => import('../views/test4.vue')
    },
    {
      path: '/test5',
      name: 'test5',
      component: () => import('../views/test5.vue')
    },
    {
      path: '/monitor',
      name: 'monitor',
      component: () => import('../views/monitor.vue')
    },
    {
      path: '/home',
      name: 'home',
      component: () => import('../views/home.vue')
    },
    {
      path: '/user',
      name: 'user',
      component: () => import('../views/user.vue')
    },
    {
      path: '/group',
      name: 'group',
      component: () => import('../views/group.vue')
    },
    {
      path: '/board',
      name: 'board',
      component: () => import('../views/board.vue')
    },
  ],

})

//라우터 이동시 로그인값확인
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore();

  let loginInfo = authStore.loginInfo;
  console.log("라우터 로그인정보");
  console.log(loginInfo);
  console.log(authStore.isLogin);
  await authStore.getSession(); //세션 값 저장

  //세션 정보가 없고 && 이동하는 페이지가 'login'이 아닐때 
  if (!authStore.isLogin && to.name !== 'login') {
    next({ name: "login" });
  } else {
    next();
  }
});

export default router