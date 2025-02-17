<script setup>
import store from "/store";
import mpHtml from '/components/mp-html/components/mp-html/mp-html'

import {onLoad} from '@dcloudio/uni-app'
import {defineExpose, getCurrentInstance, reactive, ref} from 'vue'
import env from "/env";
import {getTokenValue, removeTokenValue} from "/store/token";
import {getChatTopic, setChatTopic} from "/store/chatTopic";
import FilePreviewComponent from "./filePreviewComponent.vue";
import AiLoadingComponent from "./aiLoadingComponent.vue";
import UploadChooseComponent from "./uploadChooseComponent.vue";
import InputComponent from "./inputComponent.vue";


const instance = getCurrentInstance();
let isDisplaying = false;

const messageQueue = reactive([]);

const messageList = ref([])
const isLoading = ref(false)
const inputRef = ref(null)
const uploadRef = ref(null)
const scrollTop = ref(0)
const wssAddress = ref(env.baseWss + '/chat/' + getTokenValue())
const file = ref(undefined)


const send = (text) => {
  let files = file.value
  if (!getTokenValue()) {
    uni.reLaunch({
      url: '/pages/user/user'
    })
    return
  }

  messageList.value.push({
    issue: text,
    answer: '',
    isError: false,
    file: files ? files : undefined
  })
  isLoading.value = true
  file.value = undefined

  inputRef.value.clear()
  const index = messageList.value.length - 1;

  setTimeout(() => {
    scrollToBottom()
  }, 100)

  const conversionList = conversionDataFormat(index);
  const sendMessagesData = compressArray(conversionList, 10000);

  uni.connectSocket({
    url: wssAddress.value
  });


  uni.onSocketOpen(function () {
    uni.sendSocketMessage({
      data: JSON.stringify({
        messages: sendMessagesData,
        model: store.getters.userSetting.model,
        isFiltration: false
      })
    });
  });

  uni.onSocketMessage(function (res) {
    messageQueue.push({
      msg: res.data,
      index: index,
    });
    displayMessages();
  });

  uni.onSocketError(function () {
    uni.showToast({
      icon: 'none',
      duration: 1000,
      title: '当前登录信息已过期,请重新登录'
    });
    removeTokenValue()
    uni.reLaunch({
      url: '/pages/user/user'
    })
  });

  uni.onSocketClose(function () {
    socketClose(index)
  });
}


const stop = () => {
  messageQueue.value = []
  uni.closeSocket({
    success: function () {
    },
    fail: function (err) {

    }
  });

}

const previewFileOrImage = (src) => {
  // 定义常见的图片文件后缀
  const imageExtensions = ['png', 'jpg', 'jpeg', 'gif', 'bmp', 'webp', 'svg', 'ico'];

  // 从 URL 中提取文件名
  let filename = src.split('/').pop().split('?')[0].split('#')[0]; // 处理查询参数和哈希

  // 提取文件后缀
  let extension = filename.split('.').pop().toLowerCase();

  if (imageExtensions.includes(extension)) {
    uni.previewImage({
      urls: [src]
    });
    console.log(src)
  } else {
    uni.downloadFile({
      url: src, // 文件网络地址
      success: function (res) {
        if (res.statusCode === 200) {
          uni.openDocument({
            filePath: res.tempFilePath, // 下载后的文件路径
            success: function (openRes) {
              console.log('文件打开成功');
            }
          });
        }
      }
    });

  }
}

const socketClose = (index) => {
  let interval = setInterval(() => {
    let isError = false
    if (messageQueue.length === 0) {
      const answer = messageList.value[index].answer;
      if (!answer) {
        messageList.value.splice(index, 1)
        isError = true
      } else if (answer === "é") {
        messageList.value[index].isError = true
        messageList.value[index].answer = "当前会话出现了点问题,请稍后重试"
      }
      const chatTopic = getChatTopic();

      if (!chatTopic.array[chatTopic.index].title && !isError) {
        chatTopic.array[chatTopic.index].title = messageList.value[index].issue.substring(0, 20)
      }
      chatTopic.array[chatTopic.index].content = messageList.value
      inputRef.value.clear()
      isLoading.value = false
      setChatTopic(chatTopic)
      clearInterval(interval)
    }
  }, 50);
}

