import request from './../utils/request';

export function reqGetCurrentUserInfo() {
    return request({
        url: '/user/get/userInfo',
        method: 'GET'
    })
}


export function reqUploadNickName(data) {
    return request({
        url: '/user/upload/nickName',
        method: 'post',
        data
    })
}
