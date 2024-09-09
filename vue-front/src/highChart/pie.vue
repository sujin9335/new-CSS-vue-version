<template>


    <highcharts :options="pieChart"></highcharts>

</template>
<script lang="ts">
import axios from "@/axios";
import { ref, onMounted, watch, computed } from 'vue';


export default {
    setup() {

        const chartData = ref([{ name: '서울', y: 120 },
            { name: '부산', y: 110 },
            { name: '대구', y: 80 },
            { name: '인천', y: 95 }]);
        const drillData = ref([]);
        
        // const groupUserData = async () => {
        //     try {
        //         const result = await axios.post("/a/highChart/vPie", {

        //         });

        //         const userData = result.data.data;
        //         // console.log(userData);
        //         if (result.status === 200) {

        //             console.log("파이데이터");
        //             console.log(userData);
        //             chartData.value = userData;
        //         }
        //     } catch (error) {
        //         alert("파이 데이터 통신에러");
        //     }
        // };

        // const pieDrillData = async () => {
        //     try {
        //         const result = await axios.post("/a/highChart/vPieDrill", {

        //         });

        //         const axiosData = result.data;
        //         // console.log(userData);
        //         if (result.status === 200) {

        //             console.log("파이 드릴데이터");
        //             console.log(drillData);

        //             const resultData = [];

        //             Object.entries(axiosData).forEach(function (entry) {
        //                 var key = entry[0];   // 첫 번째 요소는 키
        //                 var value = entry[1]; // 두 번째 요소는 값

        //                 var obj = {
        //                     name: "사용자",
        //                     id: key,
        //                     data: value
        //                 }

        //                 resultData.push(obj);

        //             });
        //             console.log(resultData);
        //             drillData.value = resultData;
        //             // drillData.value = ;
                    
        //         }
        //     } catch (error) {
        //         alert("파이 드릴 데이터 통신에러");
        //     }
        // };

        const pieChart = computed(() => ({
            
            chart: {
                    backgroundColor: 'transparent',
                    type: 'pie'
                },
                credits: {
                    enabled: false  // 우측 하단 로고 제거
                },
                title: {
                    text: '그룹별 사용자 비율'
                },
                tooltip: {
                    valueSuffix: '명'
                },

                plotOptions: {
                    series: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: [{
                            enabled: true,
                            distance: 20
                        }, {
                            enabled: true,
                            distance: -40,
                            // format: '{point.percentage:.1f}%',
                            formatter: function () {
                                // console.log(this)
                                if (this.point.drilldown) {
                                    return this.point.percentage.toFixed(1)+"%";
                                } else {
                                    return "ID: "+this.key;
                                }
                            },
                            style: {
                                fontSize: '1em',
                                textOutline: 'none',
                                opacity: 0.7
                            }
                        }]
                    }
                },
                exporting: {
                    enabled: false // 우측 상단 메뉴를 비활성화합니다
                },
                series: [
                    {
                        name: '유저',
                        colorByPoint: true,
                        data: chartData.value
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
        }));

        onMounted(() => {
            // groupUserData();
            // pieDrillData();
        });

        return {
            pieChart
        }

    },

};
</script>
<style lang="">
</style>