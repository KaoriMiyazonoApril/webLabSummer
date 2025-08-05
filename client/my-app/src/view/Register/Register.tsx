import {useEffect, useState} from "react";
import { useNavigate } from 'react-router-dom';
import { ToastContainer, toast } from 'react-toastify';
import {userInfo, userLogin, userRegister} from "../../api/account"
import "./Register.css"
import "../Login/Login.css"
import Login from "../Login/Login";

const Register = () => {
    const navigate = useNavigate();
    const[username, setUsername] = useState("");
    const[telephone, setTelephone] = useState("");
    const[password, setPassword] = useState("");

    const hasUsernameInput = username.trim() !== ''
    const hasPasswordInput = password.trim() !== '';
    const hasTelephoneInput = telephone.trim() !== '';
    const telephoneLegal = /^[0-9]{11}$/.test(telephone);

    const [registerDisabled, setRegisterDisabled] = useState(true);
    useEffect(() => {
        setRegisterDisabled(!(hasUsernameInput && hasPasswordInput && hasTelephoneInput && telephoneLegal))
    },[hasUsernameInput, hasPasswordInput, telephoneLegal,hasTelephoneInput, telephoneLegal])

    const handleRegister = () => {
        userRegister({
            id:0,
            username:username,
            password:password,
            telephone:Number(telephone),
            role:'',
            avatar:''
        }).then(res => {
            if (res.data.code === '200') {
                toast.success('登录成功,即将跳转回登陆界面！', {
                    position: "top-center",  // 消息位置
                    autoClose: 3000,         // 自动关闭的时间
                    hideProgressBar: false,  // 显示进度条
                    closeOnClick: true,      // 点击关闭按钮时关闭
                    pauseOnHover: true,      // 悬停时暂停自动关闭
                })
                navigate('/login')


            } else if (res.data.code !== '200') {
                toast.error(`注册失败:${res.data.message}`, {
                    position: "top-center",
                    autoClose: 3000,
                    hideProgressBar: false,
                    closeOnClick: true,
                    pauseOnHover: true,
                });
                setUsername('')
                setPassword('');
                setTelephone('')
            }
        });
    };

    return (
        <div className="main-frame">
            <div className="register-card">
                <h2>注册</h2>
                <form>
                    <div className="form-item1">
                        <div>
                            <label htmlFor="telephone" style={{ color: hasTelephoneInput && !telephoneLegal ? 'red' : 'black' }}>
                                {!hasTelephoneInput ? '请输入您的电话号码' : !telephoneLegal ? '电话号需要为11位' : '确认您的电话号码'}
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
                            <label htmlFor="password">{hasPasswordInput? '请确认您的密码':'请输入您的密码'}</label>
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
                    <div className="form-item3">
                        <div>
                            <label htmlFor="username">
                                {!hasTelephoneInput ? '请输入您的用户名' : '请确认您的用户名'}
                            </label>
                        </div>
                        <div>
                            <input
                                id="username"
                                type="text"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                                placeholder="请输入您的用户名"
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
                    <div>
                        <button
                            type="button"
                            onClick={handleRegister}
                            disabled={registerDisabled}
                            className="RegisterButton"
                        >注册</button>

                        <button  className={"Button"} onClick={()=>{navigate('/login')}}>返回登录</button>
                    </div>


                </form>
            </div>
        </div>
    );
}

export default Register;