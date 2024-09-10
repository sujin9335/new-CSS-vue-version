


<template lang="">
    <div class="screen-wrapper"><!-- html -->
		<div class="inner-wrapper"><!-- body -->
			<div class='box'>
				<div class='wave -one'></div>
				<div class='wave -two'></div>
				<div class='wave -three'></div>
			</div>
			<div class="login_pages">
				<div class="container">
					<div class="row justify-content-center">
						<div class="col-5">
							<div class="card login_area">
								<div class="card-body p-4">
									<div class="text-center w-75 m-auto">
										<span><img src="@/assets/img/logo3.png"></span>
										<p class="text-muted mb-4 mt-3"></p>
									</div>
									<form method="post" id="login_form" action="/loginCheck" @submit.prevent="submitLogin()">
										<div class="form-group mb-3">
											<label for="emailaddress">User ID</label>
											<input class="form-control" type="text" id="id" name="id" v-model="id" required placeholder="User ID">
										</div>
										<div class="form-group mb-3">
											<label for="password">Password</label>
											<input class="form-control" type="password" required id="pw" name="pw" v-model="pw" placeholder="Password">
										</div>
										
										<div class="form-group mb-0 text-center">
											<!-- <a class="btn btn-primary btn-block" href="javascript:mysubmit()"> Log In </a> -->
											<input class="btn btn-primary btn-block" type="submit" value="Log In">
										</div>
									</form>
								</div> <!-- end card-body -->
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
            loginId: "sj",
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
<style scoped>
.screen-wrapper{background:url(@/assets/img/full_bg.jpg) no-repeat;background-size: cover;animation:efect1 2s ease-out 1 forwards; height: 100vh;}
/* .inner-wrapper{background: linear-gradient(45deg, rgba(0,0,0,0) 68%, #2fa1f3 32%);height: 100%; color: #cacaca;} */
.inner-wrapper{background:linear-gradient(rgba(0,0,0,.4),  rgba(38,129,214,.2));backdrop-filter: blur(0px);transition: 1s;overflow: hidden; background-size: cover;height: 100vh;}

.container{padding-top:250px}
.card {margin-bottom:24px;box-shadow: 0px 0px 500px 20px #000;border: 1px solid #fff;}@media (min-width:576px) {.container {max-width:1140px}}
@media (min-width:576px) {.container {max-width:1140px}}
@media (min-width:768px) {.container {max-width:1140px}}
@media (min-width:992px) {.container {max-width:1140px}}
@media (min-width:1200px) {.container {max-width:1140px}}

/* .login_logo{background:#1a1b20 url(/resources/dist/img/login_bg.jpg) no-repeat top center;background-size:cover; height: 400px; border-radius: 20px;padding: 80px 10px;color:#fff}
.login_bg{background: linear-gradient(to top, #f5f5f5 50%, #1a1b20 50%);border-radius: 52px;box-shadow: 0 0 30px #005b9c;border: 1px solid #5485a9;}
.login_area{background: linear-gradient(45deg, #0f1216 80%, #2fa1f3 20%); border-radius: 20px}
.login-box{margin:100px 50px;}
.login-box .form-control{background: #fff;border: 1px solid #d4d4d4; color:#000}
.login-box .form-group {margin-bottom: 20px;} */

html{background:url(/img/full_bg.jpg) no-repeat;background-size: cover;animation:efect1 2s ease-out 1 forwards;}
/* body{background: rgba(0,0,0,.7);backdrop-filter: blur(5px);transition: 1s;overflow: hidden; } */
/* html{background:#000;background-size: cover;animation:efect1 2s ease-out 1 forwards; min-width: 1440px} */
body{background:linear-gradient(rgba(0,0,0,.4),  rgba(38,129,214,.2));backdrop-filter: blur(0px);transition: 1s;overflow: hidden; background-size: cover;height: 100vh;}

.login_logo{background:#1a1b20 url(/resources/dist/img/login_bg.jpg) no-repeat top center;background-size:cover; height: 400px; border-radius: 20px;padding: 80px 10px;color:#fff}
.login_bg{background: linear-gradient(to top, #f5f5f5 50%, #1a1b20 50%);border-radius: 52px;box-shadow: 0 0 30px #000;border: 1px solid #5485a9;}
.login_area{background: rgba(0,0,0,.5); border-radius: 20px; z-index: 2000;}
.login-box{margin:100px 50px;}
.login-box .form-control{background: #fff;border: 1px solid #d4d4d4; color:#000}
.login-box .form-group {margin-bottom: 20px;}
.form-control,:deep(.form-control){border: 1px solid #ffffff;background: transparent;}
.login_pages{animation:efect2 2s ease-out 1 forwards;position: absolute;z-index: 100000;width: 100%;}

.modal-backdrop{background: rgba(255,255,255,.5);backdrop-filter: blur(25px);}
.form-control:focus{background-color: transparent;}
select option,:deep(select option) {background: #000;}
/* .form-control{border: 1px solid #00a1ff;} */

/* input:-webkit-autofill,
input:-webkit-autofill:hover,
input:-webkit-autofill:focus,
input:-webkit-autofill:active
{
   -webkit-box-shadow: 0 0 0 1000px #ffffff inset !important;
  -webkit-text-fill-color: #a8b5c3 !important;
} */
.realperson-text{font-size: 10px}



@keyframes efect1 {
	0%{opacity: 0}
	50%{opacity: 1}
	100%{opacity: 1}
}

@keyframes efect2 {
	0%{margin-top:-100%;opacity: 0}
	50%{margin-top:0;opacity: 0.5}
	100%{margin-top:0;opacity: 1}
}
@keyframes efect3 {
	0%{opacity: 0}
	50%{opacity: 0.5}
	100%{opacity: 1}
}
.box {
  width: 100%;
  height: 100%;
  border-radius: 5px;
  position: absolute;
  overflow: hidden;
  transform: translate3d(0, 0, 0);
  animation:efect3 3s ease-out 1 forwards;
  z-index: 1000;
}

.wave {
  opacity: .3;
  position: absolute;
  top: 8%;
  left: 50%;
  background: #00abff;
  width: 750px;
  height: 750px;
  margin-left: -375px;
  margin-top: 50px;
  transform-origin: 50% 48%;
  border-radius: 43%;
  animation: drift 20000ms infinite linear;
  filter: blur(2px);
}

.wave.-three {
  animation: drift 40000ms infinite linear;
}

.wave.-two {
  animation: drift 60000ms infinite linear;
  opacity: .1;
  background: #00d7ff;
}

.box:after {
  content: '';
  display: block;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  z-index: 11;
  transform: translate3d(0, 0, 0);
}

@keyframes drift {
  from { transform: rotate(0deg); }
  from { transform: rotate(360deg); }
}
</style>