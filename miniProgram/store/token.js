const key = 'TOKEN_VALUE';

export function getTokenValue() {
    const storageSync = uni.getStorageSync(key);
    if (storageSync) {
        return JSON.parse(storageSync)
    }
    return undefined;
}

export function removeTokenValue() {
    uni.removeStorageSync(key)
}

export function setTokenValue(data) {
    return uni.setStorageSync(key, JSON.stringify(data))
}
