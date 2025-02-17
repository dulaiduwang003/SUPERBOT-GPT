<script setup>

</script>

<template>
  <view class="loading">
    <view class="dot0"></view>
    <view class="dot1"></view>
    <view class="dot2"></view>
    <view class="dot3"></view>

  </view>
</template>


<style scoped lang="scss">
.loading {
  display: flex;
  padding: 20rpx 0
}

.dot0,
.dot1,
.dot2,
.dot3 {

  width: 30rpx;
  height: 30rpx;
  border-color: #464646;
  border-radius: 50%;
}

.dot0 {
  animation: jump 1.3s -0.64s linear infinite;
}

.dot1 {
  animation: jump 1.3s -0.32s linear infinite;
}

.dot2 {
  animation: jump 1.3s -0.16s linear infinite;
}

.dot3 {
  animation: jump 1.3s linear infinite;
}

@keyframes jump {
  0%,
  80%,
  100% {
    transform: scale(0);
    background-color: #a5a5a5;
  }
  40% {
    transform: scale(1.0);
    background-color: rgb(237, 237, 237);
  }
}
</style>
