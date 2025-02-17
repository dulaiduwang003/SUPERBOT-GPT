import App from './App'

// #ifndef VUE3
import Vue from 'vue'
import './uni.promisify.adaptor'
Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
  ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
import store from './store'
import share from './components/share/share'
import Vuex from "vuex";
export function createApp() {
  const app = createSSRApp(App)
  app.mixin(share)
  app.use(store)
  return {
    app,
    Vuex
  }
}
// #endif
