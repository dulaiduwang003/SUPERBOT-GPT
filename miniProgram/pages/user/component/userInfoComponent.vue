<template>
  <view class="container">
    <view :style="{paddingTop:barTop+'px',height:barHeight+'px'}"
          class="setting">
      <image src="/static/svg/setting.svg" @click="handleToPage('/pages/setting/setting')"/>
    </view>
    <view class="user-info" v-if="store.getters.userInfo">
      <view>
        <image :src="store.getters.userInfo.avatar?store.getters.userInfo.avatar:'/static/avatar/default_user.png'"/>
      </view>
      <view class="user-info-data">
        <view class="nick-name">
          {{ store.getters.userInfo.nickName }}
        </view>
        <view class="profile">为什么半岛铁盒只有五分十九秒</view>
        <view class="energy">
          能量: {{ store.getters.userInfo.energy }}
        </view>
      </view>
    </view>

    <view class="ad" @click="emits('rewardedAds')">
      <view class="ad-left">
        <view class="ad-text" >
          观看广告
        </view>
        <view
            class="ad-btn" @click="emits('rewardedAds')">
          立即观看
        </view>
      </view>
      <view class="ad-right">
        无限制获取能量
      </view>
    </view>
  </view>
</template>

<script setup>
import {onLoad} from '@dcloudio/uni-app'
import {ref,defineEmits} from 'vue'
import store from "@/store";


const emits = defineEmits(['rewardedAds']);

const barTop = ref(0);

const barHeight = ref(0)


const handleToPage = (url) => {
  uni.navigateTo({
    url: url,
    animationType: 'pop-in',
    animationDuration: 200
  })
}

onLoad(() => {
  let menuButtonBoundingClientRect = uni.getMenuButtonBoundingClientRect();
  barTop.value = menuButtonBoundingClientRect.top + 5
  barHeight.value = menuButtonBoundingClientRect.height

})


</script>


<style lang="scss" scoped>
.container {
  padding: 0rpx 30rpx 30rpx;
}

.setting {
  width: 100%;
  background-color: #161817;
}

.setting > image {

  width: 50rpx;
  height: 50rpx;
}

.user-info {
  display: flex;
  align-items: center;
  padding-top: 60rpx
}

.ad {
  margin-top: 40rpx;
  padding: 20rpx 40rpx;
  background: linear-gradient(to right, #f9dad5 70%, #d0cdf8 100%);
  border-radius: 20rpx
}

.ad-left {
  display: flex;
  justify-content: space-between;
  align-items: center
}

.ad-right {
  padding-top: 20rpx;
  font-size: 25rpx;
  color: #5c5d66
}

.ad-text {
  font-size: 34rpx;
  font-weight: bold
}

.ad-btn {
  font-weight: bold;
  color: #8c325b;
  background: white;
  padding: 15rpx 30rpx;
  border-radius: 35rpx;
  font-size: 30rpx
}

.user-info image {
  border-radius: 100%;
  width: 130rpx;
  height: 130rpx
}

.user-info-data {
  padding-left: 30rpx
}

.nick-name {
  color: white;
  font-size: 35rpx;
  font-weight: 550;
}

.profile {
  color: #919193;
  font-size: 26rpx;
  padding-top: 8rpx
}

.energy {
  color: #919193;
  font-size: 26rpx;
  padding-top: 8rpx
}
</style>
