<script setup>
import {defineProps} from 'vue';

const props = defineProps({
  isLoading: {
    type: Boolean,
    default: true
  },
  top: {
    type: Number,
    default: 80
  }
});
</script>

<template>
  <view class="container" :style="{ opacity: props.isLoading ? 1 : 0 ,Top:props.top+'rpx'}">
    <view class="preloader">
      <view></view>
      <view></view>
      <view></view>
      <view></view>
      <view></view>
    </view>
  </view>
</template>

<style scoped>

.container {
  position: fixed;
  z-index: 999;
  width: 100%;
  left: -40rpx;
  display: flex;
  justify-content: center;
  align-items: center;
  transition: opacity 0.5s ease-in-out;
}


.preloader {
  position: relative;
  display: flex;
}

.preloader view {
  border-radius: 4rpx;
  display: block;
  bottom: 0;
  width: 23rpx;
  height: 5rpx;
  background: #9b59b6;
  position: absolute;
  animation: preloader 1.5s infinite ease-in-out;
}

.preloader view:nth-child(2) {
  left: 21rpx;
  animation-delay: .2s;
}

.preloader view:nth-child(3) {
  left: 32rpx;
  animation-delay: .4s;
}

.preloader view:nth-child(4) {
  left: 43rpx;
  animation-delay: .6s;
}

.preloader view:nth-child(5) {
  left: 54rpx;
  animation-delay: .8s;
}

@keyframes preloader {
  0% {
    height: 5rpx;
    transform: translateY(0px);
    background: #9b59b6;
  }
  25% {
    height: 30rpx;
    transform: translateY(15rpx);
    background: #ff6fa0;
  }
  50% {
    height: 5rpx;
    transform: translateY(0rpx);
    background: #9b59b6;
  }
  100% {
    height: 5rpx;
    transform: translateY(0rpx);
    background: #9b59b6;
  }
}
</style>
