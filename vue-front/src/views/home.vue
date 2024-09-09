<template>
    <div class="container text-center">
        <div class="row">
            <div class="col">
                <h2>home</h2>
            </div>
        </div>
    </div>
    
    <div class="container" id="main">
        <highcharts :options="lineChartOptions"></highcharts>
        <highcharts :options="barChartOptions"></highcharts>
        <pieChart />
    </div>
</template>
<script lang="ts">
import axios from "@/axios";
import pieChart from "@/highChart/pie.vue";


export default {
    components: {
        pieChart
    },
    data() {
        return {

            lineChartOptions: { //라인 차트
                chart: {
                    type: 'line'
                },
                credits: {
                    enabled: false, // 우측 하단 로고 제거
                },

                title: {
                    text: "게시글 날짜별 등록 갯수",
                },
                tooltip: {
                    valueSuffix: "개",
                },

                yAxis: {
                    title: {
                        text: "게시글 수",
                        align: "high",
                        offset: 0,
                        rotation: 0,
                        y: -10,
                    },
                },
                xAxis: {
                    type: "category",
                },
                legend: {
                    enabled: false, // 범례를 숨깁니다 (맨밑 데이터 설명)
                },

                exporting: {
                    enabled: false, // 우측 상단 메뉴를 비활성화합니다
                },
                series: [

                ],
                plotOptions: {
                    series: {
                        cursor: 'pointer',
                        className: 'popup-on-click',
                        marker: {
                            lineWidth: 1
                        },
                        point: {
                            events: {
                                click: this.lineClickEvent
                            },
                            allowPointSelect: true // Clickable points option
                        }
                    }
                }
            },
            barChartOptions: { //바 차트
                
                credits: {
                    enabled: false  // 우측 하단 로고 제거
                },
                chart: {
                    type: 'column'

                },
                title: {
                    text: '게시글 날짜별 등록 갯수'
                },
                tooltip: {
                    valueSuffix: '개',
                    xDateFormat: '%Y-%m-%d'
                },
                xAxis: {
                    type: 'category'
                },
                yAxis: {
                    title: {
                        text: '게시글 수',
                        align: 'high',
                        offset: 0,
                        rotation: 0,
                        y: -10
                    }
                },

                exporting: {
                    enabled: false // 우측 상단 메뉴를 비활성화합니다
                },

                legend: {
                    enabled: false // 범례를 숨깁니다 (맨밑 데이터 설명)
                },

                series: [

                ],
                drilldown: {
                    breadcrumbs: {
                        position: {
                            align: 'right'
                        }
                    },
                    series: this.barDrill
                }
            },
        };
    },
    methods: {
        async processData() {
            try {
                const result = await axios.post("/a/highChart/vLine", {});

                const checkIdInfo = result.data;

                if (result.status === 200) {
                    // console.log(result.data);
                    const lineChartData = [];
                    const barChartData = [];
                    for (let i = 0; i < checkIdInfo.length; i++) {
                        var lineChartDataObj = {};
                        var barChartDataObj = {};
                        lineChartDataObj = { name: checkIdInfo[i].total_date, y: checkIdInfo[i].total };
                        barChartDataObj = { name: checkIdInfo[i].total_date, y: checkIdInfo[i].total, drilldown: checkIdInfo[i].total_date };

                        lineChartData.push(lineChartDataObj);
                        barChartData.push(barChartDataObj);
                        
                    }

                    // return chartData;

                    this.lineChartOptions.series = [ 
                        {
                            name: '게시글',
                            data: lineChartData 
                        }
                    ];
                    
                    this.barChartOptions.series = [ 
                        {
                            name: '게시글',
                            data: barChartData 
                        }
                    ];
                    
                }
            } catch (error) {
                alert("라인 데이터 통신에러");
            }
        },
        async clickData(name : string) {
            // alert(name);
            try {
                const result = await axios.post("/a/highChart/vLineClick", {
                    date: name
                });

                const clickData = result.data;

                if (result.status === 200) {
                    // console.log("클릭데이터");
                    // console.log(result.data);
                    return clickData;
                    
                }
            } catch (error) {
                alert("라인 데이터 통신에러");
            }
        },
        async lineClickEvent(event) {
            if (event.point.series.options.className.indexOf('popup-on-click') !== -1) {
                const data = await this.clickData(event.point.name);

                let print = "";
                if (data.length > 0) {
                    data.forEach((item) => {
                        print += "ID: " + item.name + ", 게시글수: " + item.y + "<br/>";
                    });
                } else {
                    print = "게시글 없음";
                }
                // console.log(print);

                const chart = event.point.series.chart;
                const text = print; // 출력될 내용

                const anchorX = event.point.plotX + event.point.series.xAxis.pos;
                const anchorY = event.point.plotY + event.point.series.yAxis.pos;
                const align = anchorX < chart.chartWidth - 200 ? 'left' : 'right';
                const x = align === 'left' ? anchorX + 10 : anchorX - 10;
                const y = anchorY - 30;

                if (!chart.sticky) {
                    chart.sticky = chart.renderer
                        .label(text, x, y, 'callout', anchorX, anchorY)
                        .attr({
                            align,
                            fill: 'rgba(0, 0, 0, 0.75)',
                            padding: 10,
                            zIndex: 7 // Above series, below tooltip
                        })
                        .css({
                            color: 'white'
                        })
                        .on('click', () => {
                            if (chart.sticky) {
                                chart.sticky.destroy();
                                chart.sticky = null;
                            }
                        })
                        .add();
                } else {
                    chart.sticky
                        .attr({ align, text })
                        .animate({ anchorX, anchorY, x, y }, { duration: 250 });
                }
            }
        },
        async barDrillData() {
            try {
                const result = await axios.post("/a/highChart/vBarDrill", {
                    
                });

                const DrillData = result.data;

                if (result.status === 200) {
                    // console.log("바드릴데이터");
                    // console.log(result.data);

                    const chartData = []

                    Object.entries(DrillData).forEach(function (entry) {
                        var key = entry[0];   // 첫 번째 요소는 키
                        var value = entry[1]; // 두 번째 요소는 값

                        var obj = {
                            name: key,
                            id: key,
                            data: value
                        }

                        chartData.push(obj);

                    });


                    this.barChartOptions.drilldown.series = chartData;

                    
                }
            } catch (error) {
                alert("드릴 데이터 통신에러");
            }
        },
    },
    async mounted() {

        this.processData();
        this.barDrillData();
    }
};
</script>
<style lang="">
</style>