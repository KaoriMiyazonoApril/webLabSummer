import React, { useState } from 'react';
import { Input, Button, Card, Typography, Space, message } from 'antd';
import { StarFilled, StarOutlined } from '@ant-design/icons';
import {addComment} from "../../api/comment";
import {toast, ToastOptions} from "react-toastify";
import {useNavigate} from "react-router-dom";
import UserPanel from "../Tool/UserPanel";

const { TextArea } = Input;
const { Title, Text } = Typography;

function StarRatingForm() {
    const [rating, setRating] = useState(0); // 当前评分
    const [hoverRating, setHoverRating] = useState(0); // 鼠标悬停的评分
    const [comment, setComment] = useState(''); // 评价内容
    const toastConfig: ToastOptions = {
        position: 'top-center',
        autoClose: 3000,
        hideProgressBar: false,
        closeOnClick: true,
        pauseOnHover: true
    }
    const navigate=useNavigate();

    // 处理星星点击
    const handleStarClick = (index:any) => {
        setRating(index);
    };

    // 处理鼠标悬停
    const handleStarHover = (index:any) => {
        setHoverRating(index);
    };

    // 处理鼠标离开
    const handleStarLeave = () => {
        setHoverRating(0);
    };

    // 提交评价
    const handleSubmit = () => {
        const commentInfo={id:0,activityId:Number(sessionStorage.getItem('activityId'))
            ,userId:Number(sessionStorage.getItem('userId')),detail:comment.trim(),score:rating};

        addComment(commentInfo).then(res=>{
            if(res.data.code === '200') {
                toast.success("评论上传成功,即将返回活动页面",toastConfig);
                navigate('/activity')
            }
            else{
                toast.error(`评论失败:${res.data.message}`,toastConfig)
            }
        })
    };

    // 重置表单
    const handleReset = () => {
        setRating(0);
        setComment('');
        message.info('已重置');
    };

    // 渲染星星
    const renderStars = () => {
        const stars = [];
        const displayRating = hoverRating || rating;

        for (let i = 1; i <= 10; i++) {
            const isActive = i <= displayRating;
            stars.push(
                <span
                    key={i}
                    onClick={() => handleStarClick(i)}
                    onMouseEnter={() => handleStarHover(i)}
                    onMouseLeave={handleStarLeave}
                    style={{
                        cursor: 'pointer',
                        fontSize: '28px',
                        color: isActive ? '#fadb14' : '#d9d9d9',
                        transition: 'color 0.2s ease',
                        margin: '0 4px',
                        display: 'inline-block',
                        transform: hoverRating === i ? 'scale(1.1)' : 'scale(1)',
                    }}
                >
          {isActive ? <StarFilled /> : <StarOutlined />}
        </span>
            );
        }
        return stars;
    };

    // 获取评分文字描述
    const getRatingText = (score:any) => {
        if (score === 0) return '';
        if (score <= 2) return '很差';
        if (score <= 4) return '较差';
        if (score <= 6) return '一般';
        if (score <= 8) return '良好';
        return '优秀';
    };

    return (
        <div>
            <UserPanel></UserPanel>
            <div style={{
                marginTop:'30px',
                padding: '40px',
                maxWidth: '600px',
                margin: '0 auto',
            }}>

                <Card
                    style={{
                        marginTop:'30px',
                    }}
                >
                    <Space direction="vertical" size="large" style={{ width: '100%' }}>
                        <div style={{ textAlign: 'center' }}>
                            <Title level={2} style={{ color: '#1890ff', marginBottom: '8px' }}>
                                活动评价
                            </Title>
                        </div>

                        {/* 星级评分区域 */}
                        <div style={{ textAlign: 'center' }}>


                            <div style={{ marginBottom: '12px' }}>
                                {renderStars()}
                            </div>

                            <div style={{ height: '30px' }}>
                                {(hoverRating || rating) > 0 && (
                                    <div>
                                        <Text style={{ fontSize: '18px', fontWeight: 'bold', color: '#1890ff' }}>
                                            {hoverRating || rating} 分
                                        </Text>
                                        <Text style={{ marginLeft: '12px', color: '#666' }}>
                                            {getRatingText(hoverRating || rating)}
                                        </Text>
                                    </div>
                                )}
                            </div>
                        </div>

                        {/* 评价输入区域 */}
                        <div>
                            <div style={{ marginBottom: '12px' }}>
                                <Text strong style={{ fontSize: '16px' }}>
                                    详细评价
                                </Text>
                            </div>

                            <TextArea
                                value={comment}
                                onChange={(e) => setComment(e.target.value)}
                                placeholder="请输入您对本次活动的评价和建议..."
                                rows={4}
                                maxLength={500}
                                showCount
                                style={{
                                    borderRadius: '8px',
                                    fontSize: '14px'
                                }}
                            />
                        </div>

                        {/* 按钮区域 */}
                        <div style={{ textAlign: 'center' }}>
                            <Space size="middle">
                                <Button
                                    type="default"
                                    size="large"
                                    onClick={handleReset}
                                    style={{
                                        borderRadius: '8px',
                                        width: '100px'
                                    }}
                                >
                                    重置
                                </Button>

                                <Button
                                    type="primary"
                                    size="large"
                                    onClick={handleSubmit}
                                    disabled={rating === 0 && comment.trim()===''}
                                    style={{
                                        borderRadius: '8px',
                                        width: '120px',
                                    }}
                                >
                                    提交评价
                                </Button>
                            </Space>
                        </div>

                    </Space>
                </Card>
            </div>
        </div>

    );
}

export default StarRatingForm;