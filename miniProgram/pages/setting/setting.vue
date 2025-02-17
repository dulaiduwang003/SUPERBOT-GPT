<script setup>

const toPage = (url) => {
  uni.navigateTo(
      {url: url, animationType: 'pop-in', animationDuration: 200}
  )
}

</script>


<template>
  <view class="container">
    <view class="borderRadius ">
      <view class="row " @click="toPage('/pages/setting/view/personalInformationView')">
        <view>
          个人信息
        </view>
        <view>
          <image src="/static/icon/rightArrow.svg"/>
        </view>
      </view>
      <view class="row " @click="toPage('/pages/setting/view/userAgreementView')">
        <view>
          用户协议
        </view>
        <view>
          <image src="/static/icon/rightArrow.svg"/>
        </view>
      </view>
    </view>

    <view class="borderRadius bottom">
      <view class="row" @click="toPage('/pages/setting/view/privacyPolicyView')">
        <view>
          隐私政策
        </view>
        <view>
          <image src="/static/icon/rightArrow.svg"/>
        </view>
      </view>
      <view class="relative">
        <view class="row">
          <view>
            帮助与客服
          </view>
          <view>
            <image src="/static/icon/rightArrow.svg"/>
          </view>
        </view>
        <button open-type="contact" class="customer">
        </button>
      </view>

    </view>
  </view>
</template>

<style scoped lang="scss">
.container {
  padding: 30rpx
}

.relative {
  position: relative;
}

.row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #222325;

  padding: 30rpx;
  color: whitesmoke
}

.customer {
  top: 0;
  right: 0;
  width: 100%;
  height: 100%;
  background-color: unset;

  z-index: 3;
  position: absolute;
}

.borderRadius {
  border-radius: 20rpx;
}

.bottom {
  margin-top: 30rpx;
  border-radius: 20rpx;
  overflow-y: hidden
}

.row image {
  width: 25rpx;
  height: 25rpx
}
</style>
