<template>
  <view class="container" @click="toPage('/pages/drawing/view/drawingHistoricalView')">
    创<br/>作<br/>历<br/>史
  </view>
</template>
<script setup>
const toPage = (url) => {
  uni.navigateTo({
    url: url,
    animationType: 'pop-in',
    animationDuration: 200
  })
}
</script>


<style scoped lang="scss">
.container {
  padding: 15rpx 20rpx;
  border-radius: 15rpx 0 0 10rpx;
  position: fixed;
  right: 0;
  background: #fc315d;
  top: 30vh;
  z-index: 99;
  color: white;
  font-size: 26rpx;
  text-align: center;
  animation: slideEase 0.5s ease-in-out forwards;
}

@keyframes slideEase {
  0% {
    transform: translateX(-100px);
  }
  100% {
    transform: translateX(0);
  }
}

</style>
