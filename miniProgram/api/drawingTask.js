import request from './../utils/request';
import env from "../env";


export function reqGetTaskProgress(data) {
    return request({
        url: '/drawing-task/get/task/progress?taskId=' + data,
        method: 'GET'
    })
}


export function reqGetTaskProgressList() {
    return request({
        url: '/drawing-task/get/task/progress/list',
        method: 'GET'
    })
}

export function reqSubmitTask(data) {
    return request({
        url: '/drawing-task/submit/task',
        method: 'POST',
        data
    })
}
