<template>
    <!-- <div v-if="isVisible" @click.self="closeModal"> -->
    <div :id="modalId" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="full-width-modalLabel" aria-hidden="true">
        <div class="modal-dialog modal-full">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="full-width-modalLabel">구성관리</h4>
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                </div>
                <div class="modal-body">
                    <div class="low">
                        <div class="col-5">
                            <div class="card-box">
                                <div class="right_top"><i class="fa fa-window-restore"></i><i class="fa fa-close"></i></div>
                                <h4 class="header-title"><i class="fa fa-list"></i>구성리스트</h4>
                                <div class="mt10 text-left">
                                    컨텐츠 안에 들어가는 카드 박스
                                </div>
                            </div>
                        </div>
                        <div class="col-7">
                            <div class="card-box">
                                <div class="right_top"><i class="fa fa-window-restore"></i><i class="fa fa-close"></i></div>
                                <h4 class="header-title"><i class="fa fa-list"></i>구성정보</h4>
                                <div class="mt10 text-left">
                                    컨텐츠 안에 들어가는 카드 박스
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary waves-effect" data-dismiss="modal">닫기</button>
                    <button type="button" class="btn btn-primary waves-effect waves-light">저장</button>
                </div>
            </div>
        </div>
    </div>
</template>
<script setup lang="ts">
    import { ref, defineEmits, onMounted, defineProps, watch } from 'vue';
    import { useMonitorStore } from '@/stores/monitor';
    import { Modal } from 'bootstrap';

    const monitorStore = useMonitorStore();

    const titleInput = ref('');
    const typeSelect = ref('bar');
    const selectedMonitorId = ref('');

    const isAllChecked = ref(false);//체크박스 모두선택상태
    const checkedItems = ref(Array(monitorStore.monitorList.length).fill(false));

    const props = defineProps({
        show: Boolean,
        modalId: String
    });

    const emit = defineEmits(['update:show']);
    const modalInstance = ref<Modal | null>(null);
    

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

    const open = (shouldShow: boolean) => { // 모달창 열기
        if (modalInstance.value) {
            if (shouldShow) {
            modalInstance.value.show();
            } else {
            modalInstance.value.hide();
            }
        }
    };

    onMounted(() => {
    const modalEl = document.getElementById(props.modalId);
        if (modalEl) {
            modalInstance.value = new Modal(modalEl);
            modalEl.addEventListener('hide.bs.modal', () => {
                emit('update:show', false);
            });
            open(props.show);
        }
    });

    watch(() => props.show, (newValue) => {
        open(newValue);
    });

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