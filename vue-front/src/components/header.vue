
<template>
    <div>
        <div class="top_logo"><img :src="logoUrl" /></div>
            <div class="header_box">
                <div class="header_inbox">
                    <div class="header_con">
                        <ul id="main_menu" class="gnb">
                            <li>
                                <a href="/board" 
                                    :class="[
                                        { 'active': $route.path === '/board' },
                                        {'nav-link px-2': true}
                                    ]"
                                ><span>게시판</span></a>
                            </li>
                            <li>
                                <a href="/user" 
                                    :class="[
                                        { 'active': $route.path === '/user' },
                                        {'nav-link px-2': true}
                                    ]"
                                ><span>유저관리</span></a>
                            </li>
                            <li>
                                <a href="/group" 
                                    :class="[
                                        { 'active': $route.path === '/group' },
                                        {'nav-link px-2': true}
                                    ]"
                                ><span>그룹관리</span></a>
                            </li>
                            <li>
                                <a href="/monitor" 
                                    :class="[
                                        { 'active': $route.path === '/monitor' },
                                        {'nav-link px-2': true}
                                    ]"
                                ><span>구성</span></a>
                            </li>
                        </ul>
                    </div>
                </div>
                
            </div>
        <header
        class="d-flex flex-wrap align-items-center justify-content-center justify-content-md-between py-3 mb-4 border-bottom">
        <div class="col-md-3 mb-2 mb-md-0">
            <a href="/home" class="d-inline-flex link-body-emphasis text-decoration-none">
                home
            </a>
        </div>
        
        

        <div class="btn-group" role="group">
            <div data-bs-toggle="dropdown" aria-expanded="false" style="cursor: pointer;">
                <i class="bi bi-gear"></i>
            </div>
            <div class="dropdown-menu text-center p-3">
                <button 
                    @click="toggleModal"
                    type="button" class="btn btn-outline-secondary mb-2">구성</button>
                <button 
                    @click="toggleEditMode"
                    type="button" 
                    :class="[
                        { 'active': monitorStore.isEdit },
                        { 'btn btn-outline-secondary': true}
                    ]"
                    >편집모드</button>
            </div>
        </div>
        

        
        <!-- 로그아웃 버튼 표시 -->
        <div class="col-md-3 text-end">
            
            

            <span>안녕하세요 {{ userName }}님</span>
            <button type="button" class="btn btn-outline-primary me-2" @click="authStore.logout()">Logout</button>
            <!-- <button type="button" class="btn btn-outline-primary me-2"
            onclick="location.href='/'">Login</button> -->
        </div>
        

        
        </header>
        <modal v-if="isModal" @close="toggleModal"/>
    </div>
</template>
<script setup lang="ts">
    import { ref, computed } from "vue";
    import { useAuthStore } from "@/stores/auth";
    import modal from '@/components/modals/contents.vue';
    import { useMonitorStore } from '@/stores/monitor';
    import logo from '@/assets/img/logo3.png';

    // 스토어 사용
    const authStore = useAuthStore();
    const monitorStore = useMonitorStore();

    const isModal = ref(false); //모달창 on off
    const logoUrl = logo;

    const userName = computed(() => authStore.loginInfo?.user_name || "게스트");

    const toggleModal = () => {
        isModal.value = !isModal.value;
    };

    const toggleEditMode = () => {
        monitorStore.toggleEditMode();
    };


</script>
<style scoped>
.active {
    color: white;
    background-color: green;
}
</style>