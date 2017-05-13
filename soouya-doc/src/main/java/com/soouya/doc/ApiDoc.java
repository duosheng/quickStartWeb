package com.soouya.doc;

import javacommon.util.NullClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiDoc {

	/**
	 * 参数类型
     */
	Class param() default NullClass.class;
	/**
	 * 返回类型
     */
	Class vo() default NullClass.class;
	/**
	 * page的泛型
	 */
	Class page() default NullClass.class;

	enum Need_Page{NO,YES}

	Need_Page needPage() default Need_Page.YES;

			/**
             * 路径
             */
	String path() default "";
	/**
	 * api的描述
     */
	String desc() default "";

	enum Old_Style{NO,YES}

	Old_Style oldStyle() default Old_Style.NO;

    /**
     * 模块名
	 * @return
     */
	TagsEnum[] tag();

	enum Mock{NO,YES}

	/**
	 * 是否是mock数据,默认不是mock数据
	 * @return
	 */
	Mock mock() default Mock.NO;
}
