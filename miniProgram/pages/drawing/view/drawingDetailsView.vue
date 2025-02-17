<script setup>
import {ref,nextTick} from 'vue'
import store from "../../../store";
import {onLoad} from '@dcloudio/uni-app'
import {conversionTime} from "../../../utils/date";
import LoadingPageComponent from "../../../components/loadingPageComponent.vue";
import BackPreviousComponent from "../../../components/backPreviousComponent.vue";
import LoadingDataComponent from "../../../components/loadingDataComponent.vue";
import EmptyDataComponent from "../../../components/emptyDataComponent.vue";
import TopButtonComponent from "../../../components/topButtonComponent.vue";
import {reqGetWorkflows} from "@/api/drawingWorkflows";
import {reqCreateComments, reqDeleteComments, reqGetCommentsPage} from "@/api/drawingComments";

const obj = ref({
  title: '',
  originalImage: '',
  nowImage: '',
  introduced: '',
  visited: undefined,
  createTime: '',
})

const workflowsId = ref(undefined)

const isLoading = ref(true)

const isLoadingComments = ref(false)

const barTop = ref(0)

const barHeight = ref(0)

const pageNum = ref(1)

const comments = ref([])

const input = ref('')

const isTopButtonShow = ref(false)

const scrollTop = ref(0)

const contentTop = ref(0)

const handlePageScroll = ((e) => {
  scrollTop.value = e.detail.scrollTop
  isTopButtonShow.value = e.detail.scrollTop > 600;
})

const handleBackToTop = () => {
  contentTop.value = scrollTop.value
  nextTick(() => {
    contentTop.value = 0
  })
}


const handleToPage = (url) => {
  if (!store.getters.userInfo) {
    uni.reLaunch({
      url: '/pages/user/user'
    })
    return
  }
  uni.navigateTo({
    url: url,
    animationType: 'pop-in',
    animationDuration: 200
  })
}

const handleGetCommentsPage = async () => {
  if (!isLoadingComments.value) {
    try {
      isLoadingComments.value = true
      const {data} = await reqGetCommentsPage({
        pageNum: pageNum.value,
        workflowsId: workflowsId.value
      });
      if (data.records && data.records.length > 0) {
        pageNum.value = pageNum.value + 1
        comments.value.push(...data.records);
      }
    } catch (e) {
      console.log(e)
    } finally {
      setTimeout(() => {
        isLoadingComments.value = false
      }, 500)
    }
  }
}

const handleScrollToLower = () => {
  handleGetCommentsPage()
}

const handleGetWorkflows = async (id) => {
  try {
    isLoading.value = true
    const {data} = await reqGetWorkflows(id)
    obj.value = data
  } catch (e) {
    uni.showToast({icon: 'none', duration: 1000, title: e.msg});
    uni.navigateBack()
  } finally {
    setTimeout(() => {
      isLoading.value = false
    }, 500)
  }

}


const handleSendComments = async () => {
  if (!store.getters.userInfo) {
    uni.reLaunch({
      url: '/pages/user/user'
    })
    return
  }
  if (!input.value.trim()) {
    uni.showToast({
      title: '评论内容不能为空',
      icon: 'none',
      duration: 2000
    })
    return
  }
  try {
    uni.showLoading({
      title: '正在回复 ing~',
      mask: true
    });
    await reqCreateComments({
      content: input.value,
      workflowsId: workflowsId.value
    });
    input.value = ''
    pageNum.value = 1
    comments.value = []
    await handleGetCommentsPage()
    uni.showToast({icon: 'none', duration: 3000, title: "发送成功!长按自己发布的评论可以删除哦!"});
  } catch (e) {
    console.log(e)
  } finally {
    setTimeout(() => {
      uni.hideLoading()
    }, 500)
  }

}

const handleDeleteComments = async (id, permissions, index) => {
  if (permissions) {
    uni.showModal({
      title: '提示',
      content: '确定删除这条评论？',
      success: async function (res) {
        if (res.confirm) {
          uni.showLoading({
            title: '正在删除 ing~',
            mask: true
          });
          try {
            await reqDeleteComments({
              workflowsCommentsId: id
            })
            comments.value.splice(index, 1)
            uni.hideLoading();
            uni.showToast({
              icon: 'none',
              duration: 2000,
              title: '删除成功'
            });
          } catch (e) {
            console.log(e)
          }
        }
      }
    });
  }
}

onLoad((option) => {
  workflowsId.value = Number(option.workflowsId)
  let menuButtonBoundingClientRect = uni.getMenuButtonBoundingClientRect();
  barTop.value = menuButtonBoundingClientRect.top
  barHeight.value = menuButtonBoundingClientRect.height
  handleGetCommentsPage()
  handleGetWorkflows(workflowsId.value)
})

</script>


