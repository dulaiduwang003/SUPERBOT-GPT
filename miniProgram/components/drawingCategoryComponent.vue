<template>
  <view style="width: 100%">
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
</template>

<script setup>
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
const emits = defineEmits(['category']);

const isUnfold = ref(false)

const category = (data) => {
  emits('category', data)
}


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
