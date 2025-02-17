export default {
    data() {
        return {
            // 默认的全局分享内容
            share: {
                title: '欢迎使用TYPE STARS'
            }
        }
    },
    // 定义全局分享
    // 1.发送给朋友
    onShareAppMessage(res) {
        return {
            title: this.share.title
        }
    },
    //2.分享到朋友圈
    onShareTimeline(res) {
        return {
            title: this.share.title
        }
    },
}
