<template>

<div class="card-box">
  <h4 class="header-title_dash">
    <div class="head_tit_name"> 경유국가  </div>
    
  </h4>
  <div class="con_box_content">
    <div id="container_13" style="min-width: 100%; height: 300px; margin: 0 auto"></div>
  </div>
</div>
    
</template>

<script setup lang="ts">
import { ref, defineProps, computed, onMounted} from 'vue';
import Highcharts from 'highcharts';
import HighchartsVue from 'highcharts-vue';
import Highcharts3D from 'highcharts/highcharts-3d'

Highcharts3D(Highcharts)

const chartData = ref([{ name: '서울', y: 120 },
                    { name: '부산', y: 110 },
                    { name: '대구', y: 80 },
                    { name: '인천', y: 95 }]);
const drillData = ref([]);

// 차트를 설정합니다.
const chartOptions: Highcharts.Options = {
  chart: {
        type: 'pie', // 차트 유형을 'pie'로 변경
        backgroundColor: 'transparent',
        options3d: {
          enabled: true, // 3D 활성화
          alpha: 10,
          beta: 0
        }
      },
      title: {
        text: ''
      },
      tooltip: {
        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
      },
      plotOptions: {
        pie: {
            innerSize: 70,
            depth: 45
        }
        
      },
      xAxis: {
        labels: {
            style: {
                color: '#fff' // x축 레이블 색상
            }
        },
        gridLineColor: '#fff' // x축 그리드 선 색상
    },
    yAxis: {
        labels: {
            style: {
                color: '#fff' // y축 레이블 색상
            }
        },
        gridLineColor: '#fff', // y축 그리드 선 색상
        title: {
            style: {
                color: '#fff' // y축 제목 색상
            }
        }
    },
      series: [{
        name: '공격 유형',
        colorByPoint: true,
        data: [
          { name: '공격-A', y: 11.2, color: '#F67C01' },
          { name: '공격-B', y: 18.3, color: '#1EA4FF' },
          { name: '공격-C', y: 23.2, color: '#48DAD2' },
          { name: '공격-D', y: 40.1, color: '#32B8FF' },
          { name: '공격-E', y: 7.3, color: '#20B2AA' }
        ]
      }]
};


onMounted(() => {
  Highcharts.chart('container_13', chartOptions);
});
</script>

<style scoped>
/* 스타일 추가 */
</style>
  