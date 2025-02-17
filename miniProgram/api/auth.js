import request from './../utils/request';

export function reqWechatLogin(data) {
    return request({
        url: '/auth/wechat/login',
        method: 'POST',
        data
    })
}
