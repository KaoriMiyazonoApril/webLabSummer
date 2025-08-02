import {axios} from '../utils/request'
import {ACCOUNT_MODULE} from './_prefix'
import type {AxiosRequestConfig} from "axios";
type UserInfo = {
    id:number
    role: string,
    username: string,
    avatar: string,
    telephone: number,
    password: string,
}

type LoginInfo = {
    telephone: number,
    password: string
}

//如果有“Vue: This may be converted to an async function”警告，可以不管
//用户登录
export const userLogin = (loginInfo: LoginInfo) => {
    return axios.post(`${ACCOUNT_MODULE}/login`, loginInfo, {
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res  => {
            return res;
        })
        .catch(err => {
            console.error("Login failed:", err);
            throw err;
        });
};

// 用户注册
export const userRegister = (registerInfo: UserInfo) => {
    return axios.post(`${ACCOUNT_MODULE}`, registerInfo, {
        headers: { 'Content-Type': 'application/json' }
    })
        .then(res => {
            return res;
        })
        .catch(err => {
            console.error("Registration failed:", err);
            throw err;
        });
};


// 获取用户信息
export const userInfo = (userId:number) => {
    if (!userId) {
        console.error("UserId is required.");
        return Promise.reject(new Error("UserId cannot be empty"));
    }
    return axios.get(`${ACCOUNT_MODULE}/${userId}`)
        .then(res => {
            return res;
        })
        .catch(err => {
            console.error("Failed to fetch user info:", err);
            throw err;
        });
};

// 获取所有用户信息
export const allUserInfo = () => {
    return axios.get(`${ACCOUNT_MODULE}`)
        .then(res => {
            return res;
        })
        .catch(err => {
            console.error("Failed to fetch user info:", err);
            throw err;
        });
};

export const userInfoUpdate = (updateInfo: UserInfo,  config?: AxiosRequestConfig) => {
    // 将 updateInfo 和 username 合并为一个对象
    return axios.put(`${ACCOUNT_MODULE}`, updateInfo, {
        headers: {
            'Content-Type': 'application/json',
            ...config?.headers // 动态添加自定义请求头
        }
    })
        .then(res => {
            return res;
        })
        .catch(err => {
            console.error("Failed to update user info:", err);
            throw err;
        });
};


//删除用户
export const deleteUser = (userId: number, config?: AxiosRequestConfig) => {
    if (!userId) {
        console.error("UserId is required.");
        return Promise.reject(new Error("UserId cannot be empty"));
    }

    return axios.delete(`${ACCOUNT_MODULE}/deleteUser/${userId}`, {
        ...config,
        headers: {
            'Content-Type': 'application/json',
            ...config?.headers
        }
    })
        .then(res => {
            return res;
        })
        .catch(err => {
            console.error("Failed to delete user:", err);
            throw err;
        });
};

//设置管理员身份
export const setAdminUser = (userId: number, config?: AxiosRequestConfig) => {
    if (!userId) {
        console.error("UserId is required.");
        return Promise.reject(new Error("UserId cannot be empty"));
    }
    return axios.delete(`${ACCOUNT_MODULE}/setAdmin/${userId}`, {
        ...config,
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then(res => {
            return res;
        })
        .catch(err => {
            console.error("Failed to set administrator:", err);
            throw err;
        });
}




