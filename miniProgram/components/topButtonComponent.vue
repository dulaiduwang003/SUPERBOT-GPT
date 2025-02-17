<template>
  <view class="container" :style="{ opacity: props.isShow ? 1 : 0 }" @click="emits('clickTop')">
    <image src="/static/svg/top.svg"/>
  </view>
</template>
<script setup>

import {defineProps, defineEmits} from 'vue'

const emits = defineEmits(['clickTop']);

const props = defineProps({
  isShow: {
    type: Boolean
  }
});

</script>


<style scoped lang="scss">
.container {
  position: fixed;
  right: 40rpx;
  top: 80vh;
  z-index: 99;
  color: white;
  font-size: 26rpx;
  text-align: center;
  transition: opacity 0.5s ease-in-out;
}

.container image {
  height: 90rpx;
  width: 90rpx;
}

</style>
