<template>
  <view>
	  <view class="body" v-show="store.getters.disguiseStatus">
	    <!--    导航-->
	    <navigation-component/>
	    <!--    区分显示内容-->
	    <view class="view-container">
	      <chat-component ref="chatRef"/>
	    </view>
	    <!--    新建对话-->
	    <levitation-component @new-chat-topic="newChatTopic"/>
	  </view>
	  <DialogueDisguiseComponent v-show="!(store.getters.disguiseStatus)"/>
  </view>
</template>

<script setup>
import {ref} from 'vue'

import ChatComponent from "/pages/dialogue/component/chatComponent";
import store from "/store";
import {reqGetModelList} from "/api/chatConfig";
import {onLoad, onUnload} from '@dcloudio/uni-app'
import LevitationComponent from "/pages/dialogue/component/levitationComponent.vue";
import {getChatTopic, setChatTopic} from "/store/chatTopic";
import {getTokenValue} from "/store/token";
import NavigationComponent from "./component/navigationComponent.vue";
import DialogueDisguiseComponent from "@/components/disguise/dialogueDisguiseComponent.vue";

const chatRef = ref(null)

const newChatTopic = () => {
  uni.vibrateShort()
  if (getTokenValue()) {
    const chatTopic = getChatTopic();
    const {index, array} = chatTopic
    const arrayElement = array[index];
    if (arrayElement.content.length <= 0) {
      uni.showToast({icon: 'none', duration: 3000, title: "当前已经是最新会话了"});
    } else {
      let now = new Date();
      let year = now.getFullYear(); // 获取当前年份
      let month = now.getMonth() + 1; // 获取当前月份，注意getMonth()返回的月份是从0开始的，所以需要加1
      let date = now.getDate(); // 获取当前日期

      month = month.toString().padStart(2, '0');
      date = date.toString().padStart(2, '0');

      let hours = now.getHours(); // 获取当前小时
      let minutes = now.getMinutes(); // 获取当前分钟


      hours = hours.toString().padStart(2, '0');
      minutes = minutes.toString().padStart(2, '0');


      let currentTime = hours + ':' + minutes;
      const id = generateRandom();
      chatTopic.array.unshift({
        id: id,
        title: '',
        date: year + '-' + month + '-' + date,
        time: currentTime,
        content: []
      })
      chatRef.value.html([])
      setChatTopic(chatTopic)
    }
  } else {
    uni.reLaunch({
      url: '/pages/user/user'
    })

  }
}

const generateRandom = () => {
  const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  let result = '';
  const charactersLength = characters.length;
  for (let i = 0; i < 12; i++) {
    result += characters.charAt(Math.floor(Math.random() * charactersLength));
  }
  return result;
}

const templateHTML = (data) => {
  chatRef.value.html(data)
}

const init = async () => {
  if (!store.getters.userSetting) {
    try {
      const {data} = await reqGetModelList();
      store.commit("setUserSetting", {
        model: data[0].model
      });
    } catch (e) {
      uni.showToast({icon: 'none', duration: 3000, title: "初始化用户数据失败"});
    }
  }
  if (!getTokenValue()) {
    return
  }
  const chatTopic = getChatTopic();
  if (!chatTopic) {
    let now = new Date();
    let year = now.getFullYear(); // 获取当前年份
    let month = now.getMonth() + 1; // 获取当前月份，注意getMonth()返回的月份是从0开始的，所以需要加1
    let date = now.getDate(); // 获取当前日期

    month = month.toString().padStart(2, '0');
    date = date.toString().padStart(2, '0');

    let hours = now.getHours(); // 获取当前小时
    let minutes = now.getMinutes(); // 获取当前分钟


    hours = hours.toString().padStart(2, '0');
    minutes = minutes.toString().padStart(2, '0');


    let currentTime = hours + ':' + minutes;
    const id = generateRandom();
    setChatTopic({
      index: 0,
      array: [
        {
          id: id,
          title: '',
          date: year + '-' + month + '-' + date,
          time: currentTime,
          content: []
        }
      ]
    })
  } else {
    const {index, array} = chatTopic
    const arrayElement = array[index];
    setTimeout(() => {
      templateHTML(arrayElement.content)
    }, 500)
  }

}


onLoad(() => {
  //初始化
  init()
  //构建函数
  uni.$on('dialogue_template_html', function (data) {
    templateHTML(data)
  })
})

onUnload(() => {
  uni.$off('dialogue_template_html')
})

</script>


<style scoped lang="scss">
.body {
  padding: 30rpx;
}

.view-container {
  color: white;
  padding-top: 10vh;
  flex-direction: column;
  flex: 1;
}

</style>
