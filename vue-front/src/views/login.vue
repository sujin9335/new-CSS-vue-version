


<template lang="">
    <div class="container mt-5">
        <main class="form-signin w-25 mx-auto">
            <form method="post" id="login_form" action="/loginCheck" @submit.prevent="submitLogin()">
                <h1 class="h3 mb-3 fw-normal">로그인</h1>

                <div class="form-floating">
                    <input type="text" class="form-control" id="id" name="id" v-model="loginId">
                    <label for="floatingInput">아이디</label>
                </div>
                
                <div class="form-floating">
                    <input type="password" class="form-control" id="pw" name="pw" v-model="pw">
                    <label for="floatingPassword" >비밀번호</label>
                </div>
                <div>
                    <label>
                        <input type="checkbox" v-model="rememberId"/>
                        아이디저장
                    </label>
                </div>

                <button class="btn btn-primary w-100 py-2" type="submit">로그인</button>
            </form>
            <button class="btn btn-secondary w-100 py-2 mt-2" type="submit" @click="modalClear" data-bs-toggle="modal"
            data-bs-target="#userRegis">회원가입</button>
            <div class="text-center">
                <span>관리자의 승인 후 로그인 가능</span>
            </div>
            <div class="text-center">
                <p v-if="msgError" style="color: red;">{{ msgError }}</p>
            </div>
        </main>
    </div>

    <!-- 회원가입 모달창 -->
    <div class="modal fade" id="userRegis" data-bs-backdrop="static" data-bs-keyboard="false"
        tabindex="-1" aria-labelledby="staticBackdropLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered modal-custom">
            <div class="modal-content">

                <div class="modal-body">
                    <div class="container text-center" id="regisContent">
                        <div class="row">

                            <div class="col-2 pt-3">
                                <h3>아이디</h3>
                            </div>
                            <div class="col-8 text-start px-4 pt-3 in-title" id="login_id">
                                <input type="text" class="w-80" :disabled="isInputIdDisabled" v-model="regisId">
                                <button :id="btnCheckId" :class="btnCheckClass" @click="btnCheckClick">{{ btnCheckText }}</button>
                            </div>
                            

                            <div class="col-1 editDel">
                                <div id="id" style="display: none;"></div>
                                <div id="flag" style="display: none;"></div>
                            </div>

                            <div class="col-1">
                                <!-- <button type="button" class="btn btn-dark" data-bs-dismiss="modal" onclick="close_modal()">닫기</button> -->
                                <button type="button" class="btn btn-dark"
                                    data-bs-dismiss="modal" ref="modalClose">닫기</button>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-2 pt-3 pw">
                                <h3>비밀번호</h3>
                            </div>
                            <div class="col-10 text-start px-4 pt-3 in-title pw" id="pw">
                                <input type="password" class="w-80 d-block m-1" id="pw_ch1" v-model="regisPw1" @input="checkPw" placeholder="비밀번호">
                                <input type="password" class="w-80 m-1" id="pw_ch2" v-model="regisPw2" @input="checkPw" placeholder="비밀번호 확인">
                                <p id="checkPwText" :style="{color: checkPwStyle}">{{ checkPwText }}</p>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-2 pt-3">
                                <h3>이름</h3>
                            </div>
                            <div class="col-10 text-start p-4 " id="name">
                                <input type="text" class="w-100" v-model="regisName">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-2 pt-3">
                                <h3>핸드폰번호</h3>
                            </div>
                            <div class="col-10 text-start p-4" id="tel">
                                <input type="text" class="w-100" placeholder="010-1234-1234 형식으로 써주세요" v-model="regisTel">
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-2 pt-3">
                                <h3>이메일</h3>
                            </div>
                            <div class="col-8 text-start p-4" id="mail">
                                <input type="text" class="w-100" v-model="regisEmail">
                            </div>
                            <div class="col-2">
                                <button @click="regisUser" class="btn btn-info">가입</button>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script lang="ts">
import { mapWritableState } from "pinia";
import { useAuthStore } from "@/stores/auth";
import axios from "@/axios";

