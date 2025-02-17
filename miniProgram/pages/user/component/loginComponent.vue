<script setup>
import AniLottie from '/static/lottie/paperMan.json';
import StarLottie from '/static/lottie/star.json';
import LottieAnimationComponent from "/components/lottieAnimationComponent.vue";
import {defineProps, defineEmits} from 'vue'

const emits = defineEmits(['login']);

const props = defineProps({
  isLoading: {
    type: Boolean
  }
});

const login = (e) => {
  emits('login', e.detail.code)

}


</script>


<template>
  <view class="body">
    <view class="icon">
      <LottieAnimationComponent :frames="AniLottie" :width="500" :height="300"/>
    </view>
    <view class="prompt">
      <p class="title">TYPE STARTS</p>
      <p class="word">welcome to use ai mini app</p>
    </view>
    <view class="button-box">
      <button class="btn-login" :loading="props.isLoading" @click="login">
        {{ !props.isLoading ? '微信授权登录' : '' }}
      </button>
      <view class="animation" @click="login">
        <LottieAnimationComponent :frames="StarLottie" :width="400" :height="300"/>
      </view>
    </view>
  </view>


</template>


<style scoped lang="scss">


.icon {
  display: flex;
  justify-content: center;
  align-items: center;
  padding-top: 200rpx;
}

.btn-login {
  font-size: 30rpx;
  border-radius: 70rpx;
  color: white;
  background: linear-gradient(120deg, rgba(98, 158, 239) 1%, rgba(186, 124, 250) 30.31%, rgba(225, 109, 189) 66.24%, rgb(255, 146, 146) 97.8%);;
  animation: gradient 4s ease infinite;
  background-size: 200% 200%;
  border: none;
  padding: 10rpx;
}

.animation {
  position: absolute;
  z-index: 2;
  top: -65rpx;
  left: 130rpx
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

.body {

  animation: fadeIn 0.5s ease-in-out forwards;
  padding: 150rpx 50rpx 50rpx;
}

.prompt {
  color: #ffffff;
  padding-left: 30rpx;
  padding-top: 130rpx
}

.title {
  font-size: 55rpx;
  font-weight: 550
}

.word {
  font-size: 23rpx;
  color: #a9a7a7;
  padding-top: 10rpx
}

.button-box {

  margin-top: 100rpx;
  padding: 0 30rpx;
  position: relative;
}
</style>
