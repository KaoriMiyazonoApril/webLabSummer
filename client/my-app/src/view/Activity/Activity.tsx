import React, { useState, useEffect } from 'react';
import { Card, Row, Col, Typography, Button, Avatar, Rate, Pagination, Modal, Space } from 'antd';
import { UserOutlined, DeleteOutlined, ExclamationCircleOutlined } from '@ant-design/icons';
import {attendActivity, cancelActivity, getBtnType} from "../../api/attendance";
import {deleteComment, getAvgScoreById, getCommentById} from "../../api/comment";
import {toast, ToastOptions} from "react-toastify";
import {deleteActivity, getActivityById} from "../../api/activity";
import {useNavigate} from "react-router-dom";
import Comment from "../Comment/Comment";
import UserPanel from "../Tool/UserPanel";

const { Title, Text, Paragraph } = Typography;

// 模拟活动数据
const mockActivity = {
    id: 0,
    name: '',
    image: '',
    date: new Date(),
    detail: '',
    cost: 0,
    limitCount: 0
};

interface Comment{
    id: number;
    score: number;
    detail: string;
    account:{
        id:string;
        username:string;
        avatar:string;
    };
    activity:{
        id: number;
    }
}

function ActivityDetailPage() {
    const navigate = useNavigate(); // 取消注释并安装 react-router-dom 使用
    const [activity,setActivity] = useState(mockActivity);
    const [isJoined, setIsJoined] = useState(false);
    const [avgScore, setAvgScore] = useState(0.0);
    const [comments, setComments] = useState<Comment[]>([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [loading, setLoading] = useState(false);
    const [buttonLoading, setButtonLoading] = useState(false);

    const pageSize = 10;
    const userRole = sessionStorage.getItem('role') || 'User';
    const userId = Number(sessionStorage.getItem('userId')) || 0;
    const activityId = Number(sessionStorage.getItem('activityId')) || 0;


    const toastConfig: ToastOptions = {
        position: 'top-center',
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true
    }

    // 格式化日期时间
    const formatDateTime = (dateString:any) => {
        const date = new Date(dateString)
        return `${date.getFullYear()}/${String(date.getMonth() + 1).padStart(2, '0')}/${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')} `;
    };

    const fetchActivityInfo=async()=>{
        try {
            const res = await getActivityById(activityId)
            if(res.data.code==='200')
                setActivity(res.data.data)
            else{
                toast.error(`获取按钮失败:${res.data.message}`,toastConfig);
            }
        } catch (error) {
            toast.error(`获取按钮失败:${error}`,toastConfig);
        }
    }

    // 获取按钮状态
    const fetchButtonType = async () => {
        try {
            const res = await getBtnType(userId, activityId)
            if(res.data.code==='200')
                setIsJoined(res.data.data)
            else{
                toast.error(`获取按钮失败:${res.data.message}`,toastConfig);
            }
        } catch (error) {
            toast.error(`获取按钮失败:${error}`,toastConfig);
        }
    };

    // 获取平均评分
    const fetchAvgScore = async () => {
        try {
            const res= await getAvgScoreById(activityId)

            if(res.data.code==='200'){
                console.log('aaa=',res.data.data)
                const score=Number(res.data.data)
                console.log('avgScore=',score)
                setAvgScore(score)
            }
            else{
                toast.error(`获取平均分失败:${res.data.message}`,toastConfig)
            }

        } catch (error) {
            toast.error(`获取平均评分失败:${error}`, toastConfig);
        }
    };

    // 获取评论列表
    const fetchComments = async () => {
        setLoading(true);
        try {
            const res=await getCommentById(activityId)
            if(res.data.code==='200'){
                const allComments=Object.values<Comment>(res.data.data)
                setComments(allComments);
            }
            else{
                toast.error(`获取评论列表失败:${res.data.message}`,toastConfig)
                setComments([])
            }

        } catch (error) {
            toast.error(`获取评论失败:${error}`,toastConfig);
        } finally {
            setLoading(false);
        }
    }

    // 处理参加/取消活动
    const handleActivityAction = async () => {
        setButtonLoading(true);
        try {
            if (isJoined) {
                const res = await cancelActivity(userId, activityId)

                if(res.data.code==='200'){
                    toast.success('取消活动成功',toastConfig)
                    setIsJoined(false)
                }
                else{
                    toast.error(`取消活动失败:${res.data.message}`,toastConfig)
                }



            } else {
                const res=await attendActivity(userId, activityId)
                if(res.data.code==='200'){
                    toast.success('成功参加活动',toastConfig)
                    setIsJoined(true)
                }
                else{
                    toast.error(`参加活动失败:${res.data.message}`,toastConfig)
                }


            }
        } catch (error) {
            toast.error(`参加/取消活动失败`,toastConfig)
        } finally {
            setButtonLoading(false);
        }
    };

    // 删除活动
    const handleDeleteActivity = () => {
            if(window.confirm(`确定要删除编号为${activityId}的活动吗`)) {
                try {
                    deleteActivity(activityId).then((res) => {
                        if(res.data.code==='200'){
                            toast.success('活动已删除,即将返回上一页',toastConfig)
                            navigate('/activities')
                        }
                        else{
                            toast.error(`删除失败:${res.data.message}`,toastConfig)
                        }
                    })

                } catch (error) {
                    toast.error('删除失败，请重试',toastConfig);
                }
            }

    };

    // 删除评论
    const handleDeleteComment = async(comment:any) => {
        if(window.confirm('确定要删除这条评论吗')){
            try {
                const res= await deleteComment(comment.account.id, comment.activity.id)
                    if(res.data.code==='200'){
                        toast.success('评论删除成功',toastConfig)
                        fetchComments()
                    }
                    else{
                        toast.error(`评论删除失败:${res.data.message}`,toastConfig)
                    }
            } catch (error) {
                toast.error(`评论删除失败:${error}`,toastConfig)
            }

        }
    };

    // 获取当前页的评论
    const getCurrentPageComments = () => {
        const startIndex = (currentPage - 1) * pageSize;
        const endIndex = startIndex + pageSize;
        return comments.slice(startIndex, endIndex);
    };

    //异步的
    useEffect(() => {
        const fetchData = async () => {
            await fetchButtonType();  // 等待 fetchButtonType 执行完成
            await fetchActivityInfo();  // 等待 fetchActivityInfo 执行完成
            await fetchAvgScore();  // 等待 fetchAvgScore 执行完成
            await fetchComments();  // 等待 fetchComments 执行完成
        };

        fetchData(); // 调用异步函数

    }, []);

    return (
        <div>
            <UserPanel></UserPanel>
            <div style={{ padding: '20px', maxWidth: '1200px', margin: '0 auto', backgroundColor: '#f5f5f5' }}>
                <Row gutter={24}>
                    {/* 左侧活动详情 */}
                    <Col xs={24} lg={14}>
                        <Card style={{ borderRadius: '12px', marginBottom: '20px' }}>
                            {/* 活动图片 */}
                            <div style={{ marginBottom: '20px' }}>
                                <img
                                    src={activity.image}
                                    alt={activity.name}
                                    style={{
                                        width: '100%',
                                        height: '300px',
                                        objectFit: 'cover',
                                        borderRadius: '8px'
                                    }}
                                />
                            </div>

                            {/* 活动标题 */}
                            <Title level={2} style={{ marginBottom: '20px', color: '#1890ff' }}>
                                {activity.name}
                            </Title>

                            {/* 活动详情 */}
                            <div style={{ marginBottom: '30px' }}>
                                <Title level={4} style={{ marginBottom: '16px' }}>活动详情</Title>

                                <div style={{ marginBottom: '12px' }}>
                                    <Text strong>截止报名时间</Text>
                                    <Text>{formatDateTime(activity.date)}</Text>
                                </div>

                                <div style={{ marginBottom: '12px' }}>
                                    <Text strong>剩余名额数：</Text>
                                    <Text>{activity.limitCount}</Text>
                                </div>

                                <div style={{ marginBottom: '20px' }}>
                                    <Text strong>活动费用：</Text>
                                    <Text style={{ color: '#ff4d4f', fontSize: '16px', fontWeight: 'bold' }}>
                                        ¥{activity.cost}
                                    </Text>
                                </div>

                                <div>
                                    <Title level={4} style={{ marginBottom: '12px' }}>活动描述</Title>
                                    <Paragraph style={{ fontSize: '14px', lineHeight: '1.6' }}>
                                        {activity.detail||'暂无'}
                                    </Paragraph>
                                </div>
                            </div>

                            {/* 操作按钮 */}
                            <div style={{ textAlign: 'center' }}>
                                <Space size="middle">

                                    <Button
                                        type="primary"
                                        size="large"
                                        loading={buttonLoading}
                                        onClick={handleActivityAction}
                                        style={{
                                            borderRadius: '8px',
                                            minWidth: '120px',
                                            backgroundColor: isJoined ? '#ff4d4f' : '#1890ff'
                                    }}
                                    >
                                        {isJoined ? '取消活动' : '参加活动'}
                                    </Button>

                                    {userRole === 'Admin' && (
                                        <>
                                            <Button
                                                size="large"
                                                icon={<DeleteOutlined />}
                                                onClick={handleDeleteActivity}
                                                style={{ borderRadius: '8px', minWidth: '120px' }}
                                            >
                                                删除活动
                                            </Button>
                                        </>
                                    )}
                                </Space>
                            </div>
                        </Card>
                    </Col>

                    {/* 右侧评论区 */}
                    <Col xs={24} lg={10}>
                        <Card style={{ borderRadius: '12px' }}>
                            <Title level={4} style={{ marginBottom: '20px' }}>评论</Title>

                            {/* 评分显示和写评论按钮 */}
                            <div style={{
                                marginBottom: '20px',
                                padding: '16px',
                                backgroundColor: '#f8f9fa',
                                borderRadius: '8px',
                                display: 'flex',
                                justifyContent: 'space-between',
                                alignItems: 'center'
                            }}>
                                <div>
                                    <Text strong>平均评分：</Text>
                                    <Rate disabled value={avgScore/2} allowHalf style={{ marginLeft: '8px' }} />
                                    <Text style={{ fontSize:'16px',marginLeft: '10px', color: '#666' }}>
                                        {avgScore}
                                    </Text>
                                </div>
                                <Button type="primary" onClick={()=>{navigate('/comment')}} style={{ borderRadius: '6px' }}>
                                    写下我的评论
                                </Button>
                            </div>

                            {/* 评论列表 */}
                            <div style={{ marginBottom: '20px' }}>
                                {loading ? (
                                    <div style={{ textAlign: 'center', padding: '40px' }}>
                                        <Text>加载中...</Text>
                                    </div>
                                ) : getCurrentPageComments().length === 0 ? (
                                    <div style={{ textAlign: 'center', padding: '40px' }}>
                                        <Text type="secondary">暂无评论</Text>
                                    </div>
                                ) : (
                                    getCurrentPageComments().map((comment) => (
                                        <Card
                                            key={comment.id}
                                            size="small"
                                            style={{
                                                marginBottom: '12px',
                                                borderRadius: '8px',
                                                border: '1px solid #e8e8e8'
                                            }}
                                        >
                                            {/* 用户信息行 */}
                                            <div style={{
                                                display: 'flex',
                                                alignItems: 'center',
                                                marginBottom: '8px',
                                                justifyContent: 'space-between'
                                            }}>
                                                <div style={{ display: 'flex', alignItems: 'center' }}>
                                                    <Avatar
                                                        src={comment.account.avatar ||'/avatar.png'}
                                                        icon={<UserOutlined />}
                                                        size={32}
                                                        onError={() => {
                                                            // 头像加载失败时的处理
                                                            return true;
                                                        }}
                                                        style={{ marginRight: '8px' }}
                                                    />
                                                    <Text strong style={{ fontSize: '14px' }}>
                                                        {comment.account.username}
                                                    </Text>
                                                </div>

                                                {userRole === 'Admin' && (
                                                    <Button
                                                        type="text"
                                                        danger
                                                        size="small"
                                                        icon={<DeleteOutlined />}
                                                        onClick={() => handleDeleteComment(comment)}
                                                    />
                                                )}
                                            </div>

                                            {/* 评分行 */}
                                            <div style={{ marginBottom: '8px' }}>
                                                {comment.score === 0 ? (
                                                    <Text type="secondary" style={{ fontSize: '12px' }}>
                                                        没有评分
                                                    </Text>
                                                ) : (<div>
                                                        <Rate disabled allowHalf value={comment.score/2}/>
                                                        <label style={{fontSize:'16px',marginLeft:'10px'}}>{comment.score}分</label>

                                                    </div>)


                                                }
                                            </div>

                                            {/* 评论内容 */}
                                            <Paragraph
                                                style={{
                                                    margin: 0,
                                                    fontSize: '13px',
                                                    lineHeight: '1.5',
                                                    color: '#333'
                                                }}
                                            >
                                                {comment.detail}
                                            </Paragraph>
                                        </Card>
                                    ))
                                )}
                            </div>

                            {/* 分页 */}
                            {comments.length > pageSize && (
                                <div style={{ textAlign: 'center' }}>
                                    <Pagination
                                        current={currentPage}
                                        pageSize={pageSize}
                                        total={comments.length}
                                        onChange={setCurrentPage}
                                        showSizeChanger={false}
                                        showQuickJumper={false}
                                        size="small"
                                    />
                                </div>
                            )}
                        </Card>
                    </Col>
                </Row>
            </div>
        </div>

    );
}

export default ActivityDetailPage;