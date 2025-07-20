package com.example.sports.exception;

public class SportsException extends RuntimeException {
    public SportsException(String message){super(message);}

    public static SportsException WrongUsername(){
        return new SportsException("用户名不存在，请检查后重试");
    }

    public static SportsException WrongPassword(){
        return new SportsException("密码错误，请重新输入");
    }

    public static SportsException WrongFormat(){
        return new SportsException("输入格式不符合要求，请检查后重试");
    }

    public static SportsException DuplicateName(){
        return new SportsException("用户名已被使用，请尝试其他用户名");
    }

    public static SportsException ProductNotFound(){
        return new SportsException("未找到对应商品，请检查商品ID");
    }

    public static SportsException DuplicateProduct(){
        return new SportsException("已存在同名商品，请修改商品名称");
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

    public static SportsException notEnoughStock(){return new SportsException("库存不足");}

    public static SportsException invalidUserId(){return new SportsException("用户ID不合法");}

    public static SportsException productSoldOut(){return new SportsException("商品已售罄");}

    public static SportsException cartItemNotFound(){return new SportsException("购物车中未找到对应的商品项");}

    public static SportsException getItemListFailed(){return new SportsException("获取购物车商品项列表失败");}

    public static SportsException OrderNotFound() {return new SportsException("订单不存在");}

    public static SportsException OrderStatusError() {return new SportsException("只能取消待处理的订单");}

    public static SportsException OrderPayStatusError() {return new SportsException("只能支付待处理的订单");}

    public static SportsException InvaildProductAmount(){return new SportsException("商品数量不合法");}

    public static SportsException fileUploadFail() {
        return new SportsException("文件上传失败!");
    }

    public static SportsException AdNotFound(){return new SportsException("广告id不存在");}

    public static SportsException DuplicateFavor(){return new SportsException("已经收藏该商品");}

    public static SportsException FavorNotFound(){return new SportsException("你的收藏不存在");};

    public static SportsException CommentNotFound(){return new SportsException("你的评论不存在");}

    public static SportsException NoAccession(){return new SportsException("无上述权限");}
}
