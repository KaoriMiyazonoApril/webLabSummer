import {axios} from '../utils/request'
import {COMMENT_MODULE} from './_prefix'
import type {AxiosRequestConfig} from "axios";
export type CommentInfo = {
    id: number,
    activityId:number,
    userId:number,
    detail:string,
    score:number
}

export const addComment=(commentInfo:CommentInfo)=>{
    const requestPayload = {
        id: commentInfo.id,
        account: { id: commentInfo.userId },  // 使用 userId 填充 AccountVO
        activity: { id: commentInfo.activityId },  // 使用 activityId 填充 ActivityVO
        detail: commentInfo.detail,
        score: commentInfo.score,
    };

    return axios.post(`${COMMENT_MODULE}/add`,requestPayload, {
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error("Failed to add comment:", err);
            throw err;
        });
}

export const deleteComment=(userId:number,activityId:number)=>{
    if(!activityId || !userId){
        console.error("Both two ids are required.");
        return Promise.reject(new Error("ActivityId or userId cannot be empty"));
    }

    return axios.delete(`${COMMENT_MODULE}/delete/${userId}/${activityId}`, {
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error("Failed to delete comment:", err);
            throw err;
        });
}

export const getAvgScoreById=(activityId:number)=>{
    if(!activityId){
        console.error("ActivityId is required.");
        return Promise.reject(new Error("ActivityId cannot be empty"));
    }

    return axios.get(`${COMMENT_MODULE}/avg/${activityId}`, {
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error(`Failed to get comment of activity ${activityId}:`, err);
            throw err;
        });
}

export const getCommentById=(activityId:number)=>{
    if(!activityId){
        console.error("ActivityId is required.");
        return Promise.reject(new Error("ActivityId cannot be empty"));
    }
    return axios.get(`${COMMENT_MODULE}/${activityId}`, {
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error(`Failed to get average score of activity ${activityId}:`, err);
            throw err;
        });
}