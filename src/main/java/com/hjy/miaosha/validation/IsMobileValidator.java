package com.hjy.miaosha.validation;

import com.hjy.miaosha.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


//IsMobile: 自定义的注解
//String: 注解参数类型
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

	//默认值， 用于接收注解上自定义的required  因为可为空所以则初始化为false
	private boolean required = false;

	//1、 初始化方法： 通过该方法我们可以拿到我们的注解
	@Override
	public void initialize(IsMobile constraintAnnotation) {
		required = constraintAnnotation.required();
	}

	//2、逻辑处理
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		//2.1、如果值必须有的话，直接判断返回结果
		if(required) {
			return ValidatorUtil.isMobile(value);
		} else {
		//2.2 若不必须就判断是否有值2
			if(StringUtils.isEmpty(value)) {
				return true;
			}else {
				return ValidatorUtil.isMobile(value);
			}
		}
	}

}