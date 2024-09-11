<template>
    <div>
        <div class="container-fluid" style="height: 1111px;">
            <div class="breadcrumb">
                <div class="breadcrumb-item">
                    <i class="fa fa-home"></i> /
                </div> 
                구성
            </div>
            <div style="position: relative;">
                <editMonitor v-for="monitor in monitorList" 
                    :key="monitor.monitor_id"
                    :data="monitor" 
                    :editor="isEditMode"
                    :selectedMonitor="selectedMonitor"
                    :setStyle="setStyle"
                    
                    @click="selectMonitor(monitor)"
                    @update="updateStyle"
                />
            </div>
            <div 
                :style="{ display: selectedMonitor !== null ? 'block' : 'none' }"
                class="controll_box3" style="padding: 10px;">
                <div class="controll_item pl0 pr0">
                    <div class="right"></div>
                    <br>
                    <div class="right" style="font-size: 30px;">
                        <i class="bi bi-grip-vertical"></i>
                        <!-- <div class="mb10"></div> -->
                    </div>
                </div>
                <div class="controll_item">
                    <div class="left">높이</div>
                    <div class="right">
                        <input type="text" 
                        v-model.number="divHeight"
                        @input="inputStyle"
                        style="width: 75px;"> px
                    </div>
                    <br>
                    <div class="left">넓이</div>
                    <div class="right">
                        <input type="text" 
                        v-model.number="divWidth"
                        @input="inputStyle"
                        style="width: 75px;"> px
                    </div>
                </div>
                <div class="controll_item">
                    <div class="left">X 축</div>
                    <div class="right">
                        <input type="text" 
                        v-model.number="divX"
                        @input="inputStyle"
                        style="width: 75px;"> px
                    </div>
                    <br>
                    <div class="left">Y 축</div>
                    <div class="right">
                        <input type="text" 
                        v-model.number="divY"
                        @input="inputStyle"
                        style="width: 75px;"> px
                    </div>
                </div>
                <div class="controll_item">
                    <div class="right"></div>
                    <br>
                    <div class="right">
                        <button class="btn btn-primary btn-sm fl ml10 mb20">구성 삭제</button>
                    </div>
                </div>
            </div>
        </div>
    </div>


</template>
<script setup lang="ts">
    import { onMounted, ref, watch } from "vue";
    import { useMonitorStore } from '@/stores/monitor';
    import editMonitor from '@/components/monitor/editMonitor.vue';

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

    const monitorStore = useMonitorStore();
    const monitorList = ref<Monitor[]>([]);//모니터 리스트 받아올 변수

    //모니터 정보
    const divId = ref('');
    const divWidth = ref(0);
    const divHeight = ref(0);
    const divX = ref(0);    
    const divY = ref(0);

    //인풋입력 설정할 스타일
    const setStyle = ref();

    const isEditMode = ref(monitorStore.isEdit); //에디터모드 on off

    const selectedMonitor = ref<string | null>(null);//선택 모니터 (테두리 표시)

    const selectMonitor = (monitor: Monitor) => { //선택된 모니터 입력
        if (!isEditMode.value) return;
        selectedMonitor.value = monitor.monitor_id;
    };

    //input 태그 입력시 실행
    const inputStyle = () => {
        const style = {
            id: divId.value,
            width: divWidth.value,
            height: divHeight.value,
            x: divX.value,
            y: divY.value,
        };

        setStyle.value = style;

    };

    const delMonitor = async () => {
        if (!confirm('선택한 모니터를 삭제하시겠습니까?')) {
            return;
        }

        await monitorStore.monitorDelete([selectedMonitor.value]);
        selectedMonitor.value = null;
    };




    //가로 길이 변경
    const updateStyle = (newStyle: any) => {
        console.log(newStyle);
        divId.value = newStyle.id;
        divWidth.value = newStyle.width;
        divHeight.value = newStyle.height;
        divX.value = newStyle.x;
        divY.value = newStyle.y;

    };

    //모니터 리스트 받아오기
    onMounted( async () => {
        // const monitorList = 
        await monitorStore.getMonitorList();
        monitorList.value = monitorStore.monitorList;
        console.log('mounted');
    });

    //모니터 리스트 변경시 감지
    watch(
        () => monitorStore.monitorList, 
        (newValue) => {
            monitorList.value = newValue;
            
        }
    );

    watch(
        () => monitorStore.isEdit, 
        (newValue) => {
            isEditMode.value = newValue;
            if (!isEditMode.value) {
                selectedMonitor.value = null;
            }
        }
    );


</script>
<style scoped>
     
    .controls {
        border : 1px solid black;
        position: absolute;
        bottom: 10px;
        left: 50%;
        transform: translateX(-50%);
        gap: 10px;
        background-color: rgba(128, 128, 128, 0.5);
        width: 30%
    }

    input {
        width: 100%;
    }



</style>