<template>
    <!-- <div v-if="isVisible" @click.self="closeModal"> -->
    <div :id="modalId" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="full-width-modalLabel" aria-hidden="true" >
        <div class="modal-dialog modal-lg" >
            <div class="modal-content" >
                <div class="modal-header">
                    <h4 class="modal-title" id="full-width-modalLabel">게시글 등록</h4>
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true" @click="modalClose">×</button>
                </div>
                <div class="modal-body">
                    <div class="mt10">
                        <div class="item">
                        
                            <div class="form-group form-setup row">
                                <label class="col-3 col-form-label">제목</label>
                                <div class="col-sm-9">
                                    <input type="text" class="form-control">
                                </div>
                            </div>
                        
                            <div class="form-group form-setup row">
                                <label class="col-3 col-form-label">내용</label>
                                <div class="col-sm-9">
                                    <input type="text" class="form-control">
                                </div>
                            </div>
                            <div class="form-group form-setup row">
                                <label class="col-3 col-form-label">파일</label>
                                <div class="col-sm-9">
                                    <!-- 파일추가 -->
                                    <div class="form_sub_tit fl pt5 pl10">파일추가</div>
                                    <form id="form1" class="fr">
                                        <div class="filebox bs3-primary preview-image fr files mr5">
                                            <input type="file" multiple hidden>
                                            <div class="btn btn-dark btn-hover btn-sm hover">
                                                <i class="fa fa-plus"></i>
                                            </div>
                                        </div>
                                    </form>
                                    <!-- 파일 목록 -->
                                    <table class="scrollTbody table table-dark table-hover" >
                                        <thead>
                                            <tr>
                                                
                                                <th class="tac" style="text-align: center">파일명</th>
                                                <th class="tac" style="text-align: center">삭제</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr
                                                
                                            >
                                                
                                                <td class="tac">파일명</td>
                                                <td class="tac" >
                                                    <button type="button" class="btn btn-dark btn-xs">삭제</button>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                            
                            
                        </div><!--//item-->
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary waves-effect" data-dismiss="modal" @click="modalClose">닫기</button>
                    <button  type="button" class="btn btn-primary waves-effect waves-light">저장</button>
                </div>
            </div>
        </div>
    </div>
</template>
<script setup lang="ts">
    import { ref, defineEmits, onMounted, defineProps, watch } from 'vue';
    import { Modal } from 'bootstrap';


    const titleInput = ref('');
    const typeSelect = ref('bar');
    const selectedMonitorId = ref('');


    const props = defineProps<{ show: Boolean; modalId: String; }>();
    ;

    const emit =  defineEmits<{ (event: 'update:show', value: boolean): void; }>();

    const modalInstance = ref<Modal | null>(null);

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

    onMounted(() => { //모달창 마운트
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