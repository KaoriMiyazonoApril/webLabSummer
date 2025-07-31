import {useState, useMemo} from 'react';
import {message} from 'antd';  // 使用 Ant Design 的 message 显示提示
import {useNavigate} from 'react-router-dom';  // 假设使用 react-router-dom 来导航

// 输入框值（需要在前端拦截不合法输入：是否为空+额外规则）
const username = ref('')
const password = ref('')
const hasUsernameInput = computed(() => username.value != '')
// 简单的用户名合法性检查：非空即可
const usernameLegal = computed(() => username.value.trim().length > 0);
// 密码是否为空
const hasPasswordInput = computed(() => password.value != '')
// 登录按钮可用性
const loginDisabled = computed(() => {
    return !(hasUsernameInput.value && hasPasswordInput.value)
})

// 登录按钮触发
function handleLogin() {
    userLogin({
        username: username.value,
        password: password.value
    }).then(res => {
        if (res.data.code === '200') {
            ElMessage({
                message: "登录成功！",
                type: 'success',
                center: true,
            })
            const token = res.data.data
            sessionStorage.setItem('token', token)
            sessionStorage.setItem('username', username.value)


            // 获取用户信息
            userInfo(username.value).then(res => {
                sessionStorage.setItem('name', res.data.data.name);
                sessionStorage.setItem('role', res.data.data.role);
                sessionStorage.setItem('id', res.data.data.id);
                // router.push({path: "/dashboard/" + username.value})
                router.push({ path: '/allproduct' })
            })
        } else if (res.data.code === '400') {
            ElMessage({
                message: res.data.msg,
                type: 'error',
                center: true,
            })
            password.value = ''
        }
    })
}

export default function Login=()=> {
    return (

    );
}