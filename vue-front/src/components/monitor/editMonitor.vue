<template>
    <div
        ref="draggableDiv"
        :style="divStyle"
        :class="[{
                    'monitor-on-edit' : editor && !isIdCheck,
                    'draw-border' : editor && !isIdCheck,
                    'other-elements-selector' : !isIdCheck,
                    'effect-7' : isIdCheck,
                }]" 
        @mousedown="onMouseDown"
    >
        <chart 
            style="width: 100%; height: 100%;"
            :data="props.data" 
        />
        <div v-if="isIdCheck" class="resizer right" @mousedown="onResizeMouseDown('right', $event)"></div>
        <div v-if="isIdCheck" class="resizer bottom" @mousedown="onResizeMouseDown('bottom', $event)"></div>
        <div v-if="isIdCheck" class="resizer all" @mousedown="onResizeMouseDown('all', $event)"></div>
    </div>
</template>

<script setup lang="ts">
    import chart from '@/components/chart/highChart.vue';
    import { defineProps, defineEmits, ref, onMounted, watch } from 'vue';
    import { useMonitorStore } from '@/stores/monitor';

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

    const props = defineProps<{ editor: boolean; data: Monitor; selectedMonitor: string | null; setStyle: any;}> ();
    const emit = defineEmits<{ (event: 'update', style: any): void }>();

    const draggableDiv = ref<HTMLDivElement | null>(null);
    const userMonitorStore = useMonitorStore();
    const moveWidth = ref(0);

    const divStyle = ref({
        position: 'absolute',
        left: `${props.data.monitor_x}px`,
        top: `${props.data.monitor_y}px`,
        width: `${props.data.monitor_width}px`,
        height: `${props.data.monitor_hight}px`,
        cursor: props.editor ? 'pointer' : '',
    });

    const isIdCheck = ref(false);
    const startX = ref(0);
    const startY = ref(0);
    const startLeft = ref(0);
    const startTop = ref(0);
    const startWidth = ref(0);
    const startHeight = ref(0);
    const isResizing = ref(false);
    const snapDistance = 10;

    const onMouseDown = (event: MouseEvent) => {
        if (!draggableDiv.value || !props.editor || !isIdCheck.value) return; 
        startX.value = event.clientX;
        startY.value = event.clientY;
        startLeft.value = draggableDiv.value.offsetLeft;
        startTop.value = draggableDiv.value.offsetTop;

        document.addEventListener('mousemove', onMouseMove);
        document.addEventListener('mouseup', onMouseUp);
    };

    const onMouseMove = (event: MouseEvent) => {
        if (!draggableDiv.value) return;
        if (isResizing.value) return;

        const dx = event.clientX - startX.value;
        const dy = event.clientY - startY.value;

        let newLeft = startLeft.value + dx;
        let newTop = startTop.value + dy;

        const parentRect = draggableDiv.value.parentElement?.getBoundingClientRect();
        const elements = document.querySelectorAll('.other-elements-selector');

        elements.forEach((element) => {
            const rect = element.getBoundingClientRect();

            if (Math.abs(newLeft - (rect.right - (parentRect?.left || 0))) <= snapDistance) {
                newLeft = rect.right - (parentRect?.left || 0);
            }
            if (Math.abs(newLeft + draggableDiv.value!.offsetWidth - (rect.left - (parentRect?.left || 0))) <= snapDistance) {
                newLeft = rect.left - (parentRect?.left || 0) - draggableDiv.value!.offsetWidth;
            }
            if (Math.abs(newTop - (rect.bottom - (parentRect?.top || 0))) <= snapDistance) {
                newTop = rect.bottom - (parentRect?.top || 0);
            }
            if (Math.abs(newTop + draggableDiv.value!.offsetHeight - (rect.top - (parentRect?.top || 0))) <= snapDistance) {
                newTop = rect.top - (parentRect?.top || 0) - draggableDiv.value!.offsetHeight;
            }
        });

        divStyle.value = {
            ...divStyle.value,
            left: `${newLeft}px`,
            top: `${newTop}px`
        };
    };

    const onMouseUp = () => {
        document.removeEventListener('mousemove', onMouseMove);
        document.removeEventListener('mouseup', onMouseUp);
        isResizing.value = false;
    };

    const onResizeMouseDown = (direction: string, event: MouseEvent) => {
        event.stopPropagation();
        isResizing.value = true;

        startX.value = event.clientX;
        startY.value = event.clientY;
        startWidth.value = draggableDiv.value ? draggableDiv.value.clientWidth : 0;
        startHeight.value = draggableDiv.value ? draggableDiv.value.clientHeight : 0;

        const onResizeMouseMove = (event: MouseEvent) => {
            if (!draggableDiv.value) return;

            const dx = event.clientX - startX.value;
            const dy = event.clientY - startY.value;

            let newWidth = startWidth.value;
            let newHeight = startHeight.value;

            if (direction === 'right' || direction === 'all') {
                newWidth += dx;
            }

            if (direction === 'bottom' || direction === 'all') {
                newHeight += dy;
            }

            const parentRect = draggableDiv.value.parentElement?.getBoundingClientRect();
            const elements = document.querySelectorAll('.other-elements-selector');

            elements.forEach((element) => {
                const rect = element.getBoundingClientRect();

                if (direction === 'right' || direction === 'all') {
                    if (Math.abs(draggableDiv.value!.offsetLeft + newWidth - (rect.left - (parentRect?.left || 0))) <= snapDistance) {
                        newWidth = rect.left - (parentRect?.left || 0) - draggableDiv.value!.offsetLeft;
                    }
                    if (Math.abs(draggableDiv.value!.offsetLeft + newWidth - (rect.right - (parentRect?.left || 0))) <= snapDistance) {
                        newWidth = rect.right - (parentRect?.left || 0) - draggableDiv.value!.offsetLeft;
                    }
                }

                if (direction === 'bottom' || direction === 'all') {
                    if (Math.abs(draggableDiv.value!.offsetTop + newHeight - (rect.top - (parentRect?.top || 0))) <= snapDistance) {
                        newHeight = rect.top - (parentRect?.top || 0) - draggableDiv.value!.offsetTop;
                    }
                    if (Math.abs(draggableDiv.value!.offsetTop + newHeight - (rect.bottom - (parentRect?.top || 0))) <= snapDistance) {
                        newHeight = rect.bottom - (parentRect?.top || 0) - draggableDiv.value!.offsetTop;
                    }
                }
            });

            divStyle.value.width = `${newWidth}px`;
            divStyle.value.height = `${newHeight}px`;

            moveWidth.value = newWidth;
        };

        const onResizeMouseUp = () => {
            document.removeEventListener('mousemove', onResizeMouseMove);
            document.removeEventListener('mouseup', onResizeMouseUp);
            isResizing.value = false;
        };

        document.addEventListener('mousemove', onResizeMouseMove);
        document.addEventListener('mouseup', onResizeMouseUp);
    };

    watch(() => props.selectedMonitor, (newSelectedMonitor) => {
        isIdCheck.value = props.selectedMonitor == props.data.monitor_id;
    });

    watch(() => props.setStyle, (newSetStyle) => {
        console.log(newSetStyle);    
        if (newSetStyle.id == props.data.monitor_id) {
            divStyle.value = {
                ...divStyle.value,
                left: `${newSetStyle.x}px`,
                top: `${newSetStyle.y}px`,
                width: `${newSetStyle.width}px`,
                height: `${newSetStyle.height}px`
            };
        }
    });

    watch(() => [divStyle.value, isIdCheck.value], ([newStyle, isSelected]) => {
        if (isIdCheck.value) {
            const newDivStyle = {
                id: props.data.monitor_id,
                x: newStyle.left.replace('px', ''),
                y: newStyle.top.replace('px', ''),
                width: newStyle.width.replace('px', ''),
                height: newStyle.height.replace('px', '')
            };
            emit('update', newDivStyle);
        }
    }, { deep: true });

    watch(() => props.editor, async (newVal, oldVal) => {
        if (!newVal) {
            const style = window.getComputedStyle(draggableDiv.value);
            const id = props.data.monitor_id;
            const x = parseInt(style.left, 10);
            const y = parseInt(style.top, 10);
            const width = parseInt(style.width, 10);
            const height = parseInt(style.height, 10);
            const monitorData = {
                "id": id,
                "x": x, 
                "y": y, 
                "width": width, 
                "height": height 
            };
            console.log(`x: ${x}, y: ${y}, width: ${width}, height: ${height}`);
            await userMonitorStore.monitorCoordinateUpdate(monitorData);
        }
    });
</script>

<style scoped>
.defaultBorder {
    border: 2px solid transparent;
    box-sizing: border-box;
}

.editMode {
    border: 2px dashed blue;
    cursor: pointer;
    box-sizing: border-box;
}

.editMode:hover {
    box-shadow: 10px 10px 15px rgba(0, 0, 0, 0.5);
}

.resizer {
    position: absolute;
    width: 10px;
    height: 10px;
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

.monitor-on-edit {
    border: 1px dashed #ccc !important;
}

.monitor-on-edit:hover {
    border: 0 !important;
}
</style>