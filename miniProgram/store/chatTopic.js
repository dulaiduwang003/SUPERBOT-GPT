const key = 'CHAT_TOPIC';

export function getChatTopic() {
    const storageSync = uni.getStorageSync(key);
    if (storageSync) {
        return JSON.parse(storageSync)
    }
    return undefined;
}


export function setChatTopic(data) {
    return uni.setStorageSync(key, JSON.stringify(data))
}
