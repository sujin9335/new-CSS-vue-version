<template>
    <div>
        <div>
            <h4 class="header-title_dash">
                <div class="head_tit_name">{{title}}</div>
            </h4>
        </div>
        <div :id="id" style="width: 100%; height: 80%;"></div>

    </div>
</template>

<script setup lang="ts">
import Highcharts from 'highcharts';
import { ref, defineProps, computed, onMounted} from 'vue';
import HighchartsVue from 'highcharts-vue';
import Highcharts3D from 'highcharts/highcharts-3d'

Highcharts3D(Highcharts)

interface Monitor {
    monitor_id: string;
    monitor_content: string;
    monitor_title: string;
    monitor_x: number;
    monitor_y: number;
    monitor_width: number;
    monitor_hight: number;
    monitor_date: string;
}

const props = defineProps<{data: Monitor; }> ();

const id = ref(props.data.monitor_id);
const title = ref(props.data.monitor_title);
const content = ref(props.data.monitor_content);

const chartData = ref([{ name: '서울', y: 120 },
                    { name: '부산', y: 110 },
                    { name: '대구', y: 80 },
                    { name: '인천', y: 95 }]);
const drillData = ref([]);


const chartOptions: Highcharts.Options = {
            
    chart: {
        backgroundColor: 'transparent',
        type: content.value,
        options3d: {
          enabled: true, // 3D 활성화
          alpha: content.value == 'pie' ? 45 : 5,
          beta: 0
        }
        
    },
    credits: {
        enabled: false  // 우측 하단 로고 제거
    },
    title: {
        text: ''
    },
    tooltip: {
        valueSuffix: '명'
    },
    // x축 라벨 표시
    xAxis: {
        type: 'category',
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
            text: '',
            style: {
                color: '#000' // y축 제목 색상
            }
        }
    },
    legend: {
        itemStyle: {
            color: '#fff' // 시리즈 이름 색상
        }
    },

    plotOptions: {
        pie: {
            innerSize: 30,
            depth: 40
        },
        series: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
                enabled: true,
                format: content.value == 'pie' ? '{point.name}: {point.y}명 ({point.percentage:.1f}%)' : '{point.name}: {point.y}명', 
                style: {
                    color: '#fff',
                    fontSize: '13px',
                    fontWeight: 'bold'
                }
            }
        }
    },
    exporting: {
        enabled: false // 우측 상단 메뉴를 비활성화합니다
    },
    series: [
        {
            name: '유저',
            colorByPoint: true,
            data: chartData.value,
            
        }
    ],
    drilldown: {
        breadcrumbs: {
            position: {
                align: 'center'
            }
        },
        series: drillData.value
    }
};

onMounted(() => {
    Highcharts.chart(id.value, chartOptions);
});

</script>

<style scoped>
.chart-wrapper {
    flex: 1; /* 차트가 가용 공간을 모두 차지하도록 설정 */
    display: flex;
    align-items: stretch;
}

.chart-container {
    height: 100%;
    display: flex;
    flex-direction: column;
}
</style>