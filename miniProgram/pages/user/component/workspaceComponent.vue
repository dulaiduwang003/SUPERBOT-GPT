<template>
  <view class="workspace">
    <view @click="handleChangeWorkspaceActive(0)">
      <view :class="workspaceActive===0?'workspace-view-active':'workspace-view-default'">作品集</view>
      <view :class="workspaceActive===0?'workspace-underline':''"/>
    </view>
    <view @click="handleChangeWorkspaceActive(1)">
      <view :class="workspaceActive===1?'workspace-view-active':'workspace-view-default'">最近任务</view>
      <view :class="workspaceActive===1?'workspace-underline':''"/>
    </view>
  </view>

  <view class="workspace-container" @touchend="handleTouchWorkspaceEnd" @touchstart="handleTouchWorkspaceStart">
    <view v-show="workspaceActive===0">
      <custom-waterfalls-flow v-if="props.workflowsWorksData.length>0" ref="workflowsWorksRef"
                              :value="props.workflowsWorksData"
                              @imageClick="handleToPreviewPage"
      >
        <view v-for="(item,index) in props.workflowsWorksData" :key="index" slot="slot{{index}}">
          <view class="waterfalls-flow">
            <view v-if="item.resultType==='VIDEO'">
              视频
            </view>
            <view v-if="item.resultType==='MODEL'">
              模型
            </view>
            <view v-if="item.resultType==='AUDIO'">
              音频
            </view>
            <view v-if="item.resultType==='IMAGE'">
              图像
            </view>
          </view>
        </view>
      </custom-waterfalls-flow>
      <EmptyDataComponent v-else :height="50" msg="没有绘制过任何作品"/>
    </view>
    <view v-show="workspaceActive===1">
      <view v-if="drawingTaskData.length>0">
        <view v-for="(item,index) in drawingTaskData" :key="index">
          <view v-if="item.status==='SUCCEED'" class="task-container">
            <view v-if="item.resultType==='MODEL'||item.resultType==='AUDIO'">
              <view class="padding-task-image-view">
                <image :src="item.url"
                       mode="aspectFill"/>
              </view>
            </view>
            <view v-else>
              <image :src="item.url" class="task-image"
                     mode="aspectFill"/>
            </view>
            <view class="task-flex">
              <view class="task-title">{{ item.workflowsName }}</view>
              <view v-if="item.resultType==='VIDEO'" class="task-label">视频</view>
              <view v-if="item.resultType==='IMAGE'" class="task-label">图片</view>
              <view v-if="item.resultType==='MODEL'" class="task-label">模型</view>
              <view v-if="item.resultType==='AUDIO'" class="task-label">音频</view>
              <view class="task-date">
                <view>创建于{{ conversionTime(new Date(item.createTime)) }}</view>
                <view class="task-btn" @click="handleToPreviewPage(item)">
                  预览
                </view>
              </view>
            </view>
          </view>
          <view v-if="item.status==='WAIT'" class="task-container">
            <view>
              <view class="task-image wait">
                列队中
              </view>
            </view>
            <view class="task-flex">
              <view class="task-title">{{ item.workflowsName }}</view>
              <view class="task-label">当前任务排在{{ item.location }}位</view>
              <view class="task-date">
                <view>创建于{{ conversionTime(new Date(item.createTime)) }}</view>
              </view>
            </view>
          </view>
          <view v-if="item.status==='BUILD'" class="task-container">
            <view>
              <view class="task-image build">
                {{ item.progress }}%
              </view>
            </view>
            <view class="task-flex">
              <view class="task-title">{{ item.workflowsName }}</view>
              <view class="task-label">构建中</view>
              <view class="task-date">
                <view>创建于{{ conversionTime(new Date(item.createTime)) }}</view>
              </view>
            </view>
          </view>
          <view v-if="item.status==='FAILED'" class="task-container">
            <view>
              <image class="task-image" mode="aspectFill" src="/static/svg/errorImage.svg"/>
            </view>
            <view class="task-flex">
              <view class="task-title">{{ item.workflowsName }}</view>
              <view class="task-label">构建失败</view>
              <view class="task-date">
                <view>创建于{{ conversionTime(new Date(item.createTime)) }}</view>
              </view>
            </view>
          </view>
        </view>
      </view>
      <EmptyDataComponent v-else :height="50" msg="单个任务会保留24小时"/>
    </view>
  </view>
</template>

<script setup>
import {defineEmits, defineExpose, defineProps, ref} from 'vue'
import {conversionTime} from "@/utils/date";
import EmptyDataComponent from "@/components/emptyDataComponent.vue";

const workflowsWorksRef = ref(null)