const displayMessages = () => {

  if (isDisplaying) {
    return; // 如果正在显示消息，则直接返回，等待下一次调用
  }
  isDisplaying = true;
  const message = messageQueue.shift(); // 取出队列中的第一个消息
  if (message) {
    let i = 0;
    const {index, msg} = message;

    // eslint-disable-next-line no-inner-declarations
    function displayNextCharacter() {
      const character = msg.charAt(i++);
      if (character) {
        messageList.value[index].answer += character;
        if (i === msg.length) { // 只有在内容更新时才滚动到最底部
          scrollToBottom()
          setTimeout(displayNextCharacter, 20); //平滑控制更新频率
        } else {
          setTimeout(displayNextCharacter, 20);
        }
      } else {
        isDisplaying = false; // 重置标志以便下次能够正确显示消息
        displayMessages(); // 显示下一条消息
      }
    }

    displayNextCharacter();
  } else {
    isDisplaying = false; // 重置标志以便下次能够正确显示消息
  }
}

const conversionDataFormat = (index) => {
  const messages = []
  messageList.value.forEach((c, itemIndex) => {
    const {isError, issue, answer, file} = c
    if (!isError) {
      messages.push({
        role: "user",
        content: file ? issue + '[' + file.fileName + '](' + file.fileUrl + ')' : issue
      })
      if (itemIndex !== index) {
        messages.push({
          role: "assistant",
          content: answer
        })
      }
    }
  })

  return messages;
}

const compressArray = (messages, threshold) => {
  const totalLength = messages.reduce((acc, cur) => acc + cur.content.length, 0);
  if (totalLength <= threshold) {
    return messages;
  }
  const compressed = [];
  for (let i = messages.length - 1; i >= 0; i--) {
    const currentLength = messages[i].content.length;
    if (currentLength <= threshold) {
      compressed.push(messages[i]);
      threshold -= currentLength;
    } else {
      compressed.push({
        role: messages[i].role,
        content: messages[i].content.substr(0, threshold),
      });
      break;
    }
  }

  return compressed.reverse()
}

const scrollToBottom = () => {
  uni.createSelectorQuery().in(instance).select('#scroll-content').boundingClientRect((dom) => {
    if (dom.height > 0) {
      scrollTop.value = dom.height
    }
  }).exec()
}

const showUpload = () => {
  if (getTokenValue()) {
    uploadRef.value.open()
  } else {
    uni.reLaunch({
      url: '/pages/user/user',
    })
  }
}

const chooseMessageFile = () => {
  wx.chooseMessageFile({   // 只能选取微信聊天框中的文件
    count: 1, // 最多选择一个文件
    type: "file", // 选择文件类型
    success: async function (res) {
      const {name, size, path} = res.tempFiles[0];
      await upload(path, name, size)
    },
  });
}

const chooseSystemImage = () => {
  uni.chooseImage({
    count: 1, // 最多只能选择一张图片
    success: async (res) => {

      const {size, path} = res.tempFiles[0];
      await upload(path, "图片", size)
    }
  });
}

const upload = (path, name, size) => {
  uni.showLoading({
    title: "正在上传中",
  });
  wx.uploadFile({
    url: `${env.baseHttps}/oss/upload/file`,  //服务器地址
    filePath: path,     //文件临时路径
    name: "file",
    header: {
      'Authorization': 'Bearer ' + getTokenValue(),
      "Content-Type": "multipart/form-data",
    },
    success: (res) => {
      const data = JSON.parse(res.data);
      if (data.code !== 200) {
        uni.showToast({icon: 'none', duration: 3000, title: data.msg});
        return
      }
      const number = Math.round(parseInt(size) / 1024);
      file.value = {
        fileName: name,
        fileSize: number,
        fileUrl: data.data.fileUrl,
        fileType: data.data.fileType
      }
      // 上传成功执行
      uni.hideLoading();
      uploadRef.value.close()
    }
  });
}

const html = (data) => {
  messageList.value = data
}

onLoad(() => {
  setTimeout(() => {
    scrollTop.value = 99999
  }, 800)
})

defineExpose(
    {html}
)

</script>


<template>
  <view class="body">
    <view class="icon" v-if="!(messageList.length>0)">
      <view>
        <image src="/static/avatar/default_gpt.svg"/>
      </view>
    </view>
    <scroll-view scroll-y :scrollTop="scrollTop" class="scroll" v-else>
      <view class="content" id="scroll-content">
        <view v-for="(item,index) in messageList" :key="index">
          <view class="slide-animation" style="margin-bottom: 40rpx">
            <view class="user-chat ">
              <view class="user-chat-background">
                <view>
                  {{ item.issue }}
                </view>
              </view>
              <image :src="
              store.getters.userInfo
    &&
    store.getters.userInfo.avatar
    ?
    store.getters.userInfo.avatar
    :
    '/static/avatar/default_user.png'
