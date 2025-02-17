<script setup>

import {onLoad} from '@dcloudio/uni-app'
import {ref} from "vue";
import LoadingPageComponent from "../../../components/loadingPageComponent.vue";
import BackPreviousComponent from "../../../components/backPreviousComponent.vue";
import SubmitButtonComponent from "../../../components/submitButtonComponent.vue";
import {reqGetUploadWorkflowsComponentFile, reqGetWorkflowsInterface} from "@/api/drawingWorkflows";
import {getTokenValue} from "@/store/token";
import {reqSubmitTask} from "@/api/drawingTask";
import store from "@/store";


const obj = ref({
  energy: undefined,
  containers: [],
  workflowsId: undefined,
  workflowsName: ''
})

const barTop = ref(0)

const barHeight = ref(0)

const isLoading = ref(true)

const handleGetWorkflowsInterface = async (id) => {
  try {
    isLoading.value = true
    const {data} = await reqGetWorkflowsInterface(id);
    obj.value = data
  } catch (e) {
    uni.showToast({icon: 'none', duration: 1000, title: e.msg});
    uni.navigateBack()
  } finally {
    setTimeout(() => {
      isLoading.value = false
    }, 500)
  }
}

const handleSubmitTask = async () => {
  for (let i = 0; i < obj.value.containers.length; i++) {
    const item = obj.value.containers[i];
    if (!item.nodeValue) {
      uni.showToast({
        title: item.tips,
        icon: 'none',
        duration: 4000
      });
      return;
    }
  }
  const map = obj.value.containers.map(c => {
    return {
      nodeDigital: c.nodeDigital,
      nodeKey: c.nodeKey,
      nodeValue: c.nodeValue,
    }
  });
  try {
    uni.showLoading({
      title: "正在提交中",
      mask: true,
    });
    const {data} = await reqSubmitTask({
      workflowsId: obj.value.workflowsId,
      containers: map
    });
    await store.dispatch('fetchUserInfo')
    uni.navigateTo({
      url: '/pages/drawing/view/drawingExecutionView?taskId=' + data + '&workflowsId=' + obj.value.workflowsId.value,
      animationType: 'pop-in',
      animationDuration: 200
    })
  } catch (e) {
    uni.showToast({
      icon: 'none',
      duration: 1500,
      title: e.msg
    });
  } finally {
    uni.hideLoading()
  }
}

const handleDeleteNodeData = (index) => {
  obj.value.containers[index].nodeValue = ''
  obj.value.containers[index].preview = ''
}

const handleUploadVideo = (index) => {
  uni.showActionSheet({
    itemList: ['相册中选择'],
    success: () => {
      uni.chooseVideo({
        count: 1,
        success: res => {
          if (res.size > 12 * 1024 * 1024) {
            uni.showToast({
              title: '视频超过了12MB,请重新选择',
              icon: 'none',
              duration: 4000
            })
            return;
          }
          upload(res.tempFilePath, index)
        },
        fail: {}
      })
    }
  })
}

const handleUploadImage = (index) => {
  uni.showActionSheet({
    itemList: ['相册中选择'],
    success: () => {
      uni.chooseImage({
        count: 1,
        success: res => {
          if (res.tempFiles[0].size > 4 * 1024 * 1024) { // 判断图片大小是否超过2MB
            uni.showToast({
              title: '图片超过了4MB,请重新选择',
              icon: 'none',
              duration: 4000
            })
            return;
          }
          let file = res.tempFiles[0].path;
          upload(file, index)
        },
        fail: {}
      })
    }
  })
}

const upload = (file, index) => {
  console.log(file)
  uni.showLoading({
    title: "正在上传中",
    mask: true,
  });
  uni.uploadFile({
    url: reqGetUploadWorkflowsComponentFile(),
    filePath: file,
    name: 'file',
    header: {
      'Authorization': 'Bearer ' + getTokenValue()
    },
    success(res) {
      const {data} = JSON.parse(res.data);
      obj.value.containers[index].nodeValue = data.fileName
      obj.value.containers[index].preview = data.preview
      uni.hideLoading()
    },
    fail(e) {
      console.log(e)
      uni.hideLoading()
      uni.showToast({
        title: '上传资源失败',
        icon: 'none',
        duration: 4000
      })
    }
  })
}

onLoad((option) => {
  let menuButtonBoundingClientRect = uni.getMenuButtonBoundingClientRect();
  barTop.value = menuButtonBoundingClientRect.top
  barHeight.value = menuButtonBoundingClientRect.height
  handleGetWorkflowsInterface(option.workflowsId)
})

</script>