const props = defineProps({
  workspaceActive: {
    type: Number
  },
  workflowsWorksData: {
    type: Array
  },
  drawingTaskData: {
    type: Array
  }
});

const emits = defineEmits(['changeWorkspaceActive']);


const tabSlide = ref({
  startTime: 0,
  startX: 0,
  startY: 0
})

const handleChangeWorkspaceActive = (active) => {
  emits('changeWorkspaceActive', active)
}

const handleToPreviewPage = (e) => {
  uni.navigateTo({
    url: '/pages/drawing/view/drawingPreviewView?workflowsWorksId=' + e.workflowsWorksId
  })
}

const handleTouchWorkspaceStart = (e) => {
  tabSlide.value.startTime = Date.now()
  let changedTouch = e.changedTouches[0];
  tabSlide.value.startX = changedTouch.clientX
  tabSlide.value.startY = changedTouch.clientY
}

const handleTouchWorkspaceEnd = (e) => {
  let endTime = Date.now();
  let changedTouch = e.changedTouches[0];
  let clientX = changedTouch.clientX;
  let clientY = changedTouch.clientY;
  if (endTime - tabSlide.value.startTime > 1000) {
    return
  }
  if (Math.abs(clientX - tabSlide.value.startX) > 10 && Math.abs(clientY - tabSlide.value.startY) < 10) {
    if (clientX - tabSlide.value.startX > 0) {
      if (props.workspaceActive !== 0) {
        emits('changeWorkspaceActive', 0)
      }

    } else {
      if (props.workspaceActive !== 1) {
        emits('changeWorkspaceActive', 1)
      }

    }
  }
}

const handleWorkflowsWorksRefresh = () => {
  if (workflowsWorksRef.value) {
    workflowsWorksRef.value.refresh()
  }
}

defineExpose(
    {handleWorkflowsWorksRefresh}
)


</script>


<style lang="scss" scoped>
.workspace {
  padding: 30rpx;
  display: flex;
  font-weight: bold;
  font-size: 25rpx
}

.build {
  background: linear-gradient(120deg, rgb(239, 95, 97) 1%, rgb(186, 42, 107) 66.24%, rgb(184, 54, 189) 97.8%);;
  animation: gradient 4s ease infinite;
  background-size: 200% 200%;
  font-size: 26rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white
}

.waterfalls-flow {
  background-color: #161817;
  padding-top: 10rpx;
  padding-bottom: 10rpx;
  display: flex
}

.waterfalls-flow > view {

  border: 1rpx solid #555555;
  border-radius: 10rpx;
  color: whitesmoke;
  font-weight: bold;
  padding: 8rpx 8rpx;
  font-size: 20rpx
}

.wait {
  font-size: 26rpx;
  background: #161817;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white
}

.task-flex {
  padding-left: 20rpx;
  flex: 1
}

.task-title {
  color: whitesmoke;
  font-size: 30rpx;
  font-weight: bold
}

.task-label {
  color: #989898;
  font-size: 25rpx;
  padding-top: 20rpx
}

.task-date {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  color: #989898;
  font-size: 25rpx;
  padding-top: 20rpx
}

.task-btn {
  font-weight: bold;
  background: #333335;
  font-size: 27rpx;
  border-radius: 13rpx;
  padding: 8rpx 25rpx;
  color: #bababa
}

.task-container {
  background: #242426;
  width: 100%;
  border-radius: 20rpx;
  display: flex;
  align-items: flex-start;
  padding: 10rpx;
  margin-bottom: 20rpx;
}

.task-image {
  border-radius: 10rpx;
  width: 150rpx;
  height: 150rpx;
  object-fit: cover
}

.padding-task-image-view {
  width: 150rpx;
  height: 150rpx;
  display: flex;
  align-items: center;
  justify-content: center;

}


.padding-task-image-view > image {

  width: 120rpx;
  height: 120rpx;
  object-fit: cover
}

.workspace-view-default {
  color: #909090;
  transition: color 0.5s ease;
}

.workspace-view-active {
  color: #ffffff;
  transition: color 0.5s ease;
}


.workspace > view {
  margin-right: 30rpx;
  transition: background 0.5s ease;

}

.workspace-underline {
  margin: 0 20rpx;
  border-bottom: 5rpx solid #fedbe2;
  transition: border-bottom 0.5s ease;
  padding-top: 10rpx
}

.workspace-container {
  padding: 5rpx 30rpx 30rpx;
  min-height: 50vh;
}

.workspace-container > view {
  width: 100%;
  height: 200rpx;
  animation: fadeIn 1s ease-in-out forwards;
}

@keyframes gradient {
  0% {
    background-position: 0 12%;
  }

  50% {
    background-position: 100% 100%;
  }

  100% {
    background-position: 0 12%;
  }
}
</style>
