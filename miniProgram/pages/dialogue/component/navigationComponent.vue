<script setup>
import {defineProps, ref} from 'vue'
import {onLoad} from '@dcloudio/uni-app'
import {getTokenValue} from "@/store/token";

const props = defineProps({
  // 显示文本
  title: {
    type: String,
    default: "TypeScript"
  },
  // 模型版本
  modelVersion: {
    type: String,
    default: ''
  },
  //详细信息
  details: {
    type: String,
    default: ''
  }
});


const navBarHeight = ref(0)

const toSetting = () => {
  uni.navigateTo({
    url: '/pages/dialogue/view/settingView',
    animationType: 'pop-in',
    animationDuration: 200
  })

}




onLoad(() => {
  //获取小程序胶囊的高度
  let {
    top
  } = uni.getMenuButtonBoundingClientRect()
  //高度 =（胶囊底部高低-状态栏高度）+（胶囊顶部高度-状态栏内的高度）
  navBarHeight.value = top -15
})

</script>


<template>
  <view class="container" :style="{'padding-top':navBarHeight +'px'}">
    <view class="setting" @click="toSetting" >
      <image src="/static/icon/setting.svg"/>
    </view>
    <!--    占位-->
    <view class="occupancy">
      <view/>
    </view>
  </view>
</template>


<style scoped>

.container {
  color: #ffffff;
  position: fixed;
  display: flex;

  z-index: 2;

  align-items: flex-start;

}

.tips-below {
  font-size: 16rpx;
  color: #808080;
  padding-top: 5rpx
}

.version-below {
  color: #9e9e9e
}

.text {
  width: 515rpx;
  text-align: center;
  font-size: 30rpx;
  font-weight: 550;
}

.setting {
  margin-top: 15rpx;
  width: 100rpx;

}

.setting image {
  width: 40rpx;
  height: 40rpx;
  color: #ffffff
}

.occupancy {
  width: 100rpx;

}

.occupancy view {
  width: 40rpx;
  height: 40rpx;
  color: #666464;

}
</style>
