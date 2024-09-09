import axios from 'axios';

// Axios 인스턴스 생성
const defaultAxios = axios.create({
    headers: { 'Content-Type': 'application/json' }, // 기본 헤더 설정
    timeout: 1000, // 요청 타임아웃 설정 (ms)
});

export default defaultAxios;