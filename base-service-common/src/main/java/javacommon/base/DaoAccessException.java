package javacommon.base;


/**
 * 数据访问层的错误提示
 */
public class DaoAccessException extends Exception{

	 public DaoAccessException(String message,Throwable t){
		 super(message,t);
	 }
	 
}
