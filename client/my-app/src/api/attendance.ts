import {axios} from '../utils/request'
import {ATTENDANCE_MODULE} from './_prefix'
import type {AxiosRequestConfig} from "axios";
type AttendanceInfo = {
    id: number,
    userId:number,
    activityId:number
}

export const attendActivity=(attendanceInfo:AttendanceInfo)=>{
    const requestPayload = {
        id: attendanceInfo.id,
        account: { id: attendanceInfo.userId },  // 使用 userId 填充 AccountVO
        activity: { id: attendanceInfo.activityId },  // 使用 activityId 填充 ActivityVO
        orderDate: new Date()  // 设置当前日期作为订单日期
    };

    return axios.put(`${ATTENDANCE_MODULE}/attend`, requestPayload,{
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error(`User ${attendanceInfo.userId} Failed to attend activity ${attendanceInfo.activityId}:`, err);
            throw err;
        });
}

export const cancelActivity=(accountId:number,activityId:number)=>{
    if (!activityId || !accountId) {
        console.error("Both two ids are required.");
        return Promise.reject(new Error("ActivityId or accountId cannot be empty"));
    }

    return axios.delete(`${ATTENDANCE_MODULE}/cancel/${accountId}/${activityId}`, {
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error(`User ${accountId} Failed to cancel activity ${activityId}:`, err);
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
export const getYourActivity=(accountId:number)=>{
    if (!accountId) {
        console.error("AccountId is required.");
        return Promise.reject(new Error("AccountId cannot be empty"));
    }

    return axios.get(`${ATTENDANCE_MODULE}/personal/${accountId}`, {
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error(`Failed to get activities of User ${accountId}:`, err);
            throw err;
        });
}