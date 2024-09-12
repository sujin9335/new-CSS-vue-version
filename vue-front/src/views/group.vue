<template>
    <div>
        <div class="container-fluid" style="height: 1111px;">
            <div class="breadcrumb">
                <div class="breadcrumb-item">
                    <i class="fa fa-home"></i> /
                </div> 
                그룹관리
            </div>
           
            <!-- 그룹 트리 -->
            <div class="row mt10" style="height: 700px;">
                <div class="col-3">
                    <div class="card-box">
                        <div class="right_top"><i class="fa fa-window-restore"></i></div>
                        <h4 class="header-title"><i class="fa fa-list"></i>그룹</h4>
                        <div class="mt10 text-left">
                            <div class="col-12 tac">
                                그룹 트리
                            </div>
                            <div class="col-12 tac">
                                
                                <div class="fr">
                                    <button 
                                        @click="isGetModal = true"
                                        class="btn btn-primary mt20" icon="ADD">
                                        <i class="fa fa-plus"></i> 그룹 등록
                                    </button>
                                    <button 
                                        @click="isUpsertModal = true"
                                        class="btn btn-primary mt20" icon="ADD">
                                        <i class="fa fa-plus"></i> 그룹 삭제
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 그룹 유저 -->
                <div class="col-9">
                    <div class="card-box">
                        <div class="right_top"><i class="fa fa-window-restore"></i></div>
                        <h4 class="header-title"><i class="fa fa-list"></i>그룹 유저</h4>
                        <div class="mt10 text-left">
                            <div class="row">
                                <div class="col-12 mb10">
                                    <div class="search-box">
                                        <div class="row form_width_80">
                                            <div class="col-1 border-right">
                                                <div class="search_title">검색 조건</div>
                                            </div>
                                            <div class="col-2">
                                                <div>
                                                    <select v-model="searchType" class="form-control">
                                                        <option value="board_title">제목</option>
                                                        <option value="board_content">내용</option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="col-6">
                                                <div>
                                                    <input v-model="searchText" type="text" class="form-control form_round" autocomplete="off">
                                                </div>
                                            </div>
                                            <div class="col-3">
                                                <input type="hidden" id="s_group_cd">
                                                
                                                <div>
                                                    <button class="btn btn-secondary mr-lg-4 fr">
                                                        <i class="fa fa-rotate-left"></i> 초기화
                                                    </button>
                                                    <button class="btn btn-primary ml fr">
                                                        <i class="fa fa-search"></i> 검색
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <table class="scrollTbody table table-dark table-hover" >
                                <thead>
                                    <tr>
                                        <th class="tac" width="10%" style="text-align: center">
                                            번호
                                        </th>
                                        <th class="tac" width="30%" style="text-align: center">
                                            제목
                                        </th>
                                        <th class="tac" width="10%" style="text-align: center">
                                            첨부파일
                                        </th>
                                        <th class="tac" width="20%" style="text-align: center">
                                            작성자
                                        </th>
                                        <th class="tac" style="text-align: center">
                                            조회수
                                        </th>
                                        <th class="tac"  style="text-align: center">
                                            날짜
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-if="boardList && boardList.length === 0">
                                        <td colspan="6" style="text-align: center;">게시글이 존재하지 않습니다</td>
                                    </tr>
                                    <template v-else>
                                        <tr v-for="(list, index) in boardList" 
                                            :key="list.board_id">
                                            <td class="tac">
                                                {{ ((pageCurrent - 1) * limitPage) +1  + index}}
                                            </td>
                                            <td class="tac">
                                                {{ list.board_title }}
                                            </td>
                                            <td class="tac">
                                                {{ list.file_count }}
                                            </td>
                                            <td class="tac">
                                                {{ list.user_nickname }}
                                            </td>
                                            <td class="tac">
                                                {{ list.board_view }}
                                            </td>
                                            <td class="tac">
                                                {{ list.board_date }}
                                            </td>
                                        </tr>
                                    </template>
                                </tbody>
                            </table>
                            <div class="col-12 tac">
                                <ul id="ulPage" class="pagination" style="cursor: pointer;">
                                    <!-- v-if -->
                                    <li class="paginate_button page-item previous disable">
                                        <a class="page-link"><i class="fa fa-chevron-left"></i></a>
                                    </li>
                                    <li class="paginate_button page-item active">
                                        <a class="page-link">1</a>
                                    </li>
                                    <li class="paginate_button page-item">
                                        <a class="page-link">2</a>
                                    </li>
                                    <li class="paginate_button page-item next">
                                        <a class="page-link"><i class="fa fa-chevron-right"></i></a>
                                    </li>
                                    <!-- v-if -->
                                </ul>
                                <div class="fr">
                                    <button 
                                        @click="isGetModal = true"
                                        class="btn btn-primary mt20" icon="ADD">
                                        <i class="fa fa-plus"></i> test상세
                                    </button>
                                    <button 
                                        @click="isUpsertModal = true"
                                        class="btn btn-primary mt20" icon="ADD">
                                        <i class="fa fa-plus"></i> 게시글 작성
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
        <upsertModal v-model:show="isUpsertModal" modalId="upsertModal"/>
        <getModal v-model:show="isGetModal" modalId="getModal"/>
    </div>
</template>
<script setup lang="ts">
import { ref, defineProps, computed, onMounted} from 'vue';
import axios from "@/axios";
import upsertModal from '@/components/modals/boardUpsert.vue';
import getModal from '@/components/modals/boardGet.vue';

interface Board { //게시판 리스트 데이터
    board_id: string;
    board_title: string;
    user_id: string;
    board_date: string;
    board_view: number;
    user_nickname: string;
    time: string;
    file_count: number;
}
const boardList = ref<Board[]>();//보드 리스트데이터
const totalCount = ref<number>(0);//총 데이터 개수

const searchSaveText = ref('');//검색어 저장
const searchSaveType = ref('');//검색어 타입 저장

const searchType = ref('board_title');//검색타입
const searchText = ref('');//검색어
const pageCurrent = ref<number>(1);//현재페이지
const limitPage = ref<number>(5);//한페이지에 보여줄 데이터 개수

//모달쪽
const isUpsertModal = ref(false);//등록 수정 모달
const isGetModal = ref(false);//상세보기 모달

//리스트 요청
const list = async (search : boolean) => {
    if(search){
        searchSaveText.value = searchText.value;
        searchSaveType.value = searchType.value;
    }

    const param = {
        offset: Number(limitPage.value) * (pageCurrent.value - 1), //현재 보고있는 페이지
        listSize: Number(limitPage.value), // 가져올 데이터의 개수
        searchType: searchSaveType.value,
        search: searchSaveText.value
    };

    console.log(param);


    try {
        const result = await axios.post(
            '/a/board/vlist',
            {param}
        );

        const boardData = result.data;

        if (result.status === 200) {
            boardList.value = boardData.data;
            totalCount.value = boardData.total;
            
        }
    } catch (error) {
        alert("보드 리스트 통신에러");
    }
}

const consoleTest = () => {
    console.log(boardList.value);
    console.log(totalCount.value);
}

onMounted(() => {
    list(true);

});

//상세 요청

//



</script>
<style scop>


</style>