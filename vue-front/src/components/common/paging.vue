<template>
    <nav v-if="totalPage > 0" aria-label="Page navigation">
        <ul id="ulPage" class="pagination" style="cursor: pointer;">
            <!-- Previous Button -->
            <li class="paginate_button page-item previous" :class="{ disabled: current === 1 }">
                <a class="page-link" href="#" @click.prevent="changePage(1)">
                    <i class="fa fa-chevron-left"></i>
                </a>
            </li>
    
            <!-- Page Numbers -->
            <li
                v-for="i in pageNumbers"
                :key="i"
                class="paginate_button page-item"
                :class="{ active: current === i }"
            >
                <a class="page-link" href="#" @click.prevent="changePage(i)">
                    {{ i }}
                </a>
            </li>
    
            <!-- Next Button -->
            <li class="paginate_button page-item next" :class="{ disabled: current === totalPage }">
                <a class="page-link" href="#" @click.prevent="changePage(totalPage)">
                    <i class="fa fa-chevron-right"></i>
                </a>
            </li>
        </ul>

        
    </nav>
</template>
  
<script setup lang="ts">
import { ref, computed } from 'vue';

const props = defineProps({
    current: {
        type: Number,
        required: true
    },
    totalPage: {
        type: Number,
        required: true
    }
});

const changePage = (page) => {
    if (page > 0 && page <= props.totalPage) {
        console.log('Page changed to:', page);
    }
};

const pageNumbers = computed(() => {
    const totalPages = props.totalPage;
    const currentPage = props.current;
    let pages = [];

    if (totalPages <= 10) {
        for (let i = 1; i <= totalPages; i++) {
            pages.push(i);
        }
    } else {
        const start = Math.max(1, currentPage - 5);
        const end = Math.min(totalPages, currentPage + 4);

        if (currentPage > 6) {
            pages.push('...');
        }

        for (let i = start; i <= end; i++) {
            pages.push(i);
        }

        if (currentPage < totalPages - 5) {
            pages.push('...');
        }
    }

    return pages;
});
</script>

<style scoped>

</style>
  