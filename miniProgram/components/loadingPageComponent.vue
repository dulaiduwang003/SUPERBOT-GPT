<template>
  <view class="loading-container">
    <view>
      <view class="loading-model">
        <view class="loader"></view>
      </view>
    </view>
  </view>
</template>


<style lang="scss">
.back {
  background-color: rgb(138, 117, 255);
  color: white;
  width: 500rpx;
  font-size: 30rpx
}

.loader {
  width: 100rpx;
  height: 100rpx;
  position: relative;
  animation: preloader_custom 3s infinite ease-in-out;
}

.loading_title {
  color: #818181;
  padding-top: 100rpx;
  text-align: center
}

.prompt {
  color: #797979;
  font-size: 24rpx;
  text-align: center;
  padding-top: 30rpx
}

.queue {
  padding-top: 10rpx;
  color: #a8a6ad;
}

.btn_back {
  padding: 20rpx 300rpx;
  margin-top: 30rpx
}

.loader:before,
.loader:after {
  content: "";
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.loading-container {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  animation: fadeIn 0.7s ease-in-out forwards;
}

.loader:before {
  width: 40rpx;
  height: 40rpx;
  border-radius: 50%;
  background: #c2a5ff;
  animation: preloader_custom_before 1.5s infinite ease-in-out;
}

.loader:after {
  width: 40rpx;
  height: 40rpx;
  border-radius: 50%;
  background: #f15757;
  left: 22rpx;
  animation: preloader_custom_after 1.5s infinite ease-in-out;
}

@keyframes preloader_custom {
  0% {
    transform: translateX(0rpx) rotate(0deg);
  }
  50% {
    transform: translateX(50rpx) scale(1.2) rotate(260deg);
    background: #b16ef1;
    border-radius: 0rpx;
  }
  100% {
    transform: translateX(0rpx) rotate(0deg);
  }
}

@keyframes preloader_custom_before {
  0% {
    transform: translateX(0rpx);
  }
  50% {
    transform: translateX(-50rpx) scale(1.2) rotate(-260deg);
    background: #6ea3d2;
    border-radius: 0rpx;
  }
  100% {
    transform: translateX(0rpx);
  }
}

.loading-model {
  display: flex;
  align-items: center;
  justify-content: center
}

@keyframes preloader_custom_after {
  0% {
    transform: translateX(0rpx);
  }
  50% {
    transform: translateX(-50rpx) scale(1.2) rotate(-260deg);
    background: #6ea3d2;
    border-radius: 0rpx;
  }
  100% {
    transform: translateX(0rpx);
  }
}
</style>
<script setup lang="ts">
</script>
