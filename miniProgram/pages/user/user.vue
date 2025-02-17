<script setup>
import UserInfoComponent from "./component/userInfoComponent.vue";
import LoginComponent from "./component/loginComponent.vue";

import WorkspaceComponent from "./component/workspaceComponent.vue";
import {setTokenValue} from "@/store/token";
import store from "@/store";
import {onLoad, onUnload} from '@dcloudio/uni-app'
import {nextTick, ref} from 'vue'

import {reqWechatLogin} from "@/api/auth";
import {reqGetWorkflowsWorksPage} from "@/api/drawingWorks";
import LoadingDataComponent from "../../components/loadingDataComponent.vue";
import TopButtonComponent from "../../components/topButtonComponent.vue";
import {reqGetTaskProgressList} from "@/api/drawingTask";
import env from "@/env";
import {reqRewarded} from "@/api/ad";


let videoAd= null

const isLoginLoading = ref(false)

const isWorkflowsWorksLoading = ref(false)

const workspaceActive = ref(0)

const workflowsWorksPageNum = ref(1)

const workflowsWorksData = ref([])

const intervalId = ref(null)

const isTaskStop = ref(false)

const drawingTaskData = ref([])

const isTopButtonShow = ref(false)

const workspaceRef = ref(null)

const scrollTop = ref(0)

const contentTop = ref(0)

const handleAdLoad = ()=>{
  if (wx.createRewardedVideoAd) {
	  console.log("加载广告")
    videoAd = wx.createRewardedVideoAd({
      adUnitId: env.adId
    });
    videoAd.onError(err => {
      console.log(err);
    });

    videoAd.onClose(async (status) => {
      if (status && status.isEnded || status === undefined) {
        try {
         await reqRewarded();
          uni.showToast({
            title: '奖励已发放',
            icon: 'none'
          });
          await store.dispatch('fetchUserInfo')
        } catch (e) {
          console.log(e)
          uni.showToast({
            title: '获取奖励失败',
            icon: 'none'
          });
        }

      } else {
        uni.showToast({
          icon: 'none',
          duration: 3000,
          title: `请重新观看视频获得奖励`
        });
      }
    });
  }
}

const handleScrollToLower = () => {
  if (workspaceActive.value === 0) {
    //刷新作品
    handleGetWorkflowsWorksPage()
  }
}

const handleBackToTop = () => {
  contentTop.value = scrollTop.value
  nextTick(() => {
    contentTop.value = 0
  })
}

const handlePageScroll = ((e) => {
  scrollTop.value = e.detail.scrollTop
  isTopButtonShow.value = e.detail.scrollTop > 600;
})

const handleChangeWorkspaceActive = (e) => {
  workspaceActive.value = e
}

const handleGetWorkflowsWorksPage = async () => {
  if (!isWorkflowsWorksLoading.value) {
    try {
      isWorkflowsWorksLoading.value = true
      const {data} = await reqGetWorkflowsWorksPage(workflowsWorksPageNum.value);
      if (data.records && data.records.length > 0) {
        workflowsWorksPageNum.value = workflowsWorksPageNum.value + 1
        let map = handleTreatmentWorkflowsWorks(data);
        workflowsWorksData.value.push(...map);
      }
    } catch (e) {
      console.log(e)
    } finally {
      setTimeout(() => {
        isWorkflowsWorksLoading.value = false
      }, 1000)
    }
  }
}

const handleTreatmentWorkflowsWorks = (data) => {
  return data.records.map(c => {
    if (c.resultType === 'VIDEO') {
      return {
        workflowsWorksId: c.workflowsWorksId,
        image: c.image += '?x-oss-process=video/snapshot,t_1,f_jpg',
        resultType: c.resultType
      }
    } else if (c.resultType === 'MODEL') {
      return {
        workflowsWorksId: c.workflowsWorksId,
        image: '/static/svg/model.svg',
        resultType: c.resultType
      }

    } else if (c.resultType === 'AUDIO') {
      return {
        workflowsWorksId: c.workflowsWorksId,
        image: '/static/svg/music.svg',
        resultType: c.resultType
      }
    } else if(c.resultType === 'IMAGE') {
      return {
        workflowsWorksId: c.workflowsWorksId,
        image: c.image+'?x-oss-process=image/resize,m_lfit,w_300,h_300/quality,q_80',
        resultType: c.resultType
      }
    }
    return c
  });
}

