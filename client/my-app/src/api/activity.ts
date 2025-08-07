import {axios} from '../utils/request'
import {ACTIVITY_MODULE} from './_prefix'
import type {AxiosRequestConfig} from "axios";

type ActivityInfo = {
    id: number,
    name: string,
    detail:string,
    image: string,
    date:Date |undefined,
    cost: number,
    limitCount: number
}

export const createActivity=(activityInfo:ActivityInfo)=>{
    return axios.post(`${ACTIVITY_MODULE}/create`, activityInfo, {
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error("Failed to create activity:", err);
            throw err;
        });
}

export const deleteActivity=(activityId:number)=>{
    if (!activityId) {
        console.error("ActivityId is required.");
        return Promise.reject(new Error("ActivityId cannot be empty"));
    }

    return axios.delete(`${ACTIVITY_MODULE}/delete/${activityId}`, {
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error("Failed to delete activity:", err);
            throw err;
        });
}

export const alterActivity=(activityInfo:ActivityInfo)=>{
    return axios.put(`${ACTIVITY_MODULE}/alter`,activityInfo, {
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error("Failed to alter activity:", err);
            throw err;
        });
}

export const getActivityById=(activityId:number)=>{
    if (!activityId) {
        console.error("ActivityId is required.");
        return Promise.reject(new Error("ActivityId cannot be empty"));
    }

    return axios.get(`${ACTIVITY_MODULE}/get/${activityId}`,{
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error(`Failed to get activity with id ${activityId}:`, err);
            throw err;
        });
}

export const getActivityAvailable=()=>{
    return axios.get(`${ACTIVITY_MODULE}/available`,{
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error(`Failed to get activity available:`, err);
            throw err;
        });
}

export const getActivityFull=()=>{
    return axios.get(`${ACTIVITY_MODULE}/full`,{
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error(`Failed to get activity full:`, err);
            throw err;
        });
}

export const getActivityNotAvailable=()=>{
    return axios.get(`${ACTIVITY_MODULE}/notAvailable`,{
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error(`Failed to get activity expired:`, err);
            throw err;
        });
}

export const getActivityAll=()=>{
    return axios.get(`${ACTIVITY_MODULE}/all`,{
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error(`Failed to get all activities :`, err);
            throw err;
        });
}