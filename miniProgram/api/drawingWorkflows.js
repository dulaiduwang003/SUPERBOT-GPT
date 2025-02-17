import request from './../utils/request';
import env from "../env";

export function reqGetWorkflowsCategoryList() {
    return request({
        url: '/drawing-workflows/get/workflows/category/list',
        method: 'get'
    })
}


export function reqGetWorkflowsInterface(data) {
    return request({
        url: '/drawing-workflows/get/workflows/interface?workflowsId=' + data,
        method: 'get'
    })
}

export function reqGetWorkflows(data) {
    return request({
        url: '/drawing-workflows/get/workflows?workflowsId=' + data,
        method: 'get'
    })
}

export function reqGetWorkflowsPage(data) {
    return request({
        url: '/drawing-workflows/get/workflows/page?pageNum=' + data.pageNum + '&prompt=' + data.prompt + '&workflowsCategoryId=' + data.workflowsCategoryId,
        method: 'get',
        data
    })
}

export function reqGetUploadWorkflowsComponentFile() {
    return env.baseHttps + "/drawing-workflows/upload/file"
}
