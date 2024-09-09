<template>
    <div>
        <div>
            <div
                v-for="(box, index) in boxes"
                :key="index"
                :class="isEditMode ? editModeClass(index) : defaultClass"
                :style="{ 
                    top: box.y + 'px', 
                    left: box.x + 'px',
                    width: box.width + 'px',
                    height: box.height + 'px'
                }"
                @mousedown="isEditMode ? startDrag($event, index) : null"
                @mouseup="isEditMode ? selectTag($event, index) : null"
            >
                DIV {{ index + 1 }}
                <pie class="testChild" style="width: 100%; height: 100%;">
                </pie>
                <div 
                    :class="[{
                        'resizer' : isEditMode,
                        'all' : isEditMode,
                    }]" 
                    @mousedown.stop.prevent="isEditMode ? startResize(index, $event, 'all') : null"
                ></div>
                <div :class="[{
                        'resizer' : isEditMode,
                        'bottom' : isEditMode,
                    }]" 
                    @mousedown.stop.prevent="isEditMode ? startResize(index, $event, 'bottom') : null"></div>
                <div :class="[{
                        'resizer' : isEditMode,
                        'right' : isEditMode,
                    }]" 
                    @mousedown.stop.prevent="isEditMode ? startResize(index, $event, 'right') : null"></div>
            </div>
            <button @click="modeChange">모드 체인지</button>
        </div>
        <div>x좌표: {{ x }}</div>
        <div>y좌표: {{ y }}</div>
        <div>is: {{ isEditMode }}</div>
        <div>index: {{ selectIndex }}</div>
        <div>
            <button @click="toggleModal" class="btn btn-secondary py-2 mt-2">구성</button>
        </div>
        <!-- 모달 -->
        <!-- <modal :isVisible="isModal" @close="toggleModal"/> -->
        <modal v-if="isModal" @close="toggleModal"/>
    </div>

    
</template>
  
<script>
import axios from "@/axios";
import { ref, onMounted, onUnmounted } from "vue";
import pie from '@/highChart/pie.vue';
import modal from '@/components/modals/contents.vue';

