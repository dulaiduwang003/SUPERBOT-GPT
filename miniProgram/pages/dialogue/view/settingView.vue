<script setup>

const toRouterPage = (url) => {
  uni.navigateTo(
      {url: url, animationType: 'pop-in', animationDuration: 200}
  )
}

</script>

<template>
  <view class="body">
    <view class="flex-padding">
      <view class="card" @click="toRouterPage('/pages/dialogue/view/chatTopicView')">
        <view class="operation">
          <span class="prompt">Chat history</span>
          <span>
        <image src="/static/setting/next.svg" class="image-icon"/>
      </span>
        </view>
        <p class="introduce">
          Transcript of the conversation with the model
        </p>
      </view>
      <view class="card" @click="toRouterPage('/pages/dialogue/view/changeModelView')">
        <view class="operation">
          <span class="prompt">Change model</span>
          <span>
        <image src="/static/setting/next.svg" class="image-icon"/>
      </span>
        </view>
        <p class="introduce">
          This option changes the model used by the dialog
        </p>
      </view>
      <view class="card">
        <view class="operation">
          <span class="prompt">Talk to developers</span>
          <span>
        <image src="/static/setting/next.svg" class="image-icon"/>
      </span>
        </view>
        <p class="introduce">
          Conversational functional or bug feedback reports
        </p>
        <button open-type="contact" class="customer">
        </button>
      </view>
    </view>
  </view>
</template>


<style scoped lang="scss">
.body {
  padding: 30rpx;
}

.container {
  color: white;
  font-size: 25rpx;
}


.logout {
  margin-top: 80rpx
}

.logout view {
  background-color: #f32b2b;
  color: white;
  text-align: center;
  border-radius: 10rpx;
  padding: 15rpx 0;
  font-size: 28rpx
}

.image-icon {
  width: 25rpx;
  height: 25rpx;
  margin-right: 14rpx
}

.card {
  background-color: #262626;
  padding: 30rpx;
  border-radius: 20rpx;
  margin: 30rpx 0;
  color: white;
  position: relative;
}

button[plain] {
  border: 0
}

.customer {
  top: 0;
  right: 0;
  width: 100%;
  height: 100%;
  background-color: unset;
  color: white;
  z-index: 3;
  position: absolute;
}

.operation {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 20rpx
}

.prompt {
  font-size: 28rpx;
  font-weight: 550
}

.flex-padding {
  padding-bottom: 140rpx
}

.introduce {
  font-size: 23rpx;
  color: #c1c1c1;
}
</style>
