import {axios} from '../utils/request'
import {IMAGE_MODULE} from './_prefix'

// 上传图片文件
export const uploadImage = (payload: any) => {
    return axios.post(`${IMAGE_MODULE}`, payload, {
        headers: {
            'Content-Type': "multipart/form-data;"
        }
    })
        .then(res => {
            return res
        })
}