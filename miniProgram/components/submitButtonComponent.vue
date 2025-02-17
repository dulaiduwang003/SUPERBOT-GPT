<template>
  <view class="generated" @click="emits('onClick')">
    <view >
      <view class="btn">
        {{ title }}
      </view>
      <view class="stars" >
        <LottieAnimationComponent :frames="StarLottie" :width="300" :height="200"/>
      </view>
    </view>
  </view>
</template>

<script setup>

import LottieAnimationComponent from "./lottieAnimationComponent.vue";
import {defineProps, defineEmits} from 'vue'
import StarLottie from '/static/lottie/star.json';


const emits = defineEmits(['onClick']);

const props = defineProps({
  title: {
    type: String
  },
});

</script>


<style scoped lang="scss">
.generated {
  background-color: #161817;
  position: fixed;
  bottom: 0;
  width: 100%;
  padding-bottom: 20rpx;
  left: 0
}

.generated > view {
  position: relative
}

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

.stars {
  position: absolute;
  z-index: 3;
  left: 200rpx;
  top: -60rpx
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
