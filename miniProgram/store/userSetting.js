const key = 'USER_SETTING';

export function getUserSetting() {
    const storageSync = uni.getStorageSync(key);
    if (storageSync) {
        return JSON.parse(storageSync)
    }
    return undefined;
}

export function removeUserSetting() {
    uni.removeStorageSync(key)
}

export function setUserSetting(data) {
    return uni.setStorageSync(key, JSON.stringify(data))
}
