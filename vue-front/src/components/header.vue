
<template>
    <div>
        <div class="top_logo"><img :src="logoUrl" /></div>
        <div class="header_box">
            <div class="header_inbox">
                <div class="header_con">
                    <ul id="main_menu" class="gnb">
                        <li>
                            <a href="/board"><span>게시판</span></a>
                        </li>
                        <li>
                            <a href="/user"><span>유저관리</span></a>
                        </li>
                        <li>
                            <a href="/group"><span>그룹관리</span></a>
                        </li>
                        <li>
                            <a href="/monitor"><span>구성</span></a>
                        </li>
                    </ul>
                </div>
                <!-- 사이드메뉴 -->
                <div class="side_menu" >
                    <div class="dropdown_menu cog_area" style="width: 350px; display: block; z-index: 1001;" v-if="isSideMenu">
                        
                        <ul class="cog_box">
                            <li 
                                @click="toggleModal">
                                <p class="img_02"></p>
                                <p>구성</p>
                            </li>
                            <li >
                                <p class="img_03"></p>
                                <p>콘텐츠</p>
                            </li>
                            <li 
                                @click="toggleEditMode"
                                :class="{'on': monitorStore.isEdit}" >
                                <p class="img_05"></p>
                                <p>편집모드</p>
                            </li>
                        </ul>
                        <div class="btn_area mt10 mb20">
                            <button @click.stop="isSideMenu = false" type="button" class="btn btn-secondary fr" ><i class="fa fa-close"></i> 닫기</button>
                        </div>
                    </div>
                    <!-- 사이드메뉴 보이는 아이콘 -->
                    <ul>
                        <!-- 구성 -->
                        <li class="cog_menu"
                            :class="{'on': monitorStore.isEdit}"
                            @click="isSideMenu = true"><i class="fa fa-cog"></i></li>
                        <!-- 사용자정보 -->
                        <li class="user_menu"><i class="fa fa-user"></i>
                            <div class="dropdown_menu">
                                <div class="pro_box2">
                                    <div class="pro_name">{{ userName }}</div>
                                    <div class="pro_id">{{ userAuth }}</div>
                                    <!-- <div class="btn_area tac">
                                        <button class="btn btn-primary btn-sm" onclick="updateUser()">정보 수정</button>
                                    </div> -->
                                </div>
                                
                            </div>
                        </li>
                        <!-- 로그아웃 -->
                        <li @click="authStore.logout()"><i class="fa fa-power-off"></i></li>
                        
                    </ul>
                </div>
            </div>
        </div>
        <modal v-model:show="isModal" modalId="monitorModal"/>
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
    const isSideMenu = ref(false);

    const userName = computed(() => authStore.loginInfo?.user_name || "게스트");
    const userAuth = computed(() => authStore.loginInfo?.user_auth == 1 ? '관리자' : '사용자');

    const toggleModal = () => {
        isModal.value = !isModal.value;
    };

    const toggleEditMode = () => {
        monitorStore.toggleEditMode();
    };

    



</script>
<style scoped>





</style>