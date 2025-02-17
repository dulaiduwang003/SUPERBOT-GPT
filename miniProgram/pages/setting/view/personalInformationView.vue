<template>

  <view style="padding-top: 20vh; text-align: center; display: flex;justify-content: center;align-items: center;">
    <view>
      <view class="avatar" v-if="!store.getters.userInfo.avatar">
        <image src="/static/icon/camera.svg"/>
        <button open-type="chooseAvatar" @chooseavatar="uploadAvatar"
                class="avatar-button"
                :plain="true"></button>
      </view>
      <view class="user-avatar" v-else>
        <image :src="store.getters.userInfo.avatar"/>
        <button open-type="chooseAvatar" @chooseavatar="uploadAvatar"
                class="avatar-button"
                :plain="true"></button>
      </view>
    </view>
  </view>
  <view style="padding: 90rpx 160rpx 40rpx;">
    <input v-model="nickName"
           style="font-size: 35rpx; color: whitesmoke; border-bottom: 1rpx solid whitesmoke;text-align: center;padding-bottom: 20rpx"
           type="text" placeholder="请设置昵称" maxlength="8"/>
  </view>
  <view style="position: fixed;z-index: 100;bottom: 0;left: 0;width: 100%">
    <view class="btn" @click="onBlurNickName">
      修改昵称
    </view>
  </view>

</template>

<script setup>

import store from "@/store";
import env from "@/env";
import {getTokenValue} from "@/store/token";
import {reqUploadNickName} from "@/api/user";
import {onLoad} from '@dcloudio/uni-app'
import {ref} from 'vue'

const nickName = ref('')

const onBlurNickName = async (e) => {
  if (nickName.value.length > 0) {
    try {
      uni.showLoading({
        title: "加载中",
      });
      await reqUploadNickName({
        nickName: nickName.value,
      })
      await store.dispatch('fetchUserInfo')
      uni.showToast({
        title: '修改成功',
        icon: 'none',
        duration: 4000
      });
    } catch (e) {
      console.log(e)
    } finally {
      uni.hideLoading()
    }


  } else {
    uni.showToast({
      title: '用户昵称不能为空',
      icon: 'none',
      duration: 4000
    });
  }

}

const uploadAvatar = (e) => {
  uni.showLoading({
    title: "正在上传中",
  });
  wx.uploadFile({
    url: `${env.baseHttps}/user/upload/avatar`,  //服务器地址
    filePath: e.detail.avatarUrl,
    name: "file",
    header: {
      'Authorization': 'Bearer ' + getTokenValue()
    },
    async success(res) {
      const data = JSON.parse(res.data);
      if (data.code !== 200) {
        uni.showToast({icon: 'none', duration: 3000, title: data.msg});
        return
      }
      await store.dispatch('fetchUserInfo')
      uni.showToast({
        title: '上传头像成功',
        icon: 'none',
        duration: 2000
      })
    },
    fail(res) {
      uni.showToast({
        title: '上传头像失败,请稍后重试',
        icon: 'none',
        duration: 2000
      })
    }
  })
}

onLoad(() => {
  nickName.value = store.getters.userInfo.nickName
})

</script>


<style scoped lang="scss">

.user-avatar {
  border-radius: 100%;
  width: 150rpx;
  height: 150rpx;
  overflow: hidden;
  position: relative;
}

.user-avatar image {
  width: 100%;
  height: 100%
}

.btn {
  margin: 40rpx;
  background: linear-gradient(120deg, rgb(250, 52, 54) 1%, rgb(186, 42, 107) 66.24%, rgb(184, 54, 189) 97.8%);;
  animation: gradient 4s ease infinite;
  background-size: 200% 200%;
  padding: 30rpx;
  border-radius: 20rpx;
  color: white;

  text-align: center;
  font-size: 30rpx;

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

.avatar {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 100%;
  width: 180rpx;
  height: 180rpx;
  background-color: #3e3e3e
}

.avatar image {
  width: 70rpx;
  height: 70rpx
}

.avatar > view {
  border: 8rpx solid #000000;
  border-radius: 100%;

  width: 35rpx;
  height: 35rpx;
  position: absolute;
  z-index: 2;
  bottom: 0;
  right: 0
}

button::after {
  border: none;
}

.avatar-button {
  width: 150rpx;
  height: 150rpx;
  position: absolute;
  left: 0;
  top: 0;
  border: none;
}
</style>
