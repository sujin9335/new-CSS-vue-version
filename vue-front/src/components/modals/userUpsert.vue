<template>
    <!-- <div v-if="isVisible" @click.self="closeModal"> -->
    <div :id="modalId" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="full-width-modalLabel" aria-hidden="true" >
        <div class="modal-dialog modal-lg" >
            <div class="modal-content" >
                <div class="modal-header">
                    <h4 class="modal-title" id="full-width-modalLabel">유저 등록</h4>
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true" @click="modalClose">×</button>
                </div>
                <div class="modal-body">
                    <div class="mt10">
                        <div class="item">
                        
                            <div class="form-group form-setup row">
                                <label class="col-3 col-form-label">아이디</label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control">
                                </div>
                                <div class="col-sm-4">
                                    <button type="button" class="btn btn-success">중복확인</button>
                                </div>
                            </div>
                            <div class="form-group form-setup row">
                                <label class="col-3 col-form-label">닉네임</label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control">
                                </div>
                                <div class="col-sm-4">
                                    <button type="button" class="btn btn-success">중복확인</button>
                                </div>
                            </div>
                        
                            <div class="form-group form-setup row">
                                <label class="col-3 col-form-label">비밀번호</label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control">
                                    <input type="text" class="form-control mt3">
                                </div>
                                <div class="col-sm-4">
                                    <div>비밀번호 확인 문구</div>
                                </div>
                                
                            </div>
                            <div class="form-group form-setup row">
                                <label class="col-3 col-form-label">이름</label>
                                <div class="col-sm-4">
                                    <input type="text" class="form-control">
                                </div>
                            </div>
                            
                            <div class="form-group form-setup row">
                                <label class="col-3 col-form-label">핸드폰 번호</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control">
                                </div>
                            </div>
                            <div class="form-group form-setup row">
                                <label class="col-3 col-form-label">이메일</label>
                                <div class="col-sm-6">
                                    <input type="text" class="form-control">
                                </div>
                            </div>
                            <div class="form-group form-setup row">
                                <label class="col-3 col-form-label">권한</label>
                                <div class="col-sm-4">
                                    <select 
                                        class="form-control">
                                        <option value="bar">일반</option>
                                        <option value="pie">관리자</option>
                                    </select>
                                </div>
                            </div>
                            
                            
                        </div><!--//item-->
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary waves-effect" data-dismiss="modal" @click="modalClose">닫기</button>
                    <button @click="upsertMonitor" type="button" class="btn btn-primary waves-effect waves-light">저장</button>
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

    const props = defineProps<{ show: Boolean; modalId: String; }>();
    ;

    const emit =  defineEmits<{ (event: 'update:show', value: boolean): void; }>();
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

    const modalClose = () => {
        emit('update:show', false); // 부모 컴포넌트에 모달이 닫혔음을 알림
    };

    onMounted(() => {
        const modalEl = document.getElementById(props.modalId);
            if (modalEl) {
                modalInstance.value = new Modal(modalEl);
                modalEl.addEventListener('hide.bs.modal', () => { //모달창 닫힐 때 이벤트(hide.bs.modal 가 모달창 닫힐 때 발생하는 이벤트)
                    emit('update:show', false);
                });
                open(props.show);
            }
    });

    watch(() => props.show, (newValue) => { //모달창 열림 상태 변경
        open(newValue);
    });

    watch(() => monitorStore.monitorList.length, (newLength) => { //체크박스 배열 길이 변경
        checkedItems.value = Array(newLength).fill(false);
    }, { immediate: true });

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