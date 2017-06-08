package javacommon.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonDoc {

	/**
	 * 描述
     */
	String description() default "";

	/**
	 * 默认值
     */
	String def() default "";

	/**
	 * 默认值
	 */
	int size() default 0;

	/**
	 *主键，默认为空
     */
	boolean primaryKey() default false;

	/**
	 * 默认允许为空
     */
	boolean allowNull() default true;

    /**
	 * 默认允许重复
     */
	boolean unique() default false;

	//提供几种常用的正则验证
	RegexType regexType() default RegexType.NONE;

	//自定义正则验证
	String regexExpression() default "";

}
