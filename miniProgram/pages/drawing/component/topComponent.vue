<template>
  <view class="container"
        :style="{top:barTop+'px',height:barHeight+'px'}">
    <view class="logo">
      <image src="/static/img/logo.png" />
      <view class="logo-text">
        <view>TYPE STARS</view>
        <view class="logo-de">dulaiduwang003</view>
      </view>
    </view>
    <view class="search">
      <uni-icons type="search" size="50rpx" color="#676767" class="search-icon"></uni-icons>
      <input v-model="prompt" type="text" placeholder="搜索创意" class="search-input" confirm-type="search"
             @blur="handleQueryByPromptClear"
             @confirm="emits('queryByPrompt',prompt)"/>

    </view>
    <scroll-view scroll-x="true" class="scroll-view">
      <view :class="item.workflowsCategoryId===workflowsCategoryId?'scroll-item-active':'scroll-item'"
            v-for="(item, index) in workflowsCategoryData" :key="index"
            @click="emits('queryByCategory',item.workflowsCategoryId)">
        {{ item.categoryName }}
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import {onLoad} from '@dcloudio/uni-app'
import {ref, watch, defineEmits, defineProps} from 'vue'

const props = defineProps({
  workflowsCategoryData: {
    type: Array
  },
  workflowsCategoryId: {
    type: String
  },
  prompt: {
    type: String
  }
});

const emits = defineEmits(['queryByCategory', 'queryByPrompt']);

const barTop = ref(0);

const barHeight = ref(0)

const workflowsCategoryId = ref('')

const prompt = ref('')

const handleQueryByPromptClear = () => {
  emits('queryByPrompt', '')
}


onLoad(() => {
  let menuButtonBoundingClientRect = uni.getMenuButtonBoundingClientRect();
  barTop.value = menuButtonBoundingClientRect.top
  barHeight.value = menuButtonBoundingClientRect.height

})


watch(() => props.workflowsCategoryId, (e) => {
  workflowsCategoryId.value = e
}, {
  immediate: true
})

watch(() => props.prompt, (e) => {
  prompt.value = e
}, {
  immediate: true
})


</script>


<style scoped lang="scss">

.container {
  position: fixed;
  left: 0;
  width: 100%;
  z-index: 2;
  background-color: #161817;
}

.search {
  padding: 10rpx 20rpx;
  background-color: #1D1F1E;
  margin: 0 20rpx;
  height: 100%;
  border-radius: 10rpx;
  color: #676767;
  display: flex;
  align-items: center
}

.search-icon {
  margin-top: 2rpx;
  margin-right: 10rpx
}

.search-input {
  font-size: 30rpx;
  flex: 1
}

.scroll-view {
  padding-left: 20rpx;
  width: 100%;
  white-space: nowrap;
  margin: 30rpx 0;
}

.scroll-item {
  font-size: 25rpx;
  color: #9c9c9c;
  display: inline-block;
  text-align: center;
  font-weight: 550;
  margin-right: 85rpx;
  box-sizing: border-box;
}

.scroll-item-active {
  font-size: 25rpx;
  color: #ffffff;
  display: inline-block;
  text-align: center;
  font-weight: 550;
  margin-right: 85rpx;
  box-sizing: border-box;
}
.logo{
  width: 100%;padding: 0 30rpx 30rpx;display: flex;align-items: center
}
.logo image{
  width: 70rpx;height: 70rpx;
}
.logo-text{
  font-weight: bold;
  font-size: 30rpx;padding-left: 20rpx;color: #ffffff
}
.logo-de{
  font-weight: 500;
  font-size: 25rpx;color: #c0c0c0
}
</style>
