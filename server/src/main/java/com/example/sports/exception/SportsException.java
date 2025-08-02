package com.example.sports.exception;

public class SportsException extends RuntimeException {
    public SportsException(String message){super(message);}

    public static SportsException WrongTelephone(){
        return new SportsException("电话号码不存在，请检查后重试");
    }

    public static SportsException WrongPassword(){
        return new SportsException("密码错误，请重新输入");
    }

    public static SportsException WrongFormat(){
        return new SportsException("输入格式不符合要求，请检查后重试");
    }

    public static SportsException ActivityNotFound(){
        return new SportsException("未找到对应活动");
    }

    public static SportsException NoEnoughArguments(){
        return new SportsException("请求参数不足，请检查请求内容");
    }

    public static SportsException InvalidPhoneNumber() {
        return new SportsException("手机号格式不正确，请输入11位有效手机号");
    }

    public static SportsException notLogin() { return new SportsException("未登录，请先登录");}

    public static SportsException phoneAlreadyExists(){return new SportsException("手机号已存在，请使用其他手机号注册");}

    public static SportsException ossError(){return new SportsException("oss服务出现异常");}

    public static SportsException invalidUserId(){return new SportsException("用户ID不合法");}

    public static SportsException activityFull(){return new SportsException("活动人数已满,请在有空余名额时重试");}

    public static SportsException fileUploadFail() {
        return new SportsException("文件上传失败!");
    }

    public static SportsException CommentNotFound(){return new SportsException("你的评论不存在");}

    public static SportsException NoAccession(){return new SportsException("无权限");}

    public static SportsException ActivityAlreadyUnaccessible(){return new SportsException("活动已经过期");}

    public static SportsException ActivityAlreadyJoined(){return new SportsException("已经报名了此次活动");}

    public static SportsException ActivityNotJoined(){return new SportsException("没有报名此次活动");}
}
