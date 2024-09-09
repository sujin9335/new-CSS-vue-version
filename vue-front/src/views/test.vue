<template>
    <div id="app">
      <h2>Dynamic Pie Charts</h2>
      <button @click="addChart">Add Pie Chart</button>
  
      <div v-for="(chart, index) in charts" :key="index" style="margin-top: 20px;">
        <div :id="'chart-' + index" style="height: 400px;"></div>
        <button @click="editChart(index)">Edit Chart Data</button>
      </div>
    </div>
  </template>
  
  <script>
  import { ref, nextTick } from 'vue';
  import Highcharts from 'highcharts';
  
  export default {
    name: 'App',
    setup() {
      const charts = ref([]);
      const chartInstances = ref([]);
  
      const addChart = () => {
        charts.value.push({
          title: `Pie Chart ${charts.value.length + 1}`,
          data: [
            { name: 'Apples', y: 30 },
            { name: 'Oranges', y: 20 },
            { name: 'Bananas', y: 50 }
          ]
        });
  
        nextTick(() => {
          const chartIndex = charts.value.length - 1;
          renderChart(chartIndex);
        });
      };
  
      const renderChart = (index) => {
        const chart = charts.value[index];
        const chartInstance = Highcharts.chart(`chart-${index}`, {
          chart: {
            type: 'bar'
          },
          title: {
            text: chart.title
          },
          series: [{
            name: 'Fruits',
            colorByPoint: true,
            data: chart.data
          }]
        });
        chartInstances.value[index] = chartInstance;
      };
  
      const editChart = (index) => {
        const newData = prompt('Enter new data as comma-separated values (e.g., Apples=40,Oranges=30,Bananas=30):');
        if (newData) {
          const parsedData = newData.split(',').map(item => {
            const [name, y] = item.split('=');
            return { name: name.trim(), y: parseFloat(y) };
          });
          charts.value[index].data = parsedData;
  
          // Update the chart with new data
          chartInstances.value[index].series[0].setData(parsedData);
        }
      };
  
      return {
        charts,
        addChart,
        editChart
      };
    }
  };
  </script>
  
  <style scoped>
  /* 스타일 추가 가능 */
  </style>
  