<template>
  <view>
    <TopButtonComponent :is-show="isTopButtonShow" @click-top="handleBackToTop"/>
    <BackPreviousComponent :bar-height="barHeight" :bar-top="barTop"/>
    <LoadingDataComponent v-if="isLoadingComments" :top="200"/>
    <LoadingPageComponent v-if="isLoading"/>
    <scroll-view
        scroll-y="true"
        class="scroll"
        scroll-with-animation="true"
        lower-threshold="100"
        v-show="!isLoading"
        @scrolltolower="handleScrollToLower()"
        :scrollTop="contentTop"
        @scroll="handlePageScroll"
    >
      <view>
        <view class="card-image">
          <view class="card-image-box">
            <image
                mode="aspectFill"
                :src="obj.originalImage"
            />
          </view>
          <view class="card-image-box">
            <image
                mode="aspectFill"
                :src="obj.nowImage"
            />
          </view>
          <view class="arrow">
            <image src="/static/icon/rightArrow.svg"/>
          </view>
        </view>
        <view class="textContainer">
          <view class="title">{{ obj.title }}</view>
          <view class="cardTextBox">
            <view class="cardTextBoxChild">
              <view>
                <uni-icons type="paperplane" size="30rpx" color="#a6a6a6"></uni-icons>
                <view class="paddingLeft10">发布于{{ conversionTime(obj.createTime) }}</view>
              </view>
              <view class="marginLeft20">
                <uni-icons type="star-filled" size="30rpx" color="#a6a6a6"></uni-icons>
                <view class="paddingLeft10">{{ obj.visited }}人在玩</view>
              </view>
            </view>
          </view>
          <view class="introduced">
            {{
              obj.introduced
            }}
          </view>
          <view class="btn" @click="handleToPage('/pages/drawing/view/drawingFormView?workflowsId=' + obj.workflowsId)">
            开始制作
          </view>
          <view class="driver"/>
          <view>
            <view class="comments">
              <uni-icons type="chat" size="55rpx" color="white"></uni-icons>
              <view class="commentsText">评论</view>
            </view>
            <view class="commentsInput">
              <view>
                <image class="commentsAvatar"
                       :src="store.getters.userInfo &&  store.getters.userInfo.avatar  ? store.getters.userInfo.avatar  :'/static/avatar/default_user.png'"/>
              </view>
              <view>
                <input v-model="input" placeholder="留下我的看法" type="text" class="commentsInputText" maxlength="100"
                       confirm-type="send" @confirm="handleSendComments"/>
              </view>
              <view class="commentsSend" @click="handleSendComments">
                发送
              </view>
            </view>
            <view class="commentSection" v-if="comments&&comments.length>0">
              <view class="commentView" v-for="(item,index) in comments" :key="index"
                    @longpress="handleDeleteComments(item.workflowsCommentsId,item.isCurrentUser,index)">
                <view>
                  <image class="commentsAvatar"
                         :src="item.avatar  ?item.avatar  :'/static/avatar/default_user.png'"/>
                </view>
                <view class="commentName">
                  <view>
                    {{ item.nickName }}
                  </view>
                  <view class="commentContent">
                    {{ item.content }}
                  </view>
                  <view class="commentDate">
                    {{ conversionTime(item.createTime) }}
                  </view>
                </view>
              </view>
            </view>
            <EmptyDataComponent :msg="'评论区空空如也'" :height="30" v-else/>
          </view>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<style scoped lang="scss">
.comments {
  display: flex;
  align-items: center
}

.commentsText {
  font-size: 35rpx;
  padding-left: 10rpx
}

.commentSection {
  margin-top: 70rpx;
  margin-bottom: 100rpx
}

.commentView {
  display: flex;
  align-items: flex-start;
  margin-top: 30rpx;
  animation: slideEase 0.5s ease-in-out forwards;
}

.commentName {
  padding-left: 20rpx;
  color: #c8c8c8;
  font-size: 28rpx
}

.commentContent {
  padding-top: 25rpx;
  color: #cacaca
}

.commentDate {
  padding-top: 20rpx;
  color: #867c7c;
  font-size: 25rpx
}

.commentsInput {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-top: 40rpx
}

.commentsAvatar {
  border-radius: 100%;
  width: 70rpx;
  height: 70rpx
}

.commentsInputText {
  font-size: 28rpx;
  font-weight: normal;
  border: 1rpx solid #3a3a3a;
  background: #222423;
  width: 380rpx;
  padding: 15rpx;
  border-radius: 10rpx
}

.commentsSend {
  color: #9c9c9c;
  background: #222423;
  padding: 17rpx 45rpx;
  border-radius: 10rpx;
  font-size: 28rpx
}

.btn {
  background: linear-gradient(120deg, rgb(250, 52, 54) 1%, rgb(186, 42, 107) 66.24%, rgb(184, 54, 189) 97.8%);;
  animation: gradient 4s ease infinite;
  background-size: 200% 200%;
  padding: 30rpx;
  border-radius: 20rpx;
  color: white;
  margin-top: 40rpx;
  text-align: center;
  font-size: 36rpx;
  font-weight: bold;
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

.introduced {
  margin-top: 20rpx;
  font-size: 26rpx;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  height: 100rpx;
  line-height: 1.4;

}

.title {
  font-size: 46rpx
}

.driver {
  width: 100%;
  background: #393939;
  height: 2rpx;
  margin-top: 100rpx;
  margin-bottom: 40rpx;
}

.textContainer {

  padding: 40rpx;
  color: white;
}

.card-image {
  display: flex;
  position: relative;
  box-shadow: 0 -50px 30px -10px #161817 inset;
}

.card-image-box {
  width: 50%;
}

.card-image-box image {
  width: 100%;
  height: 540rpx;
  position: relative;
  z-index: -3;
  object-fit: cover;
}

.scroll {
  height: 100vh;
  flex-grow:1;
  overflow-y:auto;
}

.arrow {
  position: absolute;
  top: 45%;
  left: 47%;
  z-index: 2
}

.arrow > image {
  width: 50rpx;
  height: 50rpx
}

.cardTextBox {
  font-size: 26rpx;
  margin-top: 60rpx;
  color: #a6a6a6;
  display: flex;
  justify-content: space-between;
  align-items: center
}

.cardTextBoxChild {
  display: flex
}

.cardTextBoxChild > view {
  display: flex;
  align-items: center
}

.marginLeft20 {
  margin-left: 20rpx
}

.paddingLeft10 {
  padding-left: 10rpx
}

@keyframes slideEase {
  0% {
    transform: translateX(-100px);
  }
  100% {
    transform: translateX(0);
  }
}
</style>