<template>
  <BackPreviousComponent :bar-height="barHeight" :bar-top="barTop"/>
  <LoadingPageComponent v-if="isLoading"/>
  <!--  操作表单页面-->

  <view class="container" v-show="!isLoading">
    <view class="forms">
      <view v-for="(item,index) in obj.containers" :key=index>
        <!--      视频-->
        <view class="VideoUpload" v-if="item.type==='VIDEO_UPLOAD'">
          <view class="VideoUploadTips">
            <uni-icons type="videocam" size="35rpx" color="white"></uni-icons>
            <view>{{ item.tips }}</view>
          </view>
          <view class="picturePreview" v-if="item.nodeValue">
            <video :src="item.preview"/>
            <view @click="handleDeleteNodeData(index)">
              ×
            </view>
          </view>
          <view class="ImageUploadBtn" @click="handleUploadVideo(index)" v-else>
            上传视频
          </view>
        </view>
        <!--      图片-->
        <view class="ImageUpload" v-if="item.type==='IMAGE_UPLOAD'">
          <view class="ImageUploadTips">
            <uni-icons type="image" size="35rpx" color="white"></uni-icons>
            <view>{{ item.tips }}</view>
          </view>
          <view class="picturePreview" v-if="item.nodeValue">
            <image :src="item.preview" mode="aspectFill"/>
            <view @click="handleDeleteNodeData(index)">
              ×
            </view>
          </view>
          <view class="ImageUploadBtn" @click="handleUploadImage(index)" v-else>
            上传图片
          </view>
        </view>
        <!--      文本-->
        <view class="TextPrompt" v-if="item.type==='TEXT_PROMPT'">
          <view class="TextPromptTips">
            <uni-icons type="compose" size="35rpx" color="white"></uni-icons>
            <view>{{ item.tips }}</view>
          </view>
          <view>
            <input v-model="item.nodeValue" placeholder="请输入...." type="text" class="input"/>
          </view>
        </view>
      </view>
    </view>
    <SubmitButtonComponent :title="'快速生成 消耗'+obj.energy+'能量'" @onClick="handleSubmitTask"/>
  </view>
</template>


<style scoped lang="scss">
.btn {
  margin: 40rpx;
  background: linear-gradient(120deg, rgb(250, 52, 54) 1%, rgb(186, 42, 107) 66.24%, rgb(184, 54, 189) 97.8%);;
  animation: gradient 4s ease infinite;
  background-size: 200% 200%;
  padding: 30rpx;
  border-radius: 20rpx;
  color: white;

  text-align: center;
  font-size: 30rpx;

}

.container {
  padding-bottom: 80rpx
}

.input {
  background: #323232;
  border: 1rpx solid #2b2b2b;
  border-radius: 15rpx;
  margin-top: 20rpx;
  padding: 15rpx
}


.forms {
  padding: 40rpx 40rpx 10vh;
  padding-top: 10vh;
}

.TextPrompt {
  background: #232323;
  border-radius: 20rpx;
  color: white;
  padding: 20rpx;
  font-size: 28rpx;
  margin: 40rpx 0;
}

.VideoUpload {
  background: #232323;
  border-radius: 20rpx;
  color: white;
  padding: 20rpx;
  font-size: 28rpx;
  margin: 40rpx 0;
}

.ImageUpload {
  background: #232323;
  border-radius: 20rpx;
  color: white;
  padding: 20rpx;
  font-size: 28rpx;
  margin: 40rpx 0;
}

.VideoUploadTips {
  display: flex;
  align-items: center
}

.VideoUploadTips > view {
  padding-left: 10rpx
}

.TextPromptTips {
  display: flex;
  align-items: center
}

.TextPromptTips > view {
  padding-left: 10rpx
}

.ImageUploadTips {
  display: flex;
  align-items: center
}

.ImageUploadTips > view {
  padding-left: 10rpx
}

.ImageUploadBtn {
  background: #323232;
  border-radius: 20rpx;
  padding: 20rpx;
  margin-top: 20rpx;
  text-align: center
}

.picturePreview {
  padding-top: 20rpx;
  position: relative;
  width: 200rpx
}

.picturePreview image {
  border-radius: 20rpx;
  width: 200rpx;
  height: 200rpx;

}

.picturePreview video {
  border-radius: 20rpx;
  width: 200rpx;
  height: 200rpx
}

.picturePreview view {
  font-size: 40rpx;
  position: absolute;
  right: 5rpx;
  top: 25rpx;
  width: 60rpx;
  height: 60rpx;
  z-index: 2;
  background: rgba(30, 30, 30, 0.73);
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 100%
}


.lottie {
  display: flex;
  justify-content: center;
  align-items: center;
}

.lottie > view {
  padding-top: 15vh
}

.title {
  text-align: center;
  color: #919191;
  font-size: 33rpx
}
</style>

