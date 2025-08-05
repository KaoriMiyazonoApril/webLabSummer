import React, {useState, useEffect, useRef} from 'react';
import {toast, ToastOptions} from 'react-toastify';
import { Uploader } from 'rsuite';
import {FileType} from "rsuite/Uploader";
import {Upload, Button, Image, Space, Typography, Modal, UploadProps, UploadFile} from 'antd';
import { InboxOutlined, DeleteOutlined } from '@ant-design/icons';
import axios from "axios";
import {uploadImage} from "../../api/image";
import {userInfoUpdate, userRegister} from "../../api/account";
import UserPanel from "../Tool/UserPanel";


const CreateActivity = () => {
    // 默认头像路径
    const DEFAULT_AVATAR = '/avatar.png'

    // 用户信息状态
    const [userInfo, setUserInfo] = useState({
        id: '',
        username: '',
        telephone: '',
        avatar: ''
    });

    const [username, setUserName] = useState('');
    const [password, setPassword] = useState('');
    const [rePassword, setRePassword] = useState('');
    const [updateDisabled, setUpdateDisabled] = useState(true);

    // 修复 fileList 类型
    const [fileList, setFileList] = useState<UploadFile[]>([]);

    // 添加缺失的预览状态
    const [previewOpen, setPreviewOpen] = useState(false);
    const [previewImage, setPreviewImage] = useState('');

    const uploader = React.useRef(null);

    const hasUserNameInput=username.trim()!==''
    const hasPassWordInput=password.trim()!=''
    const PassWordLegal=password.trim()===rePassword.trim()
    const hasPic=fileList.length>0

    // 修复 useEffect 逻辑：当密码不匹配时应该禁用按钮
    useEffect(() => {
        setUpdateDisabled(!(PassWordLegal&&(hasUserNameInput||hasPassWordInput||hasPic)))
    }, [hasUserNameInput,hasPassWordInput,PassWordLegal,hasPic]);

    const toastConfig: ToastOptions = {
        position: 'top-center',
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true
    }

    // 修复 customRequest 类型
    const customRequest = async (options: any) => {
        const { onSuccess, onError } = options;
        onSuccess("")
    }

    // 修复文件变化处理类型
    const handleChange: UploadProps['onChange'] = ({ fileList: newFileList }) => {
        // 只保留最新的一个文件
        const latestFileList = newFileList.slice(-1);
        setFileList(latestFileList);
    };

    // 修复预览处理类型
    const handlePreview = async (file: UploadFile) => {
        if (!file.url && !file.preview) {
            file.preview = await getBase64(file.originFileObj as File);
        }
        setPreviewImage(file.url || file.preview || '');
        setPreviewOpen(true);
    };

    // 修复 getBase64 函数类型
    const getBase64 = (file: File): Promise<string> =>
        new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onload = () => resolve(reader.result as string);
            reader.onerror = (error) => reject(error);
        });



    // 添加缺失的 getUserInfoFromStorage 函数
    const getUserInfoFromStorage = () => {
        try {
            const storedUserInfo = sessionStorage.getItem('userInfo');
            if (storedUserInfo) {
                const parsedUserInfo = JSON.parse(storedUserInfo);
                setUserInfo(parsedUserInfo);
            }
        } catch (error) {
            console.error('获取用户信息失败:', error);
        }
    };


    // 完善提交修改函数
    const handleSubmit = async () => {
        try {
            const formData = new FormData()
            const updateInfo={id:Number(sessionStorage.getItem('userId')),role: '', username: '', avatar: '', telephone: 0, password:''}
            // 添加头像文件（如果有选择）
            if (fileList.length === 1) {
                // 将文件添加到 formData 中
                const file = fileList[0].originFileObj
                if(file){
                    formData.append('file', file);
                    const res=await uploadImage(formData)
                    if(res.data.code==='200'){
                        updateInfo.avatar=res.data.data
                        toast.success('上传图片成功',toastConfig)
                    }
                    else{
                        toast.error(`上传图片失败:${res.data.message}`,toastConfig)
                    }
                }
            }
            if(username && username.trim() !== ''){
                updateInfo.username=username
            }
            if(password && password.trim()!==''){
                updateInfo.password=password
            }

            userInfoUpdate(updateInfo).then(res=>{
                if(res.data.code === '200'){
                    toast.success('修改成功',toastConfig)
                    sessionStorage.setItem('token',res.data.data.token)
                    sessionStorage.setItem('avatar',res.data.data.avatar)
                    sessionStorage.setItem('username',res.data.data.username)
                }
                else{
                    toast.error(`上传信息失败:${res.data.message}`,toastConfig)
                }
            })
            // 清空表单
            setUserName('');
            setPassword('');
            setRePassword('');
            setFileList([]);

        } catch (error) {
            toast.error('更新失败，请稍后重试',toastConfig);
        }
    };

    // 处理头像加载错误
    const handleAvatarError = (e: React.SyntheticEvent<HTMLImageElement, Event>) => {
        console.log('头像加载失败，使用默认头像');
        const target = e.target as HTMLImageElement;
        target.src = DEFAULT_AVATAR;
    };

    // 组件挂载时从sessionStorage获取用户信息
    useEffect(() => {
        getUserInfoFromStorage();
    }, []);

    return (
        <div>
            <UserPanel></UserPanel>
            <div style={{ maxWidth: '800px', margin: '0 auto', padding: '20px' }}>
                {/* 修改信息区域 */}
                <div style={{
                    backgroundColor: 'white',
                    borderRadius: '12px',
                    padding: '30px',
                    boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
                }}>
                    <h2 style={{
                        textAlign: 'center',
                        marginBottom: '30px',
                        color: '#333',
                        fontSize: '24px',
                        fontWeight: 'bold'
                    }}>
                        修改个人信息
                    </h2>

                    <div>
                        <div style={{
                            display: 'grid',
                            gap: '20px',
                            maxWidth: '500px',
                            margin: '0 auto'
                        }}>
                            {/* 用户名输入 */}
                            <div>
                                <label style={labelStyle}>新用户名</label>
                                <input
                                    type="text"
                                    name="username"
                                    value={username}
                                    onChange={(e) => setUserName(e.target.value)}
                                    style={{
                                        width: '200px',
                                        height: '20px',
                                        padding: '4px',
                                        fontSize: '16px',
                                        borderRadius: '5px',
                                        border: '1px solid #ccc',
                                    }}
                                    placeholder="请输入新的用户名"
                                />
                            </div>

                            {/* 密码输入 */}
                            <div>
                                <label style={labelStyle}>新密码</label>
                                <input
                                    type="password"
                                    name="password"
                                    value={password}
                                    onChange={(e) => setPassword(e.target.value)}
                                    style={{
                                        width: '200px',
                                        height: '20px',
                                        padding: '4px',
                                        fontSize: '16px',
                                        borderRadius: '5px',
                                        border: '1px solid #ccc',
                                    }}
                                    placeholder="留空则不修改密码"
                                />
                            </div>

                            {/* 确认密码 */}
                            {password && (
                                <div>
                                    <label style={labelStyle}>确认新的密码</label>
                                    <input
                                        type="password"
                                        name="rePassword"
                                        value={rePassword}
                                        onChange={(e) => setRePassword(e.target.value)}
                                        style={{
                                            width: '200px',
                                            height: '20px',
                                            padding: '4px',
                                            fontSize: '16px',
                                            borderRadius: '5px',
                                            border: password !== rePassword ? '1px solid #ff4d4f' : '1px solid #ccc',
                                        }}
                                        placeholder="请再次输入密码"
                                    />
                                    {password !== rePassword && rePassword && (
                                        <div style={{ color: '#ff4d4f', fontSize: '12px', marginTop: '4px' }}>
                                            密码不一致
                                        </div>
                                    )}
                                </div>
                            )}

                            {/* 头像上传 */}
                            <div>
                                <label style={labelStyle}>更换头像</label>
                                <Upload
                                    fileList={fileList}
                                    customRequest={customRequest}
                                    onChange={handleChange}
                                    onPreview={handlePreview}
                                    accept="image/*"
                                    showUploadList={false}
                                    maxCount={1}
                                >
                                    <Button icon={<InboxOutlined />}>
                                        点击选择图片
                                    </Button>
                                </Upload>

                                {/* 显示选中的文件 */}
                                {fileList.length > 0 && (
                                    <div style={{ marginTop: '8px', fontSize: '14px', color: '#666' }}>
                                        已选择: {fileList[0].name}
                                    </div>
                                )}
                            </div>

                            {/* 提交按钮 */}
                            <button
                                disabled={updateDisabled}
                                style={{
                                    ...buttonStyle,
                                    backgroundColor: updateDisabled ? '#d9d9d9' : '#007bff',
                                    cursor: updateDisabled ? 'not-allowed' : 'pointer',
                                }}
                                onClick={handleSubmit}
                            >
                                提交修改
                            </button>
                        </div>
                    </div>
                </div>

                {/* 预览模态框 */}
                <Modal
                    open={previewOpen}
                    title="图片预览"
                    footer={null}
                    onCancel={() => setPreviewOpen(false)}
                >
                    <img
                        alt="preview"
                        style={{ width: '100%' }}
                        src={previewImage}
                    />
                </Modal>
            </div>
        </div>
    );
};

// 样式常量
const infoItemStyle = {
    textAlign: 'center' as 'center',
    padding: '15px',
    backgroundColor: '#f8f9fa',
}

const labelStyle = {
    display: 'block',
    fontSize: '14px',
    fontWeight: 'bold',
    color: '#495057',
    marginBottom: '5px'
};

const valueStyle = {
    fontSize: '16px',
    color: '#333',
    fontWeight: '500'
};

const inputStyle = {
    width: '100%',
    padding: '12px',
    border: '1px solid #ced4da',
    borderRadius: '6px',
    fontSize: '16px',
    transition: 'border-color 0.15s ease-in-out',
    outline: 'none',
    boxSizing: 'border-box'
};

const buttonStyle = {
    width: '100%',
    padding: '12px',
    border: 'none',
    borderRadius: '6px',
    fontSize: '16px',
    fontWeight: 'bold',
    color: 'white',
    transition: 'background-color 0.15s ease-in-out',
    marginTop: '10px'
};

export default CreateActivity;