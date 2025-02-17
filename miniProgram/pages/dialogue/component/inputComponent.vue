<script setup>
import {defineExpose, defineProps, ref} from 'vue'
import {onLoad} from '@dcloudio/uni-app'


const emits = defineEmits(['send', 'stop', 'upload']);

const animationInput = ref({})

const text = ref('')

const props = defineProps({
  isLoading: {
    type: Boolean,
    default: false
  },
});
const send = () => {
  if (text.value.trim()) {
    uni.vibrateShort()
    emits('send', text.value)
  }
}

const stop = () => {
  uni.vibrateShort()
  emits('stop')
}

const upload = () => {
  uni.vibrateShort()
  emits('upload')
}

const clear = () => {
  text.value = ''
}



onLoad(() => {
  //加载动画
  const animation = uni.createAnimation({

    duration: 900,
    timingFunction: 'ease-in-out'
  })
  animation.translate(0, -85).step()
  animationInput.value = animation.export()
  //
})

defineExpose(
    {clear}
)

</script>

<template>
  <view class="container" :animation="animationInput ? animationInput : ''">
    <view class="model-container">
      <view class="btn-more" @click="upload">
        <image src="/static/icon/more.svg"/>
      </view>
      <input
          v-model="text"
          cursor-spacing="10"
          class="textarea"
          show-confirm-bar="false"
          maxlength="-1"
          :disabled="props.isLoading"
          :placeholder="props.isLoading?'请稍等片刻...':'有什么需要我帮忙的吗?'"/>
      <view class="btn-send" v-if="!props.isLoading" @click="send">
        <image src="/static/icon/send.svg"/>
      </view>
      <view class="btn-time-out" v-else @click="stop">
        <view/>
      </view>
    </view>
  </view>
</template>


<style scoped lang="scss">
.fade-enter-active, .fade-leave-active {
  transition: opacity .5s;
}

.fade-enter, .fade-leave-to {
  opacity: 0;
}


.container {
  position: fixed;
  bottom: -12vh;
  left: 0;
  width: 750rpx;
  z-index: 2;


}

.model-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 30rpx 30rpx 45rpx;
}

.btn-send {

  display: flex;
  justify-content: center;
  align-items: center;
  width: 70rpx;
  height: 70rpx;
  border-radius: 100%;
  background-color: #4c4b4b
}

.btn-time-out {

  display: flex;
  justify-content: center;
  align-items: center;
  width: 70rpx;
  height: 70rpx;
  border-radius: 100%;
  background-color: #3e3e3e;
}

.btn-time-out view {
  background-color: #d1d1d1;
  width: 25rpx;
  height: 25rpx;
}

.btn-send image {
  width: 40rpx;
  height: 40rpx;
}

.btn-more {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: transparent;
  width: 45rpx;

  height: 74rpx
}

.btn-more image {
  width: 45rpx;
  height: 45rpx

}

.textarea {
  border: 4rpx solid #4c4c4c;
  padding: 10rpx 20rpx;
  border-radius: 38rpx;
  width: 490rpx;
  height: 30rpx;

  font-size: 26rpx;

}

</style>
