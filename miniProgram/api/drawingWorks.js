import request from './../utils/request';

export function reqDeleteWorkflowsWorks(data) {
    return request({
        url: '/drawing-works/delete/works',
        method: 'POST',
        data
    })
}

export function reqGetWorkflowsWorksPage(data) {
    return request({
        url: '/drawing-works/get/works/page?pageNum=' + data,
        method: 'GET'
    })
}

export function reqGetWorkflowsWorks(data) {
    return request({
        url: '/drawing-works/get/works?workflowsWorksId=' + data,
        method: 'GET'
    })
}
