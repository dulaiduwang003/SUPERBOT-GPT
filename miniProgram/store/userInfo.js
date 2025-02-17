const key = 'USER_INFO';

export function getUserInfo() {
    const storageSync = uni.getStorageSync(key);
    if (storageSync) {
        return JSON.parse(storageSync)
    }
    return undefined;
}

export function removeUserInfo() {
    uni.removeStorageSync(key)
}

export function setUserInfo(data) {
    return uni.setStorageSync(key, JSON.stringify(data))
}
