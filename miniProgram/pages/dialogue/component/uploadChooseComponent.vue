<script setup>
import {defineEmits, defineExpose, ref} from 'vue'

const emits = defineEmits(['upload-image', 'upload-file']);

const popupRef = ref(null)


const open = () => {
  popupRef.value.open()
}
const close = () => {
  popupRef.value.close()
}

const chooseMessageFile = () => {
  uni.vibrateShort()
  emits('upload-file')

}

const chooseSystemImage = () => {
  uni.vibrateShort()
  emits('upload-image')
}

defineExpose(
    {open, close}
)
</script>


<template>
  <view>
    <uni-popup ref="popupRef" type="bottom" background-color="#171717">
      <view class="container">
        <view @click="chooseMessageFile">
          <view class="model">
            <image src="/static/icon/file.svg"/>
          </view>
          <view class="prompt">
            选择文件
          </view>
        </view>
        <view @click="chooseSystemImage">
          <view class="model">
            <image src="/static/icon/album.svg"/>
          </view>
          <view class="prompt">
            选择相册
          </view>
        </view>
      </view>
    </uni-popup>
  </view>
</template>


<style scoped lang="scss">
.container {
  display: flex;
  align-items: center;
  height: 200rpx;
  padding: 30rpx;
}

.container > view {
  text-align: center;
  margin-right: 40rpx;
}

.model {
  align-items: center;
  background-color: #0e0e0e;
  width: 140rpx;
  height: 140rpx;
  border-radius: 15rpx;
  display: flex;
  justify-content: center
}

.model image {
  width: 60rpx;
  height: 60rpx
}

.prompt {
  color: #b4adad;
  padding-top: 20rpx;
  font-size: 24rpx;
  outline-width: 40rpx;
}
</style>
