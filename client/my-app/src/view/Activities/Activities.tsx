import React, { useState, useEffect } from 'react';
import { Card, Row, Col, Typography, Spin, Empty } from 'antd';
import UserPanel from "../Tool/UserPanel";

const { Text, Title } = Typography;
import {getActivityNotAvailable,getActivityAll,getActivityAvailable,getActivityFull} from "../../api/activity"
import {getYourActivity} from "../../api/attendance"
import {toast, ToastOptions} from "react-toastify";
import {useNavigate} from "react-router-dom";

function ActivityListPage() {
    const toastConfig: ToastOptions = {
        position: 'top-center',
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true
    }

    const navigate=useNavigate()

    const [currentTab, setCurrentTab] = useState('all');
    const [activities, setActivities] = useState([]);
    const [loading, setLoading] = useState(false);

    const tabs = [
        { key: 'all', label: '所有活动', api: 'getActivityAll' },
        { key: 'available', label: '可选活动', api: 'getActivityAvailable' },
        { key: 'full', label: '已满活动', api: 'getActivityFull' },
        { key: 'expired', label: '过期活动', api: 'getActivityNotAvailable' },
        { key: 'mine', label: '我的活动', api: '' },
    ]

    const apiMap = {
        'getActivityAll': getActivityAll,
        'getActivityAvailable': getActivityAvailable,
        'getActivityFull': getActivityFull,
        'getActivityNotAvailable': getActivityNotAvailable,
    };

    // 模拟API调用
    const fetchActivities = async (type:string) => {
        setLoading(true);
        try {
            let response
            if(type==='mine'){
                response=await getYourActivity(Number(sessionStorage.getItem('userId')))
            }
            else{
                const currentTab = tabs.find(tab => tab.key === type);
                const apiFunction = apiMap[currentTab!.api as keyof typeof apiMap];
                response = await apiFunction();
            }

            if(response.data.code==='200'){
                const data = response.data.data;
                setActivities(data);
            }
            else{
                toast.error(`获取活动信息失败:${response.data.message}`)
            }
            // 存储到 sessionStorage
            sessionStorage.setItem('currentActivityTab', type);
        } catch (error) {
            console.error('获取活动数据失败:', error);
            setActivities([]);
        } finally {
            setLoading(false);
        }
    };

    // 切换标签
    const handleTabChange = (tabKey:any) => {
        setCurrentTab(tabKey);
        fetchActivities(tabKey);
    };

    function formatDateTime(dateTimeString:any) {
        // 移除"截止日期: "前缀（如果存在）
        const cleanString = dateTimeString.replace('截止日期: ', '');

        // 解析ISO格式的日期时间字符串
        const date = new Date(cleanString);

        // 检查日期是否有效
        if (isNaN(date.getTime())) {
            return '无效日期';
        }

        // 格式化为中文日期时间
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        const seconds = String(date.getSeconds()).padStart(2, '0');

        return `${year}年${month}月${day}日 ${hours}:${minutes}:${seconds}`;
    }

    // 组件挂载时从 sessionStorage 恢复状态
    useEffect(() => {
        fetchActivities('all');
    }, []);

    // 渲染活动卡片
    const renderActivityCard = (activity:any) => (
        <Col key={activity.id} xs={24} sm={12} md={8} lg={6} style={{ marginBottom: '16px' }}>
            <Card
                hoverable
                style={{
                    borderRadius: '12px',
                    overflow: 'hidden',
                    height: '100%'
                }}
                cover={
                    <div style={{ height: '160px', overflow: 'hidden' }}>
                        <img
                            alt={activity.name}
                            src={activity.image}
                            style={{
                                width: '100%',
                                height: '100%',
                                objectFit: 'cover'
                            }}
                        />
                    </div>
                }
                bodyStyle={{ padding: '12px' }}

                onClick={() => {sessionStorage.setItem('activityId', activity.id);navigate('/activity')}}
            >
                <div style={{ marginBottom: '8px' }}>
                    <Title level={5} style={{ margin: 0, fontSize: '14px', lineHeight: '1.4' }}>
                        {activity.name}
                    </Title>
                </div>

                <div style={{ color: '#666', fontSize: '12px', marginBottom: '8px' }}>
                    <div style={{ marginBottom: '4px' }}>
                        <Text style={{ color: activity.limitCount > 0 ? '#52c41a' : '#ff4d4f' }}>
                            {activity.limitCount > 0 ? `剩余 ${activity.limitCount} 个名额` : '已满员'}
                        </Text>
                    </div>

                    <div style={{ marginBottom: '4px' }}>
                        <Text strong style={{ color: '#1890ff' }}>
                            {activity.cost === 0 ? '免费' : `${activity.cost} 元`}
                        </Text>
                    </div>

                    <div>
                        <Text type="secondary">
                            截止日期: {formatDateTime(activity.date)}
                        </Text>
                    </div>
                </div>
            </Card>
        </Col>
    );

    return (
        <div>
            <UserPanel></UserPanel>

            <div style={{ backgroundColor: '#f0f2f5' ,width:'100%'}}>
                {/* 顶部导航栏 */}
                <div style={{
                    padding: '0',
                    marginBottom: '20px'
                }}>
                    <div style={{
                        display: 'flex',
                        justifyContent: 'center',
                        maxWidth: '1200px',
                        margin: '0 auto'
                    }}>
                        {tabs.map((tab) => (
                            <div
                                key={tab.key}
                                onClick={() => handleTabChange(tab.key)}
                                style={{
                                    padding: '16px 24px',
                                    color: currentTab === tab.key ? 'black' : 'rgba(0,0,0,0.8)',
                                    backgroundColor: currentTab === tab.key ? 'rgba(0,0,0,0.2)' : 'transparent',
                                    cursor: 'pointer',
                                    fontSize: '16px',
                                    fontWeight: currentTab === tab.key ? 'bold' : 'normal',
                                    borderBottom: currentTab === tab.key ? '3px solid #fff' : '3px solid transparent',
                                    transition: 'all 0.3s ease',
                                    display: 'flex',
                                    alignItems: 'center',
                                    gap: '8px'
                                }}
                            >
              <span style={{ fontSize: '18px' }}>
                {tab.key === 'all' && '📋'}
                  {tab.key === 'available' && '✅'}
                  {tab.key === 'full' && '🚫'}
                  {tab.key === 'expired' && '⏰'}
                  {tab.key === 'mine' && '👤'}
              </span>
                                {tab.label}
                            </div>
                        ))}
                    </div>
                </div>

                {/* 内容区域 */}
                <div style={{
                    maxWidth: '1200px',
                    margin: '0 auto',
                    padding: '0 20px'
                }}>
                    {loading ? (
                        <div style={{ textAlign: 'center', padding: '50px' }}>
                            <Spin size="large" />
                            <div style={{ marginTop: '16px', color: '#666' }}>
                                加载活动数据中...
                            </div>
                        </div>
                    ) : activities.length === 0 ? (
                        <div style={{ textAlign: 'center', padding: '50px' }}>
                            <Empty
                                description="暂无活动数据"
                                imageStyle={{ height: 120 }}
                            />
                        </div>
                    ) : (
                        <Row gutter={[16, 0]} justify="start">
                            {activities.map(renderActivityCard)}
                        </Row>
                    )}
                </div>
            </div>
        </div>

    );
}

export default ActivityListPage;