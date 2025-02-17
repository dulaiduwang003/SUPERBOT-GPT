import env from '../env';
import {getTokenValue} from "@/store/token";
import store from "../store";


function service(options = {}) {
    options.url = `${env.baseHttps}${options.url}`;
    options.timeout = 100000;
    const tokenValue = getTokenValue();
    if (tokenValue) {
        options.header = {
            'content-type': 'application/json',
            'Authorization': `Bearer ${tokenValue}`
        };
    }

    return new Promise((resolve, reject) => {
        uni.request({
            ...options,
            success: function (res) {
                if (res.statusCode === 200) {
                    const response = res.data;

                    if (response.code === 200) {
                        resolve(response);
                    }else if (response.code === 401){
                        store.commit("logout");
                        uni.reLaunch({
                            url: '/pages/user/user'
                        })
                    }else {
                        reject(response);
                    }
                } else {
                    reject('与服务器建立连接失败');
                }
            },
            fail: function (e) {
                console.log(e)
                reject('请检查您的网络环境是否正常');
            }
        });
    });
}

export default service;
