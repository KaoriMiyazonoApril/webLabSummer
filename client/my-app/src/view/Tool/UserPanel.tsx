//是最上方的一长条用户操作界面
import {useEffect, useRef, useState} from "react";
import { useNavigate } from 'react-router-dom';
import "./UserPanel.css"


const UserPanel = () => {
    const navigate = useNavigate();
    const [activeTab, setActiveTab] = useState(null);
    const [menuPosition, setMenuPosition] = useState({ top: 0, left: 0 });
    const [imageUrl, setImageUrl] = useState(sessionStorage.getItem("avatar"));
    const [username, setUserName] = useState(sessionStorage.getItem("username"));

    const labelRef = useRef<HTMLDivElement>(null); // 对应头像标签

    useEffect(() => {
        const avatar = sessionStorage.getItem("avatar");
        if (avatar) {setImageUrl(avatar);console.log(sessionStorage.getItem('avatar'))}

        const name = sessionStorage.getItem("username");
        if (name) setUserName(name);
    }, []);

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (labelRef.current && !labelRef.current.contains(event.target as Node)) {
                setActiveTab(null); // 点击外部区域时关闭菜单
            }
        };

        document.addEventListener("click", handleClickOutside);

        return () => {
            document.removeEventListener("click", handleClickOutside);
        };
    }, []);

    const handleTabClick = (index: any) => {
        setActiveTab(index === activeTab ? null : index);  // 点击相同标签时隐藏

        // 获取不同标签的位置
        if (labelRef.current) {
            const rect = labelRef.current.getBoundingClientRect();
            setMenuPosition({
                top: rect.bottom,
                left: rect.left,
            });
        }
    };

    return (
        <div className="user-panel">
            <div className="label-container">
                <label
                    className="label"
                    onClick={() => { navigate('/home'); }}
                >
                    体育活动管理平台
                </label>

                <label
                    className={`label`}
                    onClick={() => {navigate('/activities')}}
                >
                    活动列表
                </label>

                {sessionStorage.getItem("role") === 'Admin' && (
                    <label
                        className="label"
                        onClick={() => { navigate('/users'); }}
                    >
                        用户列表
                    </label>
                )}

                {sessionStorage.getItem("role") === 'Admin' && (
                    <label
                        className="label"
                        onClick={() => { navigate('/createActivity'); }}
                    >
                        创建活动
                    </label>
                )}


            </div>

            <div
                ref={labelRef} // 使用不同的 ref
                className="circle-container"
                onClick={() => handleTabClick(2)}
            >
                <img src={imageUrl ?imageUrl:'/avatar.png'}  className="circle-avatar"  onError={(e) => (e.target as HTMLImageElement).src = '/avatar.png'}/>
            </div>
            {activeTab !== null && (
                <>
                    {activeTab === 2 && (
                        <div className="sub-options" style={{ top: menuPosition.top, left: menuPosition.left }}>
                            <label className="username">{username}</label>
                            <label className="sub-option" onClick={() => { navigate('/userProfile'); }}>修改用户信息</label>
                            <label className="sub-option" onClick={() => { sessionStorage.clear(); navigate('/login'); }}>退出登录</label>
                        </div>
                    )}
                </>
            )}
        </div>
    );
};

export default UserPanel;

