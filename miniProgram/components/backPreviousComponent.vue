<template>
  <view class="backBar" :style="{marginTop:props.barTop+'px',height:props.barHeight+'px'}" @click="backPreviousPage">
    <view>
      <uni-icons type="left" size="26rpx" color="black"></uni-icons>
    </view>
    <view>返回</view>
  </view>
</template>

<script setup>
import {defineProps} from 'vue';

const props = defineProps({
  barTop: {
    type: Number
  },
  barHeight: {
    type: Number,
  }

});

const backPreviousPage = () => {
  try {
    uni.navigateBack()
  }catch (e) {
    uni.reLaunch({
      url: '/pages/user/user'
    })
  }
}

</script>


<style scoped lang="scss">

.backBar {
  position: fixed;
  background: rgba(255, 255, 255, 0.65);
  display: flex;
  justify-content: center;
  align-items: center;
  left: 20rpx;
  font-size: 26rpx;
  padding: 5rpx 25rpx;
  border-radius: 20rpx;
  font-weight: bold;
  z-index: 99999;
}
</style>
