import request from './../utils/request';

export function reqGetDisguiseStatus() {
    return request({
        url: '/disguise/status',
        method: 'GET'
    })
}

