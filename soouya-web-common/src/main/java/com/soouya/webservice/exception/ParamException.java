package com.soouya.webservice.exception;


import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.WebFault;
import javax.xml.ws.soap.SOAPFaultException;

/**
 * 参数错误异常：当客户端填入参数错误时抛出
 * 
 * @author xuyuli
 * @version 2016-02-27
 */
@WebFault
public class ParamException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private MyFault faultInfo;

	private Integer code;
	
	public ParamException(String s, MyFault faultInfo) {
		super(s); 
		this.faultInfo = faultInfo; 
	}

	public ParamException(int code, String msg, Throwable throwable) {
		super(msg,throwable);
		this.faultInfo = new MyFault(msg,code);
	}

	
	
	public MyFault getFaultInfo() {
		return faultInfo;
	}



	public void setFaultInfo(MyFault faultInfo) {
		this.faultInfo = faultInfo;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	@Override
	public Throwable getCause() {
		SOAPFault fault;
		try {
			fault = SOAPFactory.newInstance().createFault();
			fault.setFaultCode(new QName(this.getFaultInfo().getErrMsg(), this.getFaultInfo().getErrCode()+""));
			fault.setFaultString(this.getFaultInfo().toString());
			SOAPFaultException ex = new SOAPFaultException(fault);
			return ex;
		} catch (SOAPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return super.getCause();
		}
	}
	

	/**
	 * 构造方法
	 * 
	 * @param msg
	 *            : 异常的详细内容
	 */
	public ParamException(String msg) {
		super(msg);
	}

	public Integer getCode() {
		if(faultInfo!=null)
			return faultInfo.getErrCode();
		else
			return 0;
	}

	public String getMsg() {
		if(faultInfo!=null)
			return faultInfo.getErrMsg();
		else
			return "";
	}
}
