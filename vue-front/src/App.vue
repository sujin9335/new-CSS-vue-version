<template>
  <Header class="top_bar" v-if="isLogin"></Header>
  <router-view></router-view>
</template>

<script lang="ts">
import Header from '@/components/header.vue';
import { useAuthStore } from '@/stores/auth';


export default {
  //헤더지정
  components: {
    Header
  },
  computed: {
    isLogin() {//로그인시 헤더보이게할건지
      const authStore = useAuthStore();
      return authStore.isLogin;
    }
  }
}

</script>

<style >



</style>
