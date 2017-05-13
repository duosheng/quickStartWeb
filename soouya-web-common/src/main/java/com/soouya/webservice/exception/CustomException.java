package com.soouya.webservice.exception;


import javax.xml.ws.WebFault;

/**
 * 自定义异常：当系统处理遇到异常时抛出
 * @author xuyuli
 */
@WebFault
public class CustomException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * 构造方法
	 */
	public CustomException(){
		super();
	}
	
	/**
	 * 构造方法
	 * @param msg : 异常的详细内容
	 */
	public CustomException(String msg){
		super(msg);
	}

}