export default {
    components: {
        pie,
        modal
    },
    setup() {
        //클래스
        const isModal = ref(false); //모달창 on off

        const selectClass = ref("draggable selectTagStyle");
        const editClass = ref("draggable editMode");
        const defaultClass = ref("draggable");

        const isEditMode = ref(false); //모드 변경
        const selectIndex = ref(null); //선택 인덱스
        
        //사이즈조절
        const resizingIndex = ref(null);
        const resizingType = ref(null);
        const startWidth = ref(0);
        const startHeight = ref(0);
        const startX = ref(0);
        const startY = ref(0);    

        const offset = ref({ x: 0, y: 0 }); ///좌표

        // 여러 개의 div의 초기 위치를 저장하는 배열
        const boxes = ref([
            { x: 50, y: 50 , width: 300, height: 300  },
            { x: 200, y: 200 , width: 300, height: 300  },
        ]);

        //테스트 마우스 좌표
        const x = ref(0);
        const y = ref(0);
        const updateMousePosition = (event) => {
            x.value = event.clientX;
            y.value = event.clientY;
        };

        //모달창on/off
        const toggleModal = () => {
            isModal.value = !isModal.value;
        }

        //클래스 변경
        const editModeClass = (index) => {
            if(index === selectIndex.value) {
                return selectClass.value;
            }else {
                return editClass.value; 
            }
        }

        //모드 변경 
        const modeChange = () => {
            isEditMode.value = !isEditMode.value;//모드 bool

            if(!isEditMode.value) { //모드 off시 인덱스 초기화
                selectIndex.value = null;
                document.removeEventListener("mouseup", selectTag);
            }
        };

        //태그 선택
        const selectTag = (event, index) => {
            selectIndex.value = index;
        };

        //드래그 시작
        const startDrag = (event, index) => {
            // isDragging.value = true;
            // selectIndex.value = index;
            if(index === selectIndex.value) {
                // 드래그 시작 시 마우스와 div 사이의 거리를 계산
                offset.value.x = event.clientX - boxes.value[index].x;
                offset.value.y = event.clientY - boxes.value[index].y;
                console.log(index);
                console.log(event.clientX + "-" + event.clientY);
                console.log(offset.value.x + "-" + offset.value.y);
                console.log("");
    
                document.addEventListener("mousemove", drag);
                document.addEventListener("mouseup", stopDrag);
            }

        };

        //드래그 중
        const drag = (event) => {
            if (isEditMode.value && selectIndex.value !== null) {
                boxes.value[selectIndex.value].x = event.clientX - offset.value.x;
                boxes.value[selectIndex.value].y = event.clientY - offset.value.y;
            }
        };

        //드래그 끝
        const stopDrag = () => {
            document.removeEventListener("mousemove", drag);
            document.removeEventListener("mouseup", stopDrag);
        };
        
        onMounted(() => {
            window.addEventListener('mousemove', updateMousePosition);

            testData();
        });

        onUnmounted(() => {
            
        });

        const startResize = (index, event, type) => {
            resizingType.value = type;
            resizingIndex.value = index;
            startX.value = event.clientX;
            startY.value = event.clientY;
            startWidth.value = boxes.value[index].width;
            startHeight.value = boxes.value[index].height;

            document.addEventListener('mousemove', resize);
            document.addEventListener('mouseup', stopResize);
        };

        const resize = (event) => {
    
            const dx = event.clientX - startX.value;
            const dy = event.clientY - startY.value;
    
            const currentBox = boxes.value[resizingIndex.value];

            if (resizingType.value === 'all') {
                currentBox.height = startHeight.value + dy;
                currentBox.width = startWidth.value + dx;

            } else if (resizingType.value === 'bottom') {
                currentBox.height = startHeight.value + dy;
            } else if (resizingType.value === 'right') {
                currentBox.width = startWidth.value + dx;
            }
            
        };
    
        const stopResize = () => {
            document.removeEventListener('mousemove', resize);
            document.removeEventListener('mouseup', stopResize);
            resizingIndex.value = null;
        };


        //테스트 데이터가져오기
        const testData = async () => {
            try {
                const result = await axios.post("/a/moveTest", {

                });

                const axiosData = result.data.result;
                // console.log(userData);
                if (result.status === 200) {

                    console.log("무브데이터");
                    console.log(axiosData);

                    // boxes.value = axiosData;
                }
            } catch (error) {
                alert("test 무브 데이터 통신에러");
            }
        };

        return {
            isModal,
            selectClass,
            editClass,
            defaultClass,
            isEditMode,
            
            boxes,
            x,
            y,
            toggleModal,
            editModeClass,
            modeChange,
            selectTag,
            startDrag,
            startResize,

        };
    },
};
</script>
  
  <style>
.draggable {
    position: absolute;
    /* width: 150px;
    height: 100px; */
    /* background-color: transparent; */
    
    user-select: none;
    margin: 20px;
    text-align: center;
    /* line-height: 100px; */
}

.selectTagStyle {
    border: 2px solid orangered;
    cursor: move;
}

.editMode {
    border: 2px solid blue;
    cursor: pointer;
}

.editMode:hover {
    box-shadow: 10px 10px 15px rgba(0, 0, 0, 0.5);
}

.resizer {
    position: absolute;
    width: 10px;
    height: 10px;
    background: #000;
    bottom: 0;
    right: 0;
    cursor: nwse-resize;
    
}

.resizer.all {
    bottom: -2px;
    right: -2px;
    z-index: 1;
}

.resizer.bottom {
    width: 100%;
    height: 4px;
    background: none;
    bottom: -2px;
    left: 0;
    cursor: ns-resize;
}

.resizer.right {
    width: 4px;
    height: 100%;
    background: none;
    top: 0;
    right: -2px;
    cursor: ew-resize;
}
</style>
  