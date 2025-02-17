<script setup>
import {onLoad} from '@dcloudio/uni-app'
import {ref} from 'vue'
import {reqGetModelList} from "/api/chatConfig";
import store from "/store";

const modelList = ref([])

onLoad(() => {
  getModelList()
})

const change = async (index) => {

  try {
    uni.vibrateShort()
    uni.showLoading({
      title: "正在加载中",
    });
    const valueElement = modelList.value[index];
    const userSetting = store.getters.userSetting;
    userSetting.model = valueElement.model
    store.commit("setUserSetting", userSetting);

  } catch (e) {
    console.log(e)
  } finally {
    uni.hideLoading()
  }

}

const getModelList = async () => {

  try {
    uni.showLoading({
      title: "正在加载中",
    });
    const {data} = await reqGetModelList();
    modelList.value = data
  } catch (e) {
    console.log(e)
  } finally {
    uni.hideLoading()
  }

}

</script>


<template>

  <view class="container">
    <view class="box" v-for="(item,index) in modelList" :key="index">
      <view class="mount">
        <view class="box-name">
          {{ item.name }}
        </view>
        <view class="box-button"
              @click="change(index)"
              v-if="
            store.getters.userSetting
            &&
            store.getters.userSetting.model!==item.model
            ">
          <view>
            使用
          </view>
        </view>
        <view v-else class="applied">
          使用中
        </view>
      </view>

    </view>
  </view>
</template>


<style scoped lang="scss">
.text-ellipsis {
  margin-top: 20rpx;
  font-size: 24rpx;
  color: #6d6d6d;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}

.container {
  padding: 30rpx
}

.box {
  margin-bottom: 30rpx;
  color: white;
  font-weight: 660;
  background: #212121;
  border-radius: 20rpx;
  padding: 30rpx;

}

.mount {
  display: flex
}

.box-change {
  font-size: 50rpx;
  font-weight: 550;
  color: white;
  display: flex;
  align-items: center
}

.box-name {
  width: 70%;
  font-size: 30rpx;
}

.box-button {
  width: 30%;
  display: flex;
  justify-content: flex-end
}

.box-button > view {
  background: #373737;
  padding: 6rpx 20rpx;
  font-size: 25rpx;
  border-radius: 10rpx;
  justify-content: center;
  align-items: center
}

.applied {
  width: 30%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  font-weight: 500;
  color: #717171;
  font-size: 26rpx
}
</style>
