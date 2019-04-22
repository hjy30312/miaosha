package com.hjy.miaosha.exception;


import com.aliyuncs.exceptions.ClientException;
import com.hjy.miaosha.result.CodeMsg;
import com.hjy.miaosha.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 异常处理器
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionHandler(HttpServletResponse response,
                                           Exception e) {
        e.printStackTrace();
        if (e instanceof GlobalException) {
            GlobalException ex= (GlobalException)e;
            return Result.error(ex.getCm());
        } else if (e instanceof BindException) {
            //绑定异
            BindException ex = (BindException) e;
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            //填充参数异常信息
            return Result.error(CodeMsg.BING_ERROR.fillArgs(msg));
        } else {
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
