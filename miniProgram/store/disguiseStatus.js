const key = 'DISGUISE_STATUS';

export function getDisguiseStatus() {
    const storageSync = uni.getStorageSync(key);
    if (storageSync) {
        return JSON.parse(storageSync)
    }
    return undefined;
}


export function setDisguiseStatus(data) {
    return uni.setStorageSync(key, JSON.stringify(data))
}
