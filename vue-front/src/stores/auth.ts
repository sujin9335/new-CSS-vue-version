// stores/authStore.ts
import { acceptHMRUpdate, defineStore } from 'pinia';
import axios from "@/axios";



export const useAuthStore = defineStore('auth', {
  state: () => ({ 
    loginInfo: null,
    loginMsg: "",
    
  }),
  getters: {
    getLoginInfo: (state) => state.loginInfo,
    getLoginMsg: (state) => state.loginMsg,
    isLogin: (state) => state.loginInfo !== null,
  },
  actions: {
    async login(data) { //로그인
        try {
            const result = await axios.post(
                '/a/vloginCheck',
                {info: data}
            );

            const loginInfo = result.data;

            if (result.status === 200) {
                // console.log(result.data);
                if(!loginInfo.hasOwnProperty('msg')) {
                    this.loginInfo = loginInfo.userInfo;
                }else {
                    this.loginMsg = loginInfo.msg;
                }
                return loginInfo
                
            }
        } catch (error) {
            alert("로그인 통신에러");
        }
    },

    async logout() { //로그아웃
        try {
            const result = await axios.post(
                '/a/vlogout'
            );

            const logoutInfo = result.data;

            if (result.status === 200) {
                // console.log(result.data);
                if(logoutInfo.msg == "logout") {
                    // alert("로그아웃 성공");
                    this.loginInfo = null;

                    console.log("로그아웃 인포");
                    console.log(logoutInfo);
                    (await import('@/router')).default.push({name: 'login'});
                }else {
                    alert("로그아웃 실패");

                }
                // return logoutInfo
                
            }
        } catch (error) {
            alert("로그아웃 통신에러");
        }
    },

    async getSession() { //스프링부트 세션저장된 정보 가져오기
        try {
            const result = await axios.post(
                '/a/vgetSession'
            );

            const sessionInfo = result.data;

            if (result.status === 200) {
                // console.log(result.data);
                if(!sessionInfo.hasOwnProperty('msg')) {
                    this.loginInfo = sessionInfo.userInfo;
                    // alert("성공");
                }else {
                    this.loginMsg = sessionInfo.msg;
                }
                
            }
        } catch (error) {
            alert("세션 통신에러");
        }
    }
  },
});


// HMR 지원 설정(변경사항 바로적용)
if (import.meta.hot) {
    import.meta.hot.accept(acceptHMRUpdate(useAuthStore, import.meta.hot));
}
