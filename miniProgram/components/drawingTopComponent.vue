<template>
  <view style="position: fixed;left: 0;width: 100%;z-index: 2; background-color:#161817;"
        :style="{paddingTop:barTop+'px'}">
    <view
        style="display: flex;align-items: center;margin-left: 30rpx; border-radius: 15rpx;width: 500rpx;color: whitesmoke;padding: 10rpx"
        :style="{height:barHeight+'px'}">
      <view style="display: flex;align-items: center">
        <image src="/static/avatar/default_gpt.svg" style="width: 70rpx;height: 70rpx"/>
        <view style="font-size: 30rpx;padding-left: 20rpx">
          <view>TYPE STARS</view>
          <view style="font-size: 25rpx;color: #c0c0c0">miao-qu.com</view>
        </view>
      </view>
    </view>
    <view style="padding-top: 35rpx;width:
100%;">
      <view
          style="display: flex;align-items: center; background-color: #1d1f1e; border-radius: 15rpx;color: whitesmoke;padding: 10rpx;margin: 0 30rpx"
          :style="{height:barHeight+'px'}">
        <uni-icons type="search" size="50rpx"></uni-icons>
        <input type="text" v-model="text" placeholder="搜一搜想找的工作流"
               style="color: whitesmoke; margin-left: 10rpx;" confirm-type="search" @blur="clear"
               @confirm="search"/>
      </view>
    </view>
    <view style="width: 100%;padding-bottom: 30rpx">
      <view
          style="display: flex;justify-content: space-between; padding: 20rpx 30rpx 0;align-items: flex-start;overflow: hidden"
          :class="isUnfold?'unfold':'collapse'">
        <view style="display: flex;flex-wrap: wrap;">
          <view
              @click="category(item.workflowsCategoryId)"
              :class="props.workflowsCategoryId===item.workflowsCategoryId?'label-active':'label'"
              v-for="(item) in props.workflowsCategoryList" :key="item.workflowsCategoryId">
            {{ item.categoryName }}
          </view>
        </view>
        <view>
          <uni-icons type="down" size="50rpx" v-if="isUnfold" @click="isUnfold=!isUnfold"></uni-icons>
          <uni-icons type="up" size="50rpx" v-else @click="isUnfold=!isUnfold"></uni-icons>
        </view>
      </view>
    </view>
  </view>
</template>


<script setup>
import {onLoad} from '@dcloudio/uni-app'
import {ref} from 'vue'
import {defineEmits, defineProps} from 'vue'

const props = defineProps({
  workflowsCategoryList: {
    type: Array
  },
  workflowsCategoryId: {
    type: Number
  }
});


const emits = defineEmits(['search', 'category']);

const barTop = ref(0);

const barHeight = ref(0)

const text = ref('')


const search = () => {
  emits('search', text.value)
}


const clear = () => {
  if (text.value.trim().length === 0) {
    search()
  }
}



const isUnfold = ref(false)

const category = (data) => {
  emits('category', data)
}


onLoad(() => {
  let menuButtonBoundingClientRect = uni.getMenuButtonBoundingClientRect();
  barTop.value = menuButtonBoundingClientRect.top - 5
  barHeight.value = menuButtonBoundingClientRect.height

})
</script>

<style scoped>

.unfold {
  height: auto;
}

.collapse {
  height: 50rpx;
}

.label {
  padding: 8rpx 25rpx;
  font-size: 26rpx;
  color: #a1a1a1;
  background: #1d1f1e;
  border-radius: 10rpx;
  margin-right: 15rpx;
  margin-bottom: 15rpx
}

.label-active {
  padding: 8rpx 25rpx;
  font-size: 26rpx;
  color: #ffffff;
  background: linear-gradient(120deg, rgb(250, 52, 54) 1%, rgb(186, 42, 107) 66.24%, rgb(184, 54, 189) 97.8%);
  animation: gradient 4s ease infinite;
  background-size: 200% 200%;
  border-radius: 10rpx;
  margin-right: 15rpx;
  margin-bottom: 15rpx
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
