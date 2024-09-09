<template>
    <div
      v-for="(box, index) in boxes"
      :key="index"
      ref="box"
      class="resizable-movable-box"
      :style="{ left: `${box.position.x}px`, top: `${box.position.y}px`, width: `${box.size.width}px`, height: `${box.size.height}px` }"
    >
      <div class="resizer bottom-right" @mousedown.stop.prevent="startResize(index, $event)"></div>
      <p>Resize me {{ index + 1 }}!</p>
    </div>
  </template>
  
  <script>
  import { ref, onMounted, onUnmounted } from 'vue';
  
  export default {
    setup() {
      const boxes = ref([
        { position: { x: 50, y: 50 }, size: { width: 150, height: 150 } },
        { position: { x: 250, y: 100 }, size: { width: 150, height: 150 } },
        { position: { x: 450, y: 150 }, size: { width: 150, height: 150 } },
      ]);
      
      const resizingBoxIndex = ref(null);
      const isResizing = ref(false);
      const startX = ref(0);
      const startY = ref(0);
      const startWidth = ref(0);
      const startHeight = ref(0);
  
      const startResize = (index, event) => {
        resizingBoxIndex.value = index;
        startX.value = event.clientX;
        startY.value = event.clientY;
        startWidth.value = boxes.value[index].size.width;
        startHeight.value = boxes.value[index].size.height;
        isResizing.value = true;
      };
  
      const resize = (event) => {
        if (!isResizing.value || resizingBoxIndex.value === null) return;
  
        const dx = event.clientX - startX.value;
        const dy = event.clientY - startY.value;
  
        const currentBox = boxes.value[resizingBoxIndex.value];
        currentBox.size.width = startWidth.value + dx;
        currentBox.size.height = startHeight.value + dy;
      };
  
      const stopResize = () => {
        isResizing.value = false;
        resizingBoxIndex.value = null;
      };
  
      onMounted(() => {
        document.addEventListener('mousemove', resize);
        document.addEventListener('mouseup', stopResize);
      });
  
      onUnmounted(() => {
        document.removeEventListener('mousemove', resize);
        document.removeEventListener('mouseup', stopResize);
      });
  
      return {
        boxes,
        startResize,
      };
    },
  };
  </script>
  
  <style scoped>
  .resizable-movable-box {
    position: absolute;
    background-color: lightcoral;
    border: 2px solid red;
    padding: 10px;
    box-sizing: border-box;
    display: flex;
    align-items: center;
    justify-content: center;
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
  </style>
  