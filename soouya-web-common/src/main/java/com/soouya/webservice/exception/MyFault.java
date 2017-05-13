package com.soouya.webservice.exception;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "http://exception.bean.management.error.webos.proj.nightstudio.org/", name = "com.soouya.webservice.exception.MyFault", propOrder = {
		"errMsg",
		"errCode"
})
public class MyFault {

	
	private String errMsg; 
	private int errCode;

	public MyFault(String errMsg, int errCode) {
		this.errMsg = errMsg;
		this.errCode = errCode;
	}

	public MyFault() {
	}

	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public int getErrCode() {
		return errCode;
	}
	public void setErrCode(int errCode) {
		this.errCode = errCode;
	} 
	
	
}
