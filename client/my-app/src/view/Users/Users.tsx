import React, {useState, useEffect, use} from 'react';
import {allUserInfo, deleteUser, setAdminUser} from "../../api/account";
import {toast, ToastOptions} from 'react-toastify';
import UserPanel from "../Tool/UserPanel";

const Users = () => {
    const [users, setUsers] = useState<User[]>([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [loading, setLoading] = useState(false);
    const usersPerPage = 10;

    const toastConfig: ToastOptions = {
        position: 'top-center',
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true
    }

    // 计算分页数据
    const indexOfLastUser = currentPage * usersPerPage;
    const indexOfFirstUser = indexOfLastUser - usersPerPage;
    const currentUsers = users.slice(indexOfFirstUser, indexOfLastUser);
    const totalPages = Math.ceil(users.length / usersPerPage);

    const getUsersInformation = () => {
        setLoading(true);
        allUserInfo().then(res => {
            if (res.data.code === '200') {
                setUsers(res.data.data || []);
            } else {
                toast.error(`获取所有用户信息:${res.data.message}`, toastConfig);
            }
        }).catch(error => {
            toast.error('获取用户信息失败',toastConfig);
        }).finally(() => {
            setLoading(false);
        });
    };

    // 删除账户
    const handleDeleteUser = (userId:any) => {
        if (window.confirm(`确定要删除用户 "${userId}" 吗？`)) {
            deleteUser(userId).then(res => {
                if (res.data.code === '200') {
                   toast.success(`删除用户${userId}成功`, toastConfig);
                   getUsersInformation();
               } else {
                    toast.error(`删除用户失败: ${res.data.message}`,toastConfig);
                }
            });
        }
    };

    // 设为管理员
    const handleSetAdmin = (userId:any) => {
        if (window.confirm(`确定要将用户 "${userId}" 设为管理员吗？`)) {
            setAdminUser(userId).then(res => {
                if (res.data.code === '200') {
                    toast.success(`设置管理员${userId}成功`, toastConfig)
                    getUsersInformation(); // 重新获取用户列表
                } else {
                    toast.error(`设置管理员失败: ${res.data.message}`, toastConfig)
                }
            })
        }
    };

    // 分页控制
    const handlePageChange = (pageNumber:any) => {
        setCurrentPage(pageNumber);
    };

    // 组件挂载时获取用户信息
    useEffect(() => {
        getUsersInformation();
    }, []);

    return (
        <div>
            <UserPanel />

            <div className="users-container" style={{ padding: '20px' }}>
                <div className="users-header" style={{ marginBottom: '20px', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <h2>用户管理</h2>
                    <button
                        onClick={getUsersInformation}
                        disabled={loading}
                        style={{
                            padding: '8px 16px',
                            backgroundColor: '#007bff',
                            color: 'white',
                            border: 'none',
                            borderRadius: '4px',
                            cursor: loading ? 'not-allowed' : 'pointer'
                        }}
                    >
                        {loading ? '刷新中...' : '刷新'}
                    </button>
                </div>

                {loading ? (
                    <div style={{ textAlign: 'center', padding: '40px' }}>
                        <p>加载中...</p>
                    </div>
                ) : (
                    <>
                        {/* 用户表格 */}
                        <div className="table-container" style={{ overflowX: 'auto' }}>
                            <table style={{
                                width: '100%',
                                borderCollapse: 'collapse',
                                backgroundColor: 'white',
                                boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
                                borderRadius: '8px',
                                overflow: 'hidden'
                            }}>
                                <thead>
                                <tr style={{ backgroundColor: '#f8f9fa' }}>
                                    <th style={tableHeaderStyle}>ID</th>
                                    <th style={tableHeaderStyle}>电话号码</th>
                                    <th style={tableHeaderStyle}>用户名</th>
                                    <th style={tableHeaderStyle}>身份</th>
                                    <th style={tableHeaderStyle}>操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                {currentUsers.length > 0 ? (
                                    currentUsers.map((user, index) => (
                                        <tr key={user.id} style={{
                                            borderBottom: '1px solid #dee2e6',
                                            backgroundColor: index % 2 === 0 ? '#ffffff' : '#f8f9fa'
                                        }}>
                                            <td style={tableCellStyle}>{user.id}</td>
                                            <td style={tableCellStyle}>{user.telephone || '-'}</td>
                                            <td style={tableCellStyle}>{user.username}</td>
                                            <td style={tableCellStyle}>
                                                    <span style={{
                                                        padding: '4px 8px',
                                                        borderRadius: '12px',
                                                        fontSize: '12px',
                                                        fontWeight: 'bold',
                                                        color: user.role === 'Admin' ? '#FF0000' : '#000000'
                                                    }}>
                                                        {user.role === 'Admin' ? '管理员' : '用户'}
                                                    </span>
                                            </td>
                                            <td style={tableCellStyle}>
                                                {user.role === 'User' ? (
                                                    <div style={{ display: 'flex', gap: '8px' }}>
                                                        <button
                                                            onClick={() => handleDeleteUser(user.id)}
                                                            style={{
                                                                padding: '4px 8px',
                                                                backgroundColor: '#dc3545',
                                                                color: 'white',
                                                                border: 'none',
                                                                borderRadius: '4px',
                                                                cursor: 'pointer',
                                                                fontSize: '12px'
                                                            }}
                                                            onMouseOver={(e) => {
                                                                const target = e.target as HTMLButtonElement;  // 类型断言
                                                                target.style.backgroundColor = '#c82333';  // 鼠标悬停时背景颜色变为深红色
                                                            }}
                                                            onMouseOut={(e) => {
                                                                const target = e.target as HTMLButtonElement;  // 类型断言
                                                                target.style.backgroundColor = '#dc3545';  // 鼠标移出时背景颜色恢复为原色
                                                            }}
                                                        >
                                                            删除账户
                                                        </button>
                                                        <button
                                                            onClick={() => handleSetAdmin(user.id)}
                                                            style={{
                                                                padding: '4px 8px',
                                                                backgroundColor: '#28a745',
                                                                color: 'white',
                                                                border: 'none',
                                                                borderRadius: '4px',
                                                                cursor: 'pointer',
                                                                fontSize: '12px'
                                                            }}
                                                            onMouseOver={(e) => {
                                                                const target = e.target as HTMLButtonElement;  // 类型断言
                                                                target.style.backgroundColor = '#218838';
                                                            }}
                                                            onMouseOut={(e) => {
                                                                const target = e.target as HTMLButtonElement;  // 类型断言
                                                                target.style.backgroundColor = '#28a745';
                                                            }}
                                                        >
                                                            设为管理员
                                                        </button>
                                                    </div>
                                                ) : (
                                                    <span style={{ color: '#6c757d', fontSize: '12px' }}>无操作</span>
                                                )}
                                            </td>
                                        </tr>
                                    ))
                                ) : (
                                    <tr>
                                        <td colSpan={5} style={{
                                            ...tableCellStyle,
                                            textAlign: 'center',
                                            color: '#6c757d',
                                            padding: '40px'
                                        }}>
                                            暂无用户数据
                                        </td>
                                    </tr>
                                )}
                                </tbody>
                            </table>
                        </div>

                        {/* 分页控件 */}
                        {totalPages > 1 && (
                            <div className="pagination" style={{
                                display: 'flex',
                                justifyContent: 'center',
                                alignItems: 'center',
                                marginTop: '20px',
                                gap: '8px'
                            }}>
                                <button
                                    onClick={() => handlePageChange(currentPage - 1)}
                                    disabled={currentPage === 1}
                                    style={{
                                        padding: '8px 12px',
                                        border: '1px solid #dee2e6',
                                        backgroundColor: currentPage === 1 ? '#f8f9fa' : 'white',
                                        color: currentPage === 1 ? '#6c757d' : '#007bff',
                                        borderRadius: '4px',
                                        cursor: currentPage === 1 ? 'not-allowed' : 'pointer'
                                    }}
                                >
                                    上一页
                                </button>

                                {Array.from({ length: totalPages }, (_, i) => i + 1).map(pageNumber => (
                                    <button
                                        key={pageNumber}
                                        onClick={() => handlePageChange(pageNumber)}
                                        style={{
                                            padding: '8px 12px',
                                            border: '1px solid #dee2e6',
                                            backgroundColor: currentPage === pageNumber ? '#007bff' : 'white',
                                            color: currentPage === pageNumber ? 'white' : '#007bff',
                                            borderRadius: '4px',
                                            cursor: 'pointer'
                                        }}
                                    >
                                        {pageNumber}
                                    </button>
                                ))}

                                <button
                                    onClick={() => handlePageChange(currentPage + 1)}
                                    disabled={currentPage === totalPages}
                                    style={{
                                        padding: '8px 12px',
                                        border: '1px solid #dee2e6',
                                        backgroundColor: currentPage === totalPages ? '#f8f9fa' : 'white',
                                        color: currentPage === totalPages ? '#6c757d' : '#007bff',
                                        borderRadius: '4px',
                                        cursor: currentPage === totalPages ? 'not-allowed' : 'pointer'
                                    }}
                                >
                                    下一页
                                </button>
                            </div>
                        )}

                        {/* 分页信息 */}
                        <div style={{
                            textAlign: 'center',
                            marginTop: '10px',
                            color: '#6c757d',
                            fontSize: '14px'
                        }}>
                            共 {users.length} 条记录，当前第 {currentPage} / {totalPages} 页
                        </div>
                    </>
                )}
            </div>
        </div>
    );
};

// 样式常量
const tableHeaderStyle: React.CSSProperties = {
    padding: '12px',
    textAlign: 'left',
    fontWeight: 'bold',
    color: '#495057',
    borderBottom: '2px solid #dee2e6'
};

const tableCellStyle: React.CSSProperties= {
    padding: '12px',
    textAlign: 'left',
    color: '#495057'
};

interface User {
    id: number;
    username: string;
    telephone: string | null;
    role: 'User' | 'Admin';
    avatar?: string;
    token?: string;
}

export default Users;

