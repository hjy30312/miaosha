package com.hjy.miaosha.result;


public class CodeMsg {
    private int code;
    private String msg;

    //通用异常
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BING_ERROR = new CodeMsg(500101, "参数校验异常： %s");
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102, "请求非法");
    public static CodeMsg VERIFYCODE_ERROR = new CodeMsg(500102, "验证码错误");
    public static CodeMsg DELETE_SUCCESS = new CodeMsg(500103, "删除成功");
    public static CodeMsg DELETE_ERROR = new CodeMsg(500104, "删除失败");



    //登录注册模块 5002XX
    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或者已经失效");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号不能为空");
    public static CodeMsg MOBLIE_ERROR = new CodeMsg(500213, "手机号格式错误");
    public static CodeMsg MOBLIE_NOT_EXIST = new CodeMsg(500214, "手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");
    public static CodeMsg MOBLIE_EXIST = new CodeMsg(500216, "手机号已存在");
    public static CodeMsg REGISTER_FALSE = new CodeMsg(500217, "注册失败");
    public static CodeMsg MOBLIE_CHECK_ERROR = new CodeMsg(500218, "短信服务异常");

    //商品模块 5003XX
    public static CodeMsg GOODS_INSESRT_ERROR = new CodeMsg(500310, "商品添加失败");
    public static CodeMsg GOODS_INSETRT_SUCCESS = new CodeMsg(500311, "商品添加成功");
    public static CodeMsg MIAOSHA_GOODS_INSESRT_ERROR = new CodeMsg(500310, "秒杀商品添加失败");
    public static CodeMsg MIAOSHA_GOODS_INSETRT_SUCCESS = new CodeMsg(500311, "秒杀商品添加成功");


    //订单模块 5004XX
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500401, "订单不存在");
    //秒杀模块 5005XX
    public static CodeMsg MIAO_SHA_OVER = new CodeMsg(500500, "商品已秒杀完毕");
    public static CodeMsg REPEATE_MIAOSHA = new CodeMsg(500501, "不能重复秒杀");
    public static final CodeMsg MIAOSHA_FAIL = new CodeMsg(500502, "秒杀失败");

    /**
     * 填充的msg  变参
     *
     * @param args
     * @return
     */
    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }


    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
