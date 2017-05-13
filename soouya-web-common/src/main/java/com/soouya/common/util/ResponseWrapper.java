package com.soouya.common.util;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class ResponseWrapper extends HttpServletResponseWrapper {

	  private StringWriter sw = new StringWriter(1024);

//	  private FileWriter buf = null;

	  private int httpStatus=200;

	  public ResponseWrapper(HttpServletResponse response) {
	    super(response);
//		  try {
//			  buf = new FileWriter("/tmp/test1.text");
//		  } catch (IOException e) {
//			  e.printStackTrace();
//		  }
	  }

	  public PrintWriter getWriter() throws IOException {
//		  PrintWriter printWriter = new PrintWriter(buf);
//		  printWriter.flush();
//		  return printWriter;
		  return new PrintWriter(sw);
	  }

	  /*public ServletOutputStream getOutputStream() throws IOException {
	    throw new UnsupportedOperationException();
	  }*/

//	  public String toString() {
//	    return sw.toString();
//	  }
	  
	   @Override
	    public void sendError(int sc) throws IOException {
	        httpStatus = sc;
	        super.sendError(sc);
	    }

	    @Override
	    public void sendError(int sc, String msg) throws IOException {
	        httpStatus = sc;
	        super.sendError(sc, msg);
	    }


	    @Override
	    public void setStatus(int sc) {
	        httpStatus = sc;
	        super.setStatus(sc);
	    }

	    public int getStatus() {
	        return httpStatus;
	    }
}
