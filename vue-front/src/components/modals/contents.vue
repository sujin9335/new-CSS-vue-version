<template>
    <!-- <div v-if="isVisible" @click.self="closeModal"> -->
    <div>
        <div class="modal show blur-background" id="composition" data-bs-backdrop="static" data-bs-keyboard="true"
         aria-labelledby="staticBackdropLabel" aria-hidden="true" style="display: block;">
            <div class="modal-dialog modal-dialog-centered modal-custom">
                <div class="modal-content">

                    <div class="modal-body">
                        <div class="container" id="main">
                            <div class="row">
                                <div class=" text-end">
                                        <button id="norBtn" type="button" class="btn btn-success" @click="closeModal">닫기</button>
                                </div>
                                <div class="col-5 me-5 border hi-700" id="menu">
                                    <div class="my-2">구성 리스트</div>
                                    <span v-if="monitorStore.monitorList?.length > 0">총: {{ monitorStore.monitorList?.length }} 개</span>
                                    <div class="border hi-500 p-2 scroll" >
                                        <table class="table text-center" id="userTab">
                                            <thead>
                                                <tr>
                                                    <th class="col-2" scope="col">
                                                        <input
                                                            @change="toggleAllCheckboxes"
                                                            v-model="isAllChecked"
                                                            class="form-check-input" type="checkbox" 
                                                            id="checkboxAll">
                                                    </th>
                                                    <th class="col-5" scope="col">타이틀</th>
                                                    <th class="col-5" scope="col">콘텐츠</th>
                                                </tr>
                                            </thead>
                                            <tbody class="scroll">
                                                <tr v-for="(monitor, index) in monitorStore.monitorList" 
                                                    :key="monitor.monitor_id"
                                                    :class="[{
                                                        'table-active': monitor.monitor_id === selectedMonitorId
                                                    }]"
                                                    style="cursor: pointer;"
                                                >
                                                    <td>
                                                        <input 
                                                            v-model="checkedItems[index]"
                                                            @change="checkIfAllSelected"
                                                            class="form-check-input" type="checkbox"
                                                            id="checkbox">
                                                        <input type="hidden" :value="monitor.monitor_id">
                                                    </td>
                                                    <td @click="selectMonitor(monitor)">{{ monitor.monitor_title }}</td>
                                                    <td @click="selectMonitor(monitor)">{{ monitor.monitor_content }}차트</td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="text-end m-2">
                                        <button 
                                        @click="delMonitors"
                                        type="button" class="btn btn-secondary btn-sm">삭제</button>
                                    </div>
                                </div>
                                <div class="col border hi-700" id="content">
                                    <!-- 메뉴 -->
                                    
                                    <!-- 내용 -->
                                    <div class=" m-2">
                                        구성정보
                                            
                                            <div class="row row-cols-2">
                                                <input v-model="selectedMonitorId" type="hidden" id="id">
                                                <div class="col-2 my-2">콘텐츠</div>
                                                <div class="col-10 my-2">
                                                    <select v-model="typeSelect">
                                                        <option value="bar">bar차트</option>
                                                        <option value="pie">pie차트</option>
                                                        <option value="line">line차트</option>
                                                    </select>
                                                </div>
                                                <div class="col-2">타이틀</div>
                                                <div class="col-10 mb-2">
                                                    <input 
                                                        v-model="titleInput"
                                                        class="form-control" type="text">
                                                </div>
                                                <!-- <div class="col-2">데이터</div>
                                                <div class="col-10">
                                                    <textarea class="form-control" rows="17"></textarea>
                                                    <input type="hidden" id="id">
                                                </div> -->
                                            </div>
                                            <div class="text-end m-2">
                                                <button 
                                                    type="button"
                                                    class="btn btn-secondary btn-sm" 
                                                    @click="upsertMonitor"
                                                    >등록</button>
                                            </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script setup lang="ts">
    import { ref, defineEmits, onMounted } from 'vue';
    import { useMonitorStore } from '@/stores/monitor';

    const monitorStore = useMonitorStore();

    const titleInput = ref('');
    const typeSelect = ref('bar');
    const selectedMonitorId = ref('');

    const isAllChecked = ref(false);//체크박스 모두선택상태
    const checkedItems = ref(Array(monitorStore.monitorList.length).fill(false));

    const emit = defineEmits(['close']); // 부모 컴포넌트의 @close이벤트 설정

    const closeModal = () => {
        emit('close');
    };

    // 체크박스 전체 선택/해제 함수
    const toggleAllCheckboxes = () => {
        checkedItems.value = checkedItems.value.map(() => isAllChecked.value);
    };

    // 개별 체크박스를 변경할 때 모든 체크박스가 선택되었는지 확인하는 함수
    function checkIfAllSelected() {
        isAllChecked.value = checkedItems.value.every(checked => checked);
    }

    // 선택된 모니터 삭제
    const delMonitors = async () => {
        //삭제경고
        if (!confirm('선택한 모니터를 삭제하시겠습니까?')) {
            return;
        }

        //선택된 모니터 배열로 id저장
        const selectedMonitorIds = checkedItems.value.map((checked, index) => {
            if (checked) {
                return monitorStore.monitorList[index].monitor_id;
            }
        }).filter(id => id);

        console.log(selectedMonitorIds);

        await monitorStore.monitorDelete(selectedMonitorIds);
    };

    const monitorList = async () => { // 모니터링 리스트 가져오기
        await monitorStore.getMonitorList();
    };

    const upsertMonitor = async () => { // 모니터링 추가
        if (!/^.{1,30}$/.test(titleInput.value)) {
            alert('타이틀은 1~30 글자 입니다');
            return;
        }

        

        const addMonitor = {
            monitor_id: selectedMonitorId.value,
            monitor_title: titleInput.value,
            monitor_content: typeSelect.value,
        };

        if(selectedMonitorId.value !== '') {
            await monitorStore.monitorUpdate(addMonitor);
        }else { 
            await monitorStore.monitorInsert(addMonitor);
        }

        selectedMonitorId.value = '';
        titleInput.value = '';

        console.log(addMonitor);
        await monitorList();
        

    };  

    const selectMonitor = (monitor: any) => { // 모니터 선택
        selectedMonitorId.value = monitor.monitor_id;
        titleInput.value = monitor.monitor_title;
        typeSelect.value = monitor.monitor_content;
    };

    onMounted(monitorList); // 모달창이 열릴 때마다 모니터링 리스트를 가져옴

</script>
<style scope>
    .blur-background {
        background-color: rgba(128, 128, 128, 0.5); /* 회색 배경색 */
    }

    select {
        width: 100%;
    }

    table-active {
        background-color: #f8d7da;
    }
</style>