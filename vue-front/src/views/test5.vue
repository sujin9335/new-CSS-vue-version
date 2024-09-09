<template>
    <div>
        <highcharts :options="barChart"></highcharts>
    </div>
</template>
  
  <script>
import { ref, onMounted, computed } from "vue";
import axios from 'axios';

export default {
    setup() {


        const rainfallData = ref([
            { name: '서울', y: 120 },
            { name: '부산', y: 110 },
            { name: '대구', y: 80 },
            { name: '인천', y: 95 },
        ]);

        const barChart = computed(() => ({
            chart: {
                type: 'bar'
            },
            title: {
                text: '한국 지역별 강수량'
            },
            xAxis: {
                categories: rainfallData.value.map(data => data.name),
                title: {
                text: '지역'
                }
            },
            yAxis: {
                title: {
                text: '강수량 (mm)'
                },
                min: 0
            },
            series: [
                {
                name: '강수량',
                data: rainfallData.value
                }
            ],
            plotOptions: {
                bar: {
                dataLabels: {
                    enabled: true
                }
                }
            }
        }));

        const rainData = async () => {
            try {
                const result = await axios.post("/a/highChart/vRain", {
                });
                if (result.status === 200) {
                    console.log(result);

                }
            } catch (error) {
                console.error('강수량 데이터:', error);
            }
        }

        
        
        onMounted(async () => {
            // // mapDataAxios();
            // loadMapData();
            rainData();
        });
        

        return {
            barChart
        };
    },
};
</script>

  
  <style scoped>
/* 스타일 추가 가능 */
</style>
  