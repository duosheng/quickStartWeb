package com.soouya.springmvc;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import javacommon.base.BaseVo;
import lombok.extern.log4j.Log4j;
import net.sf.json.JSONObject;
import org.apache.log4j.MDC;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServletServerHttpResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

@Log4j
public class JsonConverter extends MappingJackson2HttpMessageConverter {


	@Override
	public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

		Object read = super.read(type, contextClass, inputMessage);
		javacommon.util.ValidateUtil.valid(read);
		MDC.put("param", JSONObject.fromObject(read).toString());
		return read;
	}

	protected void fillMap(Map map) {
		String success = (String) map.get("success");
		success = success == null ? "1" : success;
		String msg = map.get("msg") == null ? null : map.get("msg").toString();
		ResourceBundle messages = ResourceBundle.getBundle("i18n.messages", new Locale("zh", "CN"));
		try {
			msg = msg == null ? messages.getString(success) : messages.getString(msg);
		} catch (Exception e) {
		}
		map.put("success", success);
		map.put("msg", msg);
	}



	@Override
	protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
		JsonEncoding encoding = getJsonEncoding(outputMessage.getHeaders().getContentType());
		JsonGenerator generator = this.objectMapper.getFactory().createGenerator(outputMessage.getBody(), encoding);
		ServletServerHttpResponse response = (ServletServerHttpResponse) outputMessage;
		PrintWriter generator2 = response.getServletResponse().getWriter();

//		objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(new CustomerBeanSerializerModifier()));
		try {
			if (object instanceof Map) {
				fillMap((Map) object);
			}
			if (object instanceof BaseVo) {
				BaseVo object1 = (BaseVo) object;
				String code = "1"; //SUCCESS
				object1.setSuccess(code);
				object1.setMsg("操作成功");
			}

			writePrefix(generator, object);
			Class<?> serializationView = null;
			Object value = object;
			if (value instanceof MappingJacksonValue) {
				MappingJacksonValue container = (MappingJacksonValue) object;
				value = container.getValue();
				serializationView = container.getSerializationView();
			}
			if (serializationView != null) {
				this.objectMapper.writerWithView(serializationView).writeValue(generator, value);
				this.objectMapper.writerWithView(serializationView).writeValue(generator2, value);// leo
				// add
			} else {
				this.objectMapper.writeValue(generator, value);
				this.objectMapper.writeValue(generator2, value);// leo add
			}
			writeSuffix(generator, object);
			generator.flush();
			// generator2.flush();//leo add

		} catch (JsonProcessingException ex) {
			log.error(ex.getMessage(),ex);
			throw new HttpMessageNotWritableException("Could not write content: " + ex.getMessage(), ex);
		}
	}
}
