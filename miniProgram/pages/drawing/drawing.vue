<template>
	<view>
		<view class="container" v-show="store.getters.disguiseStatus">
		  <LoadingDataComponent :isLoading="isLoading"/>
		  <TopComponent :prompt="argument.prompt"
		                :workflowsCategoryData="workflowsCategoryData"
		                :workflowsCategoryId="argument.workflowsCategoryId"
		                @queryByCategory="handleGetWorkflowsPageByCategory"
		                @queryByPrompt="handleGetWorkflowsPageByPrompt"
		  />
		  <scroll-view v-if="workflowsData.length>0" class="scroll-view" scroll-y="true"
		               @scrolltolower="handleGetWorkflowsPage">
		    <WorkflowsComponent :workflowsData="workflowsData"/>
		  </scroll-view>
		  <EmptyDataComponent v-else :height="94" msg="没有找到创意数据"/>
		</view>
		<DrawingDisguiseComponent v-show="!(store.getters.disguiseStatus)"/>
	</view>

</template>

<script setup>
import TopComponent from "/pages/drawing/component/topComponent.vue";
import WorkflowsComponent from "/pages/drawing/component/workflowsComponent.vue";

import {onLoad} from '@dcloudio/uni-app'
import {ref} from 'vue'
import {reqGetWorkflowsCategoryList, reqGetWorkflowsPage} from "/api/drawingWorkflows";
import LoadingDataComponent from "../../components/loadingDataComponent.vue";
import EmptyDataComponent from "../../components/emptyDataComponent.vue";
import store from "@/store";
import DrawingDisguiseComponent from "@/components/disguise/drawingDisguiseComponent.vue";

const isLoading = ref(false)

const argument = ref({
  pageNum: 1,
  prompt: '',
  workflowsCategoryId: ''
})

const workflowsData = ref([])

const workflowsCategoryData = ref([])

const handleGetWorkflowsPageByPrompt = (prompt) => {
  if (argument.value.prompt === prompt) {
    console.log('yes')
    argument.value.prompt = ''
  } else {
    argument.value.prompt = prompt
  }
  argument.value.pageNum = 1
  workflowsData.value = []
  handleGetWorkflowsPage()
  console.log(argument.value.prompt)
}


const handleGetWorkflowsPageByCategory = (workflowsCategoryId) => {
  if (argument.value.workflowsCategoryId === workflowsCategoryId) {
    argument.value.workflowsCategoryId = ''
  } else {
    argument.value.workflowsCategoryId = workflowsCategoryId
  }
  argument.value.pageNum = 1
  workflowsData.value = []
  handleGetWorkflowsPage()
}

const handleGetWorkflowsPage = async () => {
  if (!isLoading.value) {
    try {
      isLoading.value = true
      const {data} = await reqGetWorkflowsPage(argument.value);
      if (data.records && data.records.length > 0) {
        argument.value.pageNum = argument.value.pageNum + 1

        workflowsData.value.push(...data.records);
      } else {
        uni.showToast({
          icon: 'none',
          duration: 1500,
          title: '已经拉到底~'
        });
      }
    } catch (e) {
      console.log(e)
    } finally {
      setTimeout(() => {
        isLoading.value = false
      }, 500)
    }
  }
}

const handleGetWorkflowsCategoryList = async () => {
  try {
    const {data} = await reqGetWorkflowsCategoryList();
    workflowsCategoryData.value = data
  } catch (e) {
    console.log(e)
  }
}


onLoad(() => {
  handleGetWorkflowsPage()
  handleGetWorkflowsCategoryList()
})

</script>

<style lang="scss" scoped>
.container {
  padding: 30rpx;
}

.scroll-view {
  height: 71vh;
  margin-top: 25vh
}
</style>
