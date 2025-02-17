import request from './../utils/request';

export function reqGetModelList() {
    return request({
        url: '/chat-config/get/model/list',
        method: 'GET'
    })
}
