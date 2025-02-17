<template>
  <BackPreviousComponent :bar-height="barHeight" :bar-top="barTop"/>
  <view class="container">
    <view>
      <view class="card">
        <view class="card-image">
          <view class="progress">
            {{ drawingStatus.progress ? drawingStatus.progress : '正在等待加入队列中' }}
          </view>
        </view>
      </view>
      <view class="tips">
        <view>离开当前页面创作不会受到任何影响</view>
        <view>可在个人中心查看任务进度</view>
      </view>
    </view>
    <SubmitButtonComponent title="完成后提醒我" @onClick="handleSubscription"/>
  </view>
</template>
<script setup>
import SubmitButtonComponent from "../../../components/submitButtonComponent.vue";
import {onLoad} from '@dcloudio/uni-app'
import {ref, onUnmounted} from 'vue'

import env from "../../../env";
import BackPreviousComponent from "../../../components/backPreviousComponent.vue";
import store from "@/store";
import {reqGetTaskProgress} from "@/api/drawingTask";

const barTop = ref(0)

const barHeight = ref(0)

const drawingStatus = ref({
  progress: '',
  workflowsWorksId: undefined,
})

const timer = ref(null)

const isInvoke = ref(false)

const handleGetDrawingProgress = async (taskId) => {
  timer.value = setInterval(async () => {
    if (!isInvoke.value) {
      isInvoke.value = true
      try {
        let {data} = await reqGetTaskProgress(taskId);
        const {progress, workflowsWorksId, status,location} = data
        if (status === 'WAIT') {
          drawingStatus.value.progress = '任务正在列队中,当前排在' + location + "位"
          return
        }
        if (status === 'BUILD') {
          if (progress === 99) {
            drawingStatus.value.progress = '正在进行敏感检测'
          } else {
            drawingStatus.value.progress = progress + '%'
          }
          return
        }
        if (status === "SUCCEED") {
          clearInterval(timer.value);
          uni.$emit("WORKFLOWS_WORKS:REFRESH")
          uni.redirectTo({
            url: '/pages/drawing/view/drawingPreviewView?workflowsWorksId=' + workflowsWorksId
          })
          return
        }
        if (status === "FAILED") {
          uni.showToast({
            title: '服务貌似出了点问题,请稍后再试',
            icon: 'none',
            duration: 2000
          })
          await store.dispatch('fetchUserInfo')
          clearInterval(timer.value);
          setTimeout(() => {
            uni.navigateBack()
          }, 1000)
        }
      } catch (e) {
        clearInterval(timer.value);
        uni.showToast({icon: 'none', duration: 1000, title: e.msg});
        uni.navigateBack()
      } finally {
        isInvoke.value = false
      }
    }
  }, 2000);
}

const handleSubscription = async () => {

  let tmplIds = env.tmplIds;
  uni.requestSubscribeMessage({
    tmplIds: tmplIds,
    success: async function (res) {
      if (res[tmplIds[0]] === 'accept') {
        uni.showToast({
          title: '作品生成完成后会以微信通知方式发送给您~',
          icon: 'none',
          duration: 2000
        })
      } else {
        uni.showModal({
          title: '订阅消息',
          content: '您当前拒绝接收当前作品生成消息通知，是否去开启?',
          confirmText: '开启授权',
          confirmColor: '#345391',
          cancelText: '仍然拒绝',
          cancelColor: '#999999',
          success: res => {
            if (res.confirm) {
              uni.openSetting({
                success(res) {
                  console.log(res)
                },
              })
            }
          }
        })
      }
    }
  })
}


onLoad((option) => {
  let menuButtonBoundingClientRect = uni.getMenuButtonBoundingClientRect();
  barTop.value = menuButtonBoundingClientRect.top
  barHeight.value = menuButtonBoundingClientRect.height
  handleGetDrawingProgress(option.taskId)
})

onUnmounted(() => {
  clearInterval(timer.value)
})
</script>


<style scoped lang="scss">

.container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center
}

.tips {
  text-align: center;
  margin-top: 30rpx;
  font-size: 26rpx
}

.tips > view:first-child {
  color: #818181;
  padding-top: 30rpx
}

.tips > view:last-child {
  color: #afafaf;
  padding-top: 20rpx
}

.progress {
  color: white;
  font-size: 40rpx
}

.card {
  background: linear-gradient(120deg, rgb(243, 99, 101) 1%, rgb(162, 89, 244) 10.31%, rgb(248, 71, 149) 26.24%, rgb(184, 54, 189) 97.8%);;
  animation: gradient 1s ease infinite;
  background-size: 200% 200%;
  padding: 5rpx;
  border-radius: 25rpx;
}

.card-image {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 700rpx;
  height: 600rpx;
  border-radius: 25rpx;
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
