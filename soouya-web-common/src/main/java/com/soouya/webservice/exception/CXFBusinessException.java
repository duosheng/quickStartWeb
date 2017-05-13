package com.soouya.webservice.exception;


import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.WebFault;
import javax.xml.ws.soap.SOAPFaultException;

/**
 * webservice异常
 * 
 * @author xuyuli
 * @version 2016-02-27
 */
//@WebFault(faultBean="com.soouya.webservice.exception.MyFault", name="WebOSException", targetNamespace="http://exception.bean.management.error.webos.proj.nightstudio.org/")
@WebFault
public class CXFBusinessException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private MyFault faultInfo;
	

	public CXFBusinessException(String reason) {
		super(reason);
	}

	public CXFBusinessException(String s, MyFault faultInfo) {
		super(s); 
		this.faultInfo = faultInfo;
	}

	/**
	 * 构造方法
	 * 
	 * @param msg
	 *            : 异常的详细内容
	 */
	public CXFBusinessException(int code, String msg, Throwable throwable) {

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
}
