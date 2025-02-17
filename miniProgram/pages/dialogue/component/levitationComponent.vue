<script setup>

import {getTokenValue} from "@/store/token";
import {defineEmits} from 'vue'

const emits = defineEmits(['new-chat-topic']);
const handleNewChat = () => {
  emits('new-chat-topic')
}


const toTopicPage = () =>{
  if (getTokenValue()){
    uni.navigateTo({
      url: '/pages/dialogue/view/chatTopicView',
      animationType: 'pop-in',
      animationDuration: 200
    })
  }else {
    uni.navigateTo({
      url: '/pages/user/user',
      animationType: 'pop-in',
      animationDuration: 200
    })
  }
}

</script>

<template>
  <view class="container">
    <view>
      <image src="/static/icon/newChat.svg" @click="handleNewChat" @longpress="toTopicPage"/>
    </view>
  </view>
</template>


<style scoped>
.container {
  position: fixed;
  right: -60rpx;
  top: 60vh;
  z-index: 2
}

.container > view {
  border: 3rpx solid #464646;
  padding-left: 15rpx;
  height: 90rpx;
  width: 140rpx;
  background:#1d1d1d;
  border-radius: 100rpx;
  display: flex;
  justify-content: flex-start;
  align-items: center
}

.container image {
  width: 60rpx;
  height: 60rpx;
}

</style>
