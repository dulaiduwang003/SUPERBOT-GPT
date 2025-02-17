<template>
  <LoadingPageComponent v-if="isLoading"/>
  <view v-show="!isLoading">
    <BackPreviousComponent :bar-top="barTop" :bar-height="barHeight"/>
    <view class="container" v-if="obj.resultType!=='MODEL'">
      <image class="preview" mode="widthFix" :src="obj.url" @click="handlePreviewImage(obj.url)"
             v-if="obj.resultType==='IMAGE'"/>
      <video class="preview" :src="obj.url" v-if="obj.resultType==='VIDEO'||obj.resultType==='AUDIO'"/>
    </view>
    <view v-else>
      <xr-start disable-scroll
                :url="obj.url"
                :width="renderWidth"
                :height="renderHeight"
                style="width: 100%;height: 90vh;margin-top: 180rpx">
      </xr-start>
    </view>
    <view class="suspension">
      <view>
        <view class="operationView">
          <view @click="handleRebuild">
            <view>
              <uni-icons type="reload" size="40rpx" color="#d5d5d5"></uni-icons>
            </view>
            <view class="text">
              重新生成
            </view>
          </view>
          <view @click="handleDeleteWorkflowsWorks" v-if="obj.isCurrentUser">
            <view>
              <uni-icons type="trash" size="40rpx" color="#d5d5d5"></uni-icons>
            </view>
            <view class="text">
              删除作品
            </view>
          </view>
          <view @click="handleDownloadFile(obj.url)" v-if="obj.resultType!=='MODEL'">
            <view>
              <uni-icons type="download" size="40rpx" color="#d5d5d5"></uni-icons>
            </view>
            <view class="text">
              下载保存
            </view>
          </view>

        </view>
      </view>
    </view>
  </view>

</template>

<script setup>
import {ref} from 'vue'
import BackPreviousComponent from "../../../components/backPreviousComponent.vue";
import LoadingPageComponent from "../../../components/loadingPageComponent.vue";
import {onLoad} from '@dcloudio/uni-app'
import {reqDeleteWorkflowsWorks, reqGetWorkflowsWorks} from "@/api/drawingWorks";

const barTop = ref(0)

const barHeight = ref(0)

const isLoading = ref(false)

const renderWidth = ref(100)

const renderHeight = ref(100)

const obj = ref({
  workflowsWorksId: undefined,
  workflowsId: undefined,
  url: '',
  resultType: '',
  isCurrentUser: false
})

const handlePreviewImage = (url) => {
  uni.previewImage({
    urls: [url]
  });
}

const handleRebuild = () => {
  uni.navigateTo({
    url: '/pages/drawing/view/drawingDetailsView?workflowsId=' + obj.value.workflowsId,
    animationType: 'pop-in',
    animationDuration: 200
  })

}

const handleGetWorkflowsWorks = async (id) => {
  try {
    isLoading.value = true
    const {data} = await reqGetWorkflowsWorks(id);

    obj.value = data
  } catch (e) {
    console.log(e)
  } finally {
    setTimeout(() => {
      isLoading.value = false
    }, 1000)
  }
}

const handleDeleteWorkflowsWorks = async () => {
  uni.showModal({
    title: '提示',
    content: '确定删除这个作品?',
    success: async function (res) {
      if (res.confirm) {
        uni.showLoading({
          title: '正在删除 ing~',
          mask: true
        });
        try {
          await reqDeleteWorkflowsWorks({
            workflowsWorksId: obj.value.workflowsWorksId
          })
          uni.hideLoading();
          uni.$emit("WORKFLOWS_WORKS:REFRESH")
          uni.showToast({
            icon: 'none',
            duration: 1000,
            title: '删除成功'
          });

        } catch (e) {
          console.log(e)
        } finally {
          setTimeout(() => {
            uni.navigateBack()
          }, 500)
        }
      }
    }
  });
}

const handleSaveImage = (url) => {
  uni.showLoading({
    title: '请稍等',
    mask: true
  });
  uni.downloadFile({
    url: url,
    success: function (res) {
      uni.saveImageToPhotosAlbum({
        filePath: res.tempFilePath,
        success: function () {
          uni.hideLoading();
          uni.showToast({
            title: '保存成功',
            icon: 'none',
            duration: 4000
          })
        },
        fail(e) {
          console.log(e)
        },
        complete() {
          uni.hideLoading();
        }
      })
    }
  })
}

const handleSaveMedia = (url) => {
  uni.showLoading({
    title: '请稍等',
    mask: true
  });
  uni.downloadFile({
    url: url,
    success: function (res) {
      uni.saveVideoToPhotosAlbum({
        filePath: res.tempFilePath,
        success: function (data) {
          uni.hideLoading();
          uni.showToast({
            title: '保存成功',
            icon: 'none',
            duration: 4000
          })
        },
        fail(e) {
          console.log(e)
        },
        complete(res) {
          uni.hideLoading(); //隐藏 loading 提示框
        }
      })
    }
  })
}

const handleDownloadFile = (url) => {
  uni.getSetting({
    success(res) {
      if (!res.authSetting['scope.writePhotosAlbum']) {
        uni.authorize({
          scope: 'scope.writePhotosAlbum',
          success() {
            if (obj.resultType !== 'IMAGE') {
              handleSaveMedia(url)
            } else
              handleSaveImage(url)
          }
        })
      } else {
        handleSaveImage(url)
      }
    }
  })
}




onLoad((option) => {
  let menuButtonBoundingClientRect = uni.getMenuButtonBoundingClientRect();
  barTop.value = menuButtonBoundingClientRect.top - 93
  barHeight.value = menuButtonBoundingClientRect.height

  const dpi = uni.getWindowInfo().pixelRatio
  const width = uni.getWindowInfo().windowWidth
  const height = uni.getWindowInfo().windowHeight
  renderWidth.value = width * dpi
  renderHeight.value = height * dpi
  handleGetWorkflowsWorks(option.workflowsWorksId)

})
</script>


<style scoped lang="scss">
.container {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  margin-top: 180rpx;
  padding: 20rpx
}

.share {
  width: 100%;
  height: 100%;
  position: absolute;
  z-index: 999;
  left: 0;
  top: 0;
  background-color: unset;
}

.relative {
  position: relative;
}

.suspension {
  position: fixed;
  bottom: 30rpx;
  width: 100%;
  z-index: 9999;
}

button[plain] {
  border: 0
}

.suspension > view {
  display: flex;
  padding: 20rpx;
  color: #d5d5d5
}

.operationView {
  font-size: 24rpx;
  background: #323232;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20rpx 30rpx;
  border-radius: 40rpx;
  text-align: center;
}

.operationView > view {
  margin: 0 30rpx;
}

.text {
  padding-top: 10rpx
}

.preview {
  width: 100%;
  border-radius: 15rpx
}
</style>