"/>
            </view>
            <view class="chat-file slide-animation" v-if="item.file">
              <file-preview-component
                  @preview="previewFileOrImage"
                  :file-name="item.file.fileName"
                  :file-type="item.file.fileType"
                  :file-url="item.file.fileUrl"
                  :file-size="item.file.fileSize"/>
            </view>

          </view>
          <view class="ai-chat slide-animation">
            <view class="ai-avatar">
              <image src="/static/avatar/default_gpt.svg"/>
            </view>
            <view class="ai-chat-container">
              <view class="ai-name">
                TYPE STARS
              </view>
              <view class="ai-chat-background" v-if="item.answer">
                <view class="ai-content">
                  <mp-html :content="item.answer" :selectable="true" :lazy-load="true" :copy-link="true"
                           :markdown="true"/>
                </view>
              </view>
              <view class="ai-loading" v-else>
                <ai-loading-component/>
              </view>
            </view>
          </view>
        </view>
      </view>
    </scroll-view>
  </view>
  <view v-if="file" class=" fixed-file slide-animation">
    <file-preview-component
        :file-name="file.fileName"
        :file-type="file.fileType"
        :file-size="file.fileSize"
    />
  </view>

  <upload-choose-component ref="uploadRef" @upload-file="chooseMessageFile" @upload-image="chooseSystemImage"/>

  <input-component
      ref="inputRef"
      @send="send"
      @stop="stop"
      @upload="showUpload"
      :is-prompt="!(messageList.length>0)"
      :is-loading="isLoading"

  />
</template>


<style scoped>
.fixed-file {
  position: fixed;
  top: 82vh;
  z-index: 999;
  left: 20rpx;
}

.user-chat {
  display: flex;
  justify-content: flex-end;
  color: black;

}

.chat-file {
  display: flex;
  justify-content: flex-end;
  margin-right: 90rpx;
  margin-top: 15rpx
}


.user-chat-background {
  font-size: 26rpx;
  max-width: 600rpx;
  background-color: #27b561;
  margin-right: 20rpx;
  border-radius: 20rpx;

}

.user-chat-background view {
  padding: 20rpx;
  word-wrap: break-word;
  color: black;
}

.user-chat image {
  width: 70rpx;
  height: 70rpx;
  border-radius: 100%
}

.ai-chat {
  display: flex;
  justify-content: flex-start;
  color: black;
  margin-bottom: 80rpx
}

.ai-avatar {
  margin-top: 8rpx;
  width: 70rpx;
  height: 70rpx;

  border-radius: 100%;
  display: flex;
  justify-content: center;
  align-items: center
}

.ai-avatar image {
  width: 70rpx;
  height: 70rpx
}

.ai-chat-container {
  margin-left: 20rpx;
}

.ai-name {
  font-weight: 550;
  margin-bottom: 10rpx;
  font-size: 23rpx;
  color: #5f5f5f
}

.ai-chat-background {
  font-size: 26rpx;
  max-width: 600rpx;
  background-color: rgb(40, 40, 40);
  border-radius: 20rpx;
  color: white
}

.ai-content {
  word-wrap: break-word;
  padding: 0 20rpx
}

.ai-loading {

  padding: 0 20rpx;
  background-color: rgb(40, 40, 40);

  border-radius: 20rpx;

}

.scroll {
  height: 78vh
}

.content {
  padding-bottom: 30rpx

}

.slide-animation {
  animation: slideEase 0.5s ease-in-out forwards;
}

@keyframes slideEase {
  0% {
    transform: translateX(-100px);
  }
  100% {
    transform: translateX(0);
  }
}

.icon {
  padding-top: 35vh;
  animation: fadeIn 0.5s ease-in-out forwards;
  display: flex;
  justify-content: center;
  align-items: center;

}

.icon > view {

  display: flex;
  justify-content: center;
  align-items: center;
  width: 75rpx;
  height: 75rpx;

  border-radius: 100%
}

.icon image {
  width: 75rpx;
  height: 75rpx;
}
</style>
