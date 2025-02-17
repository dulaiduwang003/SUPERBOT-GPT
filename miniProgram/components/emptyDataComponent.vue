<template>
  <view class="empty-container" :style="{'height': props.height + 'vh'}">
    <view>
      <image src="/static/svg/empty.svg" class="empty-image"/>
      <view class="empty-title">{{ msg }}</view>
    </view>
  </view>
</template>

<script setup>
import {defineProps} from 'vue'

const props = defineProps({
  msg: {
    type: String,
    default: '这里空空如也'
  },
  height: {
    type: Number,
    default: 100
  },
});

</script>

<style lang="scss">
.empty-container {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  animation: fadeIn 0.7s ease-in-out forwards;
}

.empty-image {
  width: 200rpx;
  height: 200rpx
}

.empty-title {
  padding-top: 20rpx;
  color: #858585
}
</style>
