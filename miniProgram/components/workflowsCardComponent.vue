<script setup>
import {defineProps} from 'vue'
import {conversionTime} from "../utils/date";
import CompareImageComponent from "./compareImageComponent.vue";

const props = defineProps({
  array: {
    type: Array
  },
});


const toPage = (url) => {
  uni.navigateTo({
    url: url,
    animationType: 'pop-in',
    animationDuration: 200
  })
}

</script>

<template>
  <view class="card" v-for="(item,index) in array" :key="index" @click="toPage('/pages/drawing/view/drawingBrieflyView?workflowsId='+item.workflowsId)">
    <view class="card-image">
      <CompareImageComponent :width="'680'" :height="'600'" :beforeImageUrl="item.originalImage" :after-image-url="item.nowImage" />
    </view>
    <view class="cardText">
      <view>
        {{ item.title }}
      </view>
      <view class="cardTextBox">
        <view class="cardTextBoxChild">
          <view>
            <uni-icons type="paperplane" size="26rpx" color="#a6a6a6"></uni-icons>
            <view class="paddingLeft10">发布于{{ conversionTime(item.createTime) }}</view>
          </view>
          <view class="marginLeft20">
            <uni-icons type="star-filled" size="26rpx" color="#a6a6a6"></uni-icons>
            <view class="paddingLeft10">{{ item.visited }}人在玩</view>
          </view>
        </view>
        <view class="btn" @click="toPage('/pages/drawing/view/drawingBrieflyView?workflowsId='+item.workflowsId)">
          快速生成
        </view>
      </view>
    </view>
  </view>
</template>


<style scoped lang="scss">

.cardText {
  padding: 30rpx 40rpx;
  color: white;
  font-size: 30rpx
}

.cardTextBox {
  font-size: 24rpx;
  margin-top: 40rpx;
  color: #a6a6a6;
  display: flex;
  justify-content: space-between;
  align-items: center
}

.btn {
  background: linear-gradient(120deg, rgb(239, 95, 97) 1%,rgb(186, 42, 107) 66.24%, rgb(184, 54, 189) 97.8%);;
  animation: gradient 4s ease infinite;
  background-size: 200% 200%;
  padding: 10rpx 20rpx;
  border-radius: 10rpx;
  color: white;
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

.card {
  border-radius: 20rpx;
  overflow: hidden;
  background: #1d1f1e;
  margin-bottom: 40rpx;
  animation: fadeIn 0.7s ease-in-out forwards;
}

.card-image {
  display: flex;
  position: relative
}

.card-image-box {
  width: 50%;
}

.card-image-box image {
  width: 100%;
  height: 600rpx;
}

.arrow {
  position: absolute;
  top: 45%;
  left: 47%;
  z-index: 2
}

.arrow > image {
  width: 50rpx;
  height: 50rpx
}

.cardTextBoxChild {
  display: flex
}

.cardTextBoxChild > view {
  display: flex;
  align-items: center
}

.marginLeft20 {
  margin-left: 20rpx
}

.paddingLeft10 {
  padding-left: 10rpx
}
</style>
