import 'bootstrap/dist/css/bootstrap.min.css'  // Bootstrap CSS 추가
import 'bootstrap-icons/font/bootstrap-icons.css'; // Bootstrap Icons 추가

import './assets/main.css'
import './assets/css/style.css'
// import './assets/css/font-awesome.min.css'
import './assets/css/font-awesome.css'
import './assets/css/tables.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'


import axios from './axios'// 엑시오스 추가
import * as bootstrap from 'bootstrap'
import { createPinia } from 'pinia' // 피니아 추가

// alertifyjs 추가
import alertify from 'alertifyjs';
import 'alertifyjs/build/css/alertify.min.css';
import 'alertifyjs/build/css/themes/default.min.css';

// highchart 추가
import Highcharts from 'highcharts';
import HighchartsVue from 'highcharts-vue';
import Drilldown from 'highcharts/modules/drilldown';

Drilldown(Highcharts);


const app = createApp(App);
const pinia = createPinia();


app.use(pinia);
app.use(router);
app.use(HighchartsVue, { Highcharts });


//전역속성으로 사용할 수 있음
app.config.globalProperties.$bootstrap = bootstrap;
app.config.globalProperties.$axios = axios;
app.config.globalProperties.$alertify = alertify;

app.mount('#app');