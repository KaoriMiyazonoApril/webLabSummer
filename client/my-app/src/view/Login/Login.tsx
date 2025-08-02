import {useEffect, useState} from "react";
import { useNavigate } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import {userInfo, userLogin, userRegister} from "../../api/account"
import "./Login.css"


const Login = () => {
    // 使用 useState 管理表单数据
    const [telephone, setTelephone] = useState('');
    const [password, setPassword] = useState('');

    const [loginDisabled, setLoginDisabled] = useState(true);
    const navigate = useNavigate();

    // 计算是否有输入用户名和密码
    const hasTelephoneInput = telephone.trim() !== '';
    const TelephoneLegal = /^[0-9]{11}$/.test(telephone);
    const hasPasswordInput = password.trim() !== '';

    // 登录按钮的禁用状态
    useEffect(() => {
        setLoginDisabled(!(hasTelephoneInput && hasPasswordInput&&TelephoneLegal));
    }, [hasTelephoneInput, hasPasswordInput,TelephoneLegal]);

    const handleLogin = () => {
        userLogin({
            telephone: Number(telephone),
            password: password
        }).then(res => {
            if (res.data.code === '200') {
                toast.success('登录成功,即将自动跳转！', {
                    position: "top-center",  // 消息位置
                    autoClose: 3000,         // 自动关闭的时间
                    hideProgressBar: false,  // 显示进度条
                    closeOnClick: true,      // 点击关闭按钮时关闭
                    pauseOnHover: true,      // 悬停时暂停自动关闭
                })

                sessionStorage.setItem('token', res.data.data.token);
                sessionStorage.setItem('userId', res.data.data.userId);

                // 获取用户信息
                userInfo(Number(sessionStorage.getItem('userId'))).then(res => {
                    sessionStorage.setItem('telephone', res.data.data.telephone);
                    sessionStorage.setItem('role', res.data.data.role);
                    sessionStorage.setItem('avatar',res.data.data.avatar)
                    // 跳转到所有产品页
                    navigate('/activities')
                });
            } else if (res.data.code !== '200') {
                toast.error(`登录失败:${res.data.message}`, {
                    position: "top-center",
                    autoClose: 3000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                });
                setPassword('');
            }
        });
    };

    return (
        <div className="main-frame bgImage">
            <div className="login-card">
                <h2>登录</h2>
                <form>
                    <div className="form-item1">
                        <div>
                            <label htmlFor="username">
                                {!hasTelephoneInput ? '请输入您的电话号码' : !TelephoneLegal ? '电话号需要为11位' : '电话号码'}
                            </label>
                        </div>
                        <div>
                        <input
                            id="telephone"
                            type="text"
                            value={telephone}
                            onChange={(e) => setTelephone(e.target.value)}
                            required
                            placeholder="请输入您的电话号码"
                            style={{
                                width: '200px',  // 设置宽度
                                height: '20px',  // 设置高度
                                padding: '4px', // 内边距，让文本更舒适
                                fontSize: '16px', // 设置字体大小
                                borderRadius: '5px', // 设置圆角边框
                                border: '1px solid #ccc', // 设置边框样式
                            }}
                        />
                        </div>
                    </div>

                    <div className="form-item2">
                        <div>
                            <label htmlFor="password">请输入您的密码</label>
                        </div>
                        <div>
                        <input
                            id="password"
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            placeholder="••••••••"
                            style={{
                                width: '200px',  // 设置宽度
                                height: '20px',  // 设置高度
                                padding: '4px', // 内边距，让文本更舒适
                                fontSize: '16px', // 设置字体大小
                                borderRadius: '5px', // 设置圆角边框
                                border: '1px solid #ccc', // 设置边框样式
                            }}
                        />
                        </div>
                    </div>

                    <button
                        type="button"
                        onClick={handleLogin}
                        disabled={loginDisabled}
                        className="LoginButton"
                    >登录</button>

                    <div>
                        没有账号?{' '}
                        <span
                            style={{ color: 'blue', cursor: 'pointer' }}
                            onClick={() => {navigate('/register')
                            }}
                        >点击注册
                        </span>
                    </div>


                </form>
            </div>
        </div>
    );
};

export default Login;