export default {
    data() {
        return {
            //로그인
            loginId: "",
            pw: "sujin1!",
            msgError: "",
            //회원가입
            regisId: "",
            regisPw1: "",
            regisPw2: "",
            regisName: "",
            regisTel: "",
            regisEmail: "",

            //ID중복확인
            btnCheckId: "idCh",
            btnCheckClass: "btn btn-danger",
            btnCheckText: "중복체크",
            isInputIdDisabled: false,
            btnCheckClick: this.checkId, //실행 함수 변경

            //PW확인
            checkPwText: "비밀번호 4글자 이상 16글자 이하, 영문, 숫자, 특수문자 사용",
            checkPwStyle: "red",

            //아이디 저장
            rememberId: false,

        };
    },
    created() {
        // 페이지 로드 시 로컬 스토리지에서 아이디와 체크박스 상태를 가져옴
        this.loginId = localStorage.getItem('loginId') || '';
        this.rememberId = localStorage.getItem('rememberId') === 'true';
    },    
    // mounted() {
    //     if(this.loginMsg) {
    //         alert(this.loginMsg);
    //     }
    // },
    methods: {
        async submitLogin() {
            //유효성검사
            if (this.loginId.trim() === "") {
                this.msgError = "아이디를 입력해주세요";
                return;
            }
            if (this.pw.trim() === "") {
                this.msgError = "비밀번호를 입력해주세요";
                return;
            }

            const userInfo = {
                id: this.loginId,
                pw: this.pw,
            };

            const authStore = useAuthStore();
            const result = await authStore.login(userInfo); //로그인 해서 반환 정보
            if (result.msg) {
                // 로그인 문제있을경우 {"msg": "내용"} 으로 되어있음
                // alert(result.msg);
                this.msgError = result.msg;
            } else {

                if (this.rememberId) {
                    // 체크박스가 선택된 경우 아이디와 상태를 로컬 스토리지에 저장
                    localStorage.setItem('loginId', this.loginId);
                    localStorage.setItem('rememberId', this.rememberId);
                } else {
                    // 체크박스가 선택되지 않은 경우 아이디와 상태를 로컬 스토리지에서 삭제
                    localStorage.removeItem('loginId');
                    localStorage.removeItem('rememberId');
                }

                await this.$router.push({ name: "monitor" });
                // window.location.href = 'http://localhost:25000/highChart/';
            }
            console.log(result);
        },

        async checkId() {
            //아이디 중복검사

            //아이디 유효성 검사
            if (!/^[A-Za-z0-9]{1,8}$/.test(this.regisId)) {
                alert("ID는 8자 이하의 알파벳과 숫자로만 이루어져야 합니다");
                return;
            }

            const info = {
                user_login_id: this.regisId,
            };

            //중복체크 axios
            try {
                const result = await axios.post("/a/vIdCheck", {
                    info: info,
                });

                const checkIdInfo = result.data;

                if (result.status === 200) {
                    console.log(result.data.cnt);
                    if (!result.data.cnt > 0) {
                        this.btnCheckClass = "btn btn-success";
                        this.btnCheckText = "체크해제";
                        this.isInputIdDisabled = true;
                        if (this.btnCheckClick === this.checkId) { //실행함수 변경
                            this.btnCheckClick = this.btnClear;
                        }
                    } else {
                        alert("중복된 아이디 입니다");
                    }
                }
            } catch (error) {
                alert("id중복체크 통신에러");
            }
        },

        btnClear() {
            //중복체크 해제
            this.btnCheckClass = "btn btn-danger";
            this.btnCheckText = "중복체크";
            this.isInputIdDisabled = false;
            if (this.btnCheckClick === this.btnClear) {
                this.btnCheckClick = this.checkId;
            }
        },

        checkPw() {
            //비밀번호 이중 입력확인
            // console.log(this.regisPw1 + " " + this.regisPw2);

            if (
                !/^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{4,16}$/.test(
                    this.regisPw1
                )
            ) {
                this.checkPwText =
                    "비밀번호 4글자 이상 16글자 이하, 영문, 숫자, 특수문자 사용";
                this.checkPwStyle = "red";
                return;
            }

            if (this.regisPw1 === this.regisPw2) {
                this.checkPwText = "비밀번호 일치";
                this.checkPwStyle = "green";
            } else {
                this.checkPwText = "비밀번호 불일치";
                this.checkPwStyle = "red";
            }
        },

        async regisUser() { //회원가입
            if(!this.isInputIdDisabled) {
                alert("아이디 중복체크 확인");
                return;
            }
            if(this.checkPwStyle !== "green") {
                alert("비밀번호 확인");
                return;
            }
            if (!/^[A-Za-z가-힣]{1,10}$/.test(this.regisName)) {
                alert("이름은 10글자 이하 한글, 영어만 사용가능합니다(특수기호, 공백사용불가");
                return;
            }
            if (!/^01(?:0|1|[6-9])-(?:\d{4})-\d{4}$/.test(this.regisTel)) {
                alert("핸드폰 번호에 문제가 있습니다");
                return;
            }
            if (!/^[\w.%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/.test(this.regisEmail)) {
                alert("이메일 주소에 문제가 있습니다");
                return;
            }

            try { //회원가입 정보 전송
                const regisInfo = {
                    user_login_id: this.regisId,
                    user_pw: this.regisPw1,
                    user_name: this.regisName,
                    user_tel: this.regisTel,
                    user_mail: this.regisEmail
                };
                    
                const result = await axios.post("/a/vRegis", {
                    info: regisInfo,
                });

                const checkIdInfo = result.data;

                if (result.status === 200) {
                    // console.log(result.data);

                    if(result.data.resultUser != 1 ) {
                        alert("회원가입 DB 에러");
                    }

                    this.$refs.modalClose.click(); //모달 닫기
                }
            } catch (error) {
                alert("회원가입 통신에러");
            }
        },
        modalClear() { //모달창 입력 초기화
            this.regisId = "";
            this.regisPw1 = "";
            this.regisPw2 = "";
            this.regisName = "";
            this.regisTel = "";
            this.regisEmail = "";

            this.btnCheckId = "idCh";
            this.btnCheckClass = "btn btn-danger";
            this.btnCheckText = "중복체크";
            this.isInputIdDisabled = false;
            this.btnCheckClick = this.checkId; //실행 함수 변경

            this.checkPwText = "비밀번호 4글자 이상 16글자 이하, 영문, 숫자, 특수문자 사용";
            this.checkPwStyle = "red";
            
        }

    },
    // computed:{
    // 	...mapWritableState(useAuthStore,{
    // 		loginMsg:'loginMsg'
    // 	})

    // }
};
</script>
<style>
#regisContent div {
    border: 1px solid #ccc;
    border-collapse: collapse;
    padding: 3px;
}

.modal-custom {
    max-width: 50% !important;
}
</style>