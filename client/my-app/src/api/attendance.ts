import {axios} from '../utils/request'
import {ATTENDANCE_MODULE} from './_prefix'

export const attendActivity=(userId:number,activityId:number)=>{
    if (!activityId || !userId) {
        console.error("Both two ids are required.");
        return Promise.reject(new Error("ActivityId or accountId cannot be empty"));
    }

    const requestPayload = {
        id: 0,
        account: { id: userId },  // 使用 userId 填充 AccountVO
        activity: { id: activityId },  // 使用 activityId 填充 ActivityVO
        orderDate: new Date()  // 设置当前日期作为订单日期
    };

    return axios.put(`${ATTENDANCE_MODULE}/attend`, requestPayload,{
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error(`User ${userId} Failed to attend activity ${activityId}:`, err);
            throw err;
        });
}

export const cancelActivity=(userId:number,activityId:number)=>{
    if (!activityId || !userId) {
        console.error("Both two ids are required.");
        return Promise.reject(new Error("ActivityId or accountId cannot be empty"));
    }

    return axios.delete(`${ATTENDANCE_MODULE}/cancel/${userId}/${activityId}`, {
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error(`User ${userId} Failed to cancel activity ${activityId}:`, err);
            throw err;
        });
}

//参加这个活动的人
export const getMember=(activityId:number)=>{
    if (!activityId) {
        console.error("ActivityId is required.");
        return Promise.reject(new Error("ActivityId cannot be empty"));
    }

    return axios.get(`${ATTENDANCE_MODULE}/member/${activityId}`, {
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error(`Failed to get people who attend activity ${activityId}:`, err);
            throw err;
        });
}

//个人参加的活动
export const getYourActivity=(userId:number)=>{
    if (!userId) {
        console.error("AccountId is required.");
        return Promise.reject(new Error("AccountId cannot be empty"));
    }

    return axios.get(`${ATTENDANCE_MODULE}/personal/${userId}`, {
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error(`Failed to get activities of User ${userId}:`, err);
            throw err;
        });
}

export const getBtnType=(userId:number,activityId:number)=>{
    if (!userId) {
        console.error("AccountId is required.");
        return Promise.reject(new Error("AccountId cannot be empty"));
    }
    if(!activityId){
        console.error("ActivityId is required.");
        return Promise.reject(new Error("ActivityId cannot be empty"));
    }

    return axios.get(`${ATTENDANCE_MODULE}/btnType/${userId}/${activityId}`, {
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error(`Failed to get button type`, err);
            throw err;
        });
}