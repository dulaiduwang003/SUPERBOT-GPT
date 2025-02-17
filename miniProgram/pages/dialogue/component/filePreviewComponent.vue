<script setup>
import {defineEmits, defineProps} from 'vue'

const emits = defineEmits(['preview']);

const props = defineProps({
  fileType: {
    type: String
  },
  fileName: {
    type: String,
    default: ''
  },
  fileSize: {
    type: Number,
    default: ''
  },
  fileUrl: {
    type: String,
    default: ''
  }
});

const preview = (src) => {
  emits('preview',src)
}

</script>

<template>
  <view class="container" @click="preview(props.fileUrl)">
    <view>
      <view>
        <image src="/static/icon/pdf.svg" v-if="props.fileType==='PDF'"/>
        <image src="/static/icon/excel.svg" v-if="props.fileType==='XLSX'"/>
        <image src="/static/icon/docx.svg" v-if="props.fileType==='DOCX'"/>
        <image src="/static/icon/image.svg" v-if="props.fileType==='IMAGE'"/>
      </view>
      <view class="text">
        <view class="prompt">
          {{ props.fileName }}
        </view>
        <view class="size">
          {{ props.fileSize }} KB
        </view>
      </view>
    </view>
  </view>
</template>


<style scoped lang="scss">

.container > view {
  display: flex;
  align-items: center;
  justify-content: center
}

.text {
  width: 265rpx;
  font-size: 28rpx;
  padding-left: 10rpx;
  padding-top: 20rpx
}

.size {
  font-size: 24rpx;
  color: #c1c1c1
}

.prompt {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: #e5e5e5;
  font-weight: 550
}

.container image {
  width: 60rpx;
  height: 60rpx;
  margin-top: 24rpx
}

.container {


  width: 380rpx;
  height: 110rpx;
  background-color: #1D1D1D;

  border-radius: 10rpx

}

</style>
