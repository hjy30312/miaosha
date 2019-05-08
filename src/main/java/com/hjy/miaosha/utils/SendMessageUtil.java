package com.hjy.miaosha.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
public class SendMessageUtil {

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "LTAIzobu9Ad63wvC";
    static final String accessKeySecret = "xsxFzHKAhIMd4zjxULGAjfrRl0pCkC";

    //发短信
    public static CommonResponse sendSms(String telephone,String code) throws ClientException {

        DefaultProfile profile = DefaultProfile.getProfile("default", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");

        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", telephone);
        request.putQueryParameter("SignName", "清清秒杀");
        request.putQueryParameter("TemplateCode", "SMS_163439154");
        String jsonContent = "{\"code\":\"" + code + "\"}";
        request.putQueryParameter("TemplateParam", jsonContent);
        CommonResponse response = null;
        try {
            response = client.getCommonResponse(request);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static void main(String[] args) {
        try {
            System.out.println(SendMessageUtil.sendSms("18772842517", "123456"));
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}