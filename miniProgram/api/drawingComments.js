import request from './../utils/request';
import env from "../env";


export function reqDeleteComments(data) {
    return request({
        url: '/drawing-comments/delete/comments',
        method: 'post',
        data
    })
}

export function reqCreateComments(data) {
    return request({
        url: '/drawing-comments/create/comments',
        method: 'post',
        data
    })
}

export function reqGetCommentsPage(data) {
    const {pageNum, workflowsId} = data
    return request({
        url: '/drawing-comments/get/comments/page?pageNum=' + pageNum + '&workflowsId=' + workflowsId,
        method: 'GET'
    })
}
