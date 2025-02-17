import request from './../utils/request';

export function reqRewarded() {
    return request({
        url: '/ad/rewarded',
        method: 'POST'
    })
}

