<script>

import store from "/store";

export default {
  onLaunch: async function () {
    store.commit("initState");
    await store.dispatch('fetchDisguiseStatus')
	await store.dispatch('fetchUserInfo')
  }
}
</script>


<style>
page {
  background-color: #161817 !important;
}

::-webkit-scrollbar {
  display: none;
}

/*每个页面公共css */
</style>
