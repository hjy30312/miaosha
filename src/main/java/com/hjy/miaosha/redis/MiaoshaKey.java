package com.hjy.miaosha.redis;

public class MiaoshaKey extends BasePrefix{



    public MiaoshaKey(String prefix) {
        super(prefix);
    }
    public MiaoshaKey( int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static MiaoshaKey isGoodsOver = new MiaoshaKey("go");
    public static MiaoshaKey isMiaoshaPath = new MiaoshaKey(60,"mp");
    public static MiaoshaKey getMiaoshaVerifyCode = new MiaoshaKey(300,"vc");
    public static MiaoshaKey isGoodsMiaoshaSort = new MiaoshaKey("sort");

}

