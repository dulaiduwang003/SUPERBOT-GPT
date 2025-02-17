import {createStore} from 'vuex'
import {getTokenValue, removeTokenValue} from "/store/token";
import {getUserInfo, removeUserInfo, setUserInfo} from "/store/userInfo";
import {getUserSetting, removeUserSetting, setUserSetting} from "./userSetting";
import {reqGetCurrentUserInfo} from "@/api/user";
import {reqGetDisguiseStatus} from "@/api/disguise";
import {getDisguiseStatus, setDisguiseStatus} from "@/store/disguiseStatus";

const store = createStore({
    state: {
        userInfo: undefined,
        userSetting: undefined,
        disguiseStatus: undefined,
    },
    getters: {
        userInfo: (state) => state.userInfo,
        userSetting: (state) => state.userSetting,
        disguiseStatus: (state) => state.disguiseStatus,
    },
    mutations: {
        logout(state) {
            state.userInfo = undefined
            removeTokenValue()
        },
        setDisguiseStatus(state, info) {
            state.disguiseStatus = info;
            setUserSetting(info)
        },
        setUserInfo(state, info) {
            state.userInfo = info;
            setUserInfo(info)
        },
        setUserSetting(state, info) {
            state.userSetting = info;
            setUserSetting(info)
        },
        initState(state) {
            let tokenValue = getTokenValue();
            if (tokenValue) {
                state.userInfo = getUserInfo();
                state.usetSetting = getUserSetting();
                state.disguiseStatus = getDisguiseStatus();
            }
        },
    },
    actions: {
        async fetchUserInfo({commit}) {
            try {
                const res = await reqGetCurrentUserInfo();
                commit("setUserInfo", res.data);
            } catch (error) {
                uni.showToast({
                    icon: 'none',
                    duration: 1500,
                    title: '获取用户信息失败'
                });
            }
        },
        async fetchDisguiseStatus({commit}) {
            try {
                let {data} = await reqGetDisguiseStatus();
                commit("setDisguiseStatus", data);
            }catch (e) {
                setDisguiseStatus(false)
            }
        },
    },
})

export default store