const handleWechatLogin = () => {
  uni.vibrateShort()
  isLoginLoading.value = true
  uni.login({
    async success(res) {
      try {
        const {data} = await reqWechatLogin({
          code: res.code
        })
        setTokenValue(data)
        await store.dispatch('fetchUserInfo')
        //刷新作品
        await handleGetWorkflowsWorksPage()
		await handleListeningTask()
        uni.showToast({icon: 'none', duration: 3000, title: "欢迎使用 TYPE STARTS"});
      } catch (e) {
        uni.showToast({
          icon: 'none',
          duration: 6000,
          title: e.msg
        });
      } finally {
        isLoginLoading.value = false
      }
    }
  })

}

const handleListeningTask = async () => {
  intervalId.value = setInterval(async () => {
    if (!isTaskStop.value) {
      isTaskStop.value = true
      try {
        const {data} = await reqGetTaskProgressList();
        drawingTaskData.value = data.map(c => {
	
          if (c.resultType === 'VIDEO') {
            c.url += '?x-oss-process=video/snapshot,t_1,f_jpg'
          } else if (c.resultType === 'MODEL') {
            c.url = '/static/svg/model.svg'
          } else if (c.resultType === 'AUDIO') {
            c.url = '/static/svg/audio.svg'
          }
          return c
        })
      } catch (e) {
        console.log(e)
      } finally {
        isTaskStop.value = false
      }
    }
  }, 3000);
}

const handleRewardedAds = async () => {
  if (videoAd) {
    videoAd.show().catch(() => {
      // 广告拉取失败，重试
      videoAd.load().then(() => {
        videoAd.show();
      });
    })
  }
}

onLoad(async () => {
  handleAdLoad()
  if (store.getters.userInfo) {
    await handleListeningTask()
    await store.dispatch('fetchUserInfo')
    await handleGetWorkflowsWorksPage()
  }

  uni.$on("WORKFLOWS_WORKS:REFRESH", async () => {
    workflowsWorksPageNum.value = 1
    workflowsWorksData.value = []
    workspaceRef.value.handleWorkflowsWorksRefresh()
    if (!isWorkflowsWorksLoading.value) {
      try {
        isWorkflowsWorksLoading.value = true
        const {data} = await reqGetWorkflowsWorksPage(workflowsWorksPageNum.value);
        if (data.records && data.records.length > 0) {
          workflowsWorksPageNum.value = workflowsWorksPageNum.value + 1
          let map = handleTreatmentWorkflowsWorks(data);
          workflowsWorksData.value.push(...map);
          workspaceRef.value.handleWorkflowsWorksRefresh()
        }
      } catch (e) {
        console.log(e)
      } finally {
        setTimeout(() => {
          isWorkflowsWorksLoading.value = false
        }, 1000)
      }
    }
  })
})

onUnload(() => {
  clearInterval(intervalId);
  uni.$off("WORKFLOWS_WORKS:REFRESH")
})

</script>


<template>
  <view>
    <LoginComponent v-show="!(store.getters.userInfo)" :isLoading="isLoginLoading" @login="handleWechatLogin"/>
    <TopButtonComponent :isShow="isTopButtonShow" @clickTop="handleBackToTop"/>
    <LoadingDataComponent :is-loading="isWorkflowsWorksLoading"/>
    <scroll-view v-show="store.getters.userInfo" id="scroll" :scrollTop="contentTop" class="scroll" lower-threshold="10"
                 scroll-with-animation="true"
                 scroll-y="true"
                 @scroll="handlePageScroll"
                 @scrolltolower="handleScrollToLower()"
    >
      <UserInfoComponent @rewardedAds="handleRewardedAds"/>
      <WorkspaceComponent
          v-if="store.getters.disguiseStatus"
          ref="workspaceRef"
          :drawingTaskData="drawingTaskData"
          :workflowsWorksData="workflowsWorksData"
          :workspaceActive="workspaceActive"
          @changeWorkspaceActive="handleChangeWorkspaceActive"
      />
    </scroll-view>
  </view>
</template>

<style lang="scss" scoped>

.scroll {
  height: 100vh;
}

.workspace {
  padding: 30rpx;
  display: flex;
  color: #909090;
  font-weight: bold;
  font-size: 25rpx
}

.workspace > view {
  margin-right: 30rpx
}

.workspace-underline {
  margin: 0 20rpx;
  border-bottom: 5rpx solid #fedbe2;
  padding-top: 10rpx
}
</style>
