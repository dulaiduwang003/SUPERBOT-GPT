<script setup>
import {ref} from 'vue'
import {onLoad} from '@dcloudio/uni-app'
import {getChatTopic, setChatTopic} from "@/store/chatTopic";

const data = ref({
  other: [],
  today: [],
  yesterday: []
})

const handoffTopic = (id) => {

  const chatTopic = getChatTopic();
  const index = chatTopic.array.findIndex(item => item.id === id);
  chatTopic.index = index
  const content = chatTopic.array[index].content;
  setChatTopic(chatTopic)
  uni.$emit('dialogue_template_html', content)
  uni.navigateBack()

}

onLoad(() => {
  uni.showLoading({
    title: "加载中",
  });
  const chatTopic = getChatTopic();
  const array = chatTopic.array;
  data.value = groupDates(array);
  uni.hideLoading()
})

const groupDates = (dates) => {
  const today = new Date();
  const yesterday = new Date(today);
  yesterday.setDate(today.getDate() - 1);
  const groupedDates = {
    today: [],
    yesterday: [],
    other: []
  };
  dates.forEach(dateObj => {
    const date = new Date(dateObj.date);
    if (date.toDateString() === today.toDateString()) {
      groupedDates.today.push(dateObj);
    } else if (date.toDateString() === yesterday.toDateString()) {
      groupedDates.yesterday.push(dateObj);
    } else {
      groupedDates.other.push(dateObj);
    }
  });
  return groupedDates;
}


</script>

<template>
  <view class="container">
    <view class="model" v-if="data.today.length>0">
      <view class="title">
        今天
      </view>
      <view class="box" v-for="(item,index) in data.today " :key="index" @click="handoffTopic(item.id)">
        <view class="icon">
          <image src="/static/icon/document.svg"/>
        </view>
        <view class="prompt">
          {{ item.title ? item.title : '未命名的会话主题' }}
        </view>
        <view class="date">
          {{ item.time }}
        </view>
      </view>
    </view>
    <view class="model" v-if="data.yesterday.length>0">
      <view class="title">
        昨天
      </view>
      <view class="box" v-for="(item,index) in data.yesterday " :key="index" @click="handoffTopic(item.id)">
        <view class="icon">
          <image src="/static/icon/document.svg"/>
        </view>
        <view class="prompt">
          {{ item.title ? item.title : '未命名的会话主题' }}
        </view>
        <view class="date">
          {{ item.time }}
        </view>
      </view>
    </view>
    <view class="model" v-if="data.other.length>0">
      <view class="title">
        上一次
      </view>
      <view class="box" v-for="(item,index) in data.yesterday " :key="index" @click="handoffTopic(item.id)">
        <view class="icon">
          <image src="/static/icon/document.svg"/>
        </view>
        <view class="prompt">
          {{ item.title ? item.title : '未命名的会话主题' }}
        </view>
        <view class="date">
          {{ item.date }}
        </view>
      </view>
    </view>
  </view>
</template>


<style scoped lang="scss">
.container {
  padding: 30rpx;
  color: white;
}

.model {
  margin-bottom: 50rpx
}

.box {
  background: #1e1e1e;
  margin-top: 30rpx;
  border-radius: 20rpx;
  padding: 30rpx;
  display: flex;
  align-items: center;
}

.icon {
  width: 10%;

}

.icon > image {
  width: 40rpx;
  height: 40rpx
}

.prompt {
  width: 65%;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.date {
  width: 25%;
  text-align: right;
  color: #6d6d6d
}

.title {
  font-size: 38rpx;
  font-weight: bold;
}

</style>
