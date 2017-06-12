package com.base.doc;

import com.base.common.util.DateHelper;
import javacommon.base.JsonDoc;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonDocHelper {


	private static List<Method> getMethodList(Class clazz){
		List<Method> lst = new ArrayList<Method>();
		while(clazz != Object.class){
			Method[] methodList = clazz.getDeclaredMethods();
			lst.addAll(Arrays.asList(methodList));

			clazz = clazz.getSuperclass();
		}
		return lst;
	}

	private final static String lineEnd = "\n";

	/**
	 *
	 * @param apiPath jsonDoc的完整api路径，例如 /sys/Seed/login.do
	 * @param tagList 接口隶属的tag
	 * @param operationId 操作id
	 * @param description 描述
	 * @param sunmmary 摘要
	 * @param paramClazz 参数类
	 * @param returnClazz 返回的结果类
	 * @param page 返回page时应该传的泛型class
	 *            应该加入List 泛型class
	 */
	public static String writeDoc(String apiPath, List<String> tagList, String operationId, String description, String sunmmary, Class paramClazz, Class returnClazz, Class page, String mock, boolean needPage, boolean oldStyle){
		StringBuilder sb = new StringBuilder();
		sb.append("\"").append(apiPath).append("\":{").append(lineEnd)
				.append("\"post\": {").append(lineEnd)
				.append("\"tags\": ").append(JSONArray.fromObject(tagList).toString()).append(",").append(lineEnd)
				.append("\"mock\": "+mock+",").append(lineEnd)
				.append("\"summary\": \"").append(sunmmary).append("\",").append(lineEnd)
				.append("\"description\": \"").append(description).append("\",").append(lineEnd)
				.append("\"operationId\": \"").append(operationId).append("\",").append(lineEnd)
				.append("\"produces\": [\"application/json\"],").append(lineEnd);

		sb.append(writeRetJson(returnClazz,page,needPage));
		sb.append(",");
		sb.append(lineEnd);
		if(oldStyle){
			sb.append(writeParametersWithGet(paramClazz));
		}else {
			sb.append(writeParameters(paramClazz));
		}

		sb.append("");
		sb.append(lineEnd);
		sb.append("}\n" +
				"    }");

		return sb.toString();

	}




	/**
	 *根据class生成返回parameters文档
	 * @param paramClazz
	 */
	private static String writeParameters(Class paramClazz){
		StringBuilder sb = new StringBuilder();
		sb.append("\"parameters\": [");
		sb.append("{\n" +
				"  \"name\": \"param\",\n" +
				"  \"in\": \"body\",\n" +
				"  \"description\": \"参数\",\n" +
				"  \"required\": true,\n" +
				"  \"schema\": {\n" +
				"    \"type\": \"object\",\n" +
				"    \"properties\": {");
		sb.append(lineEnd);
		sb.append(getModelDefine(paramClazz,null,true));
		sb.append(lineEnd);
		sb.append("}\n" +
				"}");
		sb.append("}");
		sb.append("]");
		return sb.toString();
	}

	//"parameters": [
	/**
	 *根据class生成返回parameters文档  兼容旧的风格
	 * @param paramClazz
	 */
	private static String writeParametersWithGet(Class paramClazz){
		StringBuilder sb = new StringBuilder();
		sb.append("\"parameters\": [");

		sb.append(getModelDefine4Parameter(paramClazz));
		sb.append(lineEnd);

		sb.append("]");
		return sb.toString();
	}


	/**
	 * 根据class生成返回responses文档
	 * @param returnClazz
	 * @return
	 */
	private static String writeRetJson(Class returnClazz, Class page, boolean needPage){

		StringBuilder sb = new StringBuilder();

		String reqStart = "\"responses\": {\n" +
				"    \"200\": {\n" +
				"      \"description\": \"\",\n" +
				"      \"schema\": {\n" +
				"        \"type\": \"object\",\n" +
				"        \"required\": [\n" +
				"          \"success\",\n" +
				"          \"msg\"\n" +
				"        ],\n" +
				"        \"properties\": {";

		String modelDefine = getModelDefine(returnClazz,page,needPage);

		String reqEnd = "}\n" +
				"      }\n" +
				"    }\n" +
				"  }";

		return sb.append(reqStart).append(modelDefine).append(reqEnd).toString();

	}


	/**
	 * 根据class获取返回的文档(就是带:的)
	 * needPage 备用字段,暂时用不着
	 * @param clazz
	 * @return
	 */
	public static String getModelDefine(Class<?> clazz, Class page, boolean needPage) {
		String className = clazz.getSimpleName();
		char chBegin = Character.toLowerCase(className.charAt(0));
		String model = new StringBuilder().append(chBegin).append(className.substring(1)).toString();

		StringBuilder sb = new StringBuilder();

		Field[] fs = clazz.getDeclaredFields();
		Class<?> superclass = clazz.getSuperclass();
		Field[] superFields = null;
		if(superclass!=null){
			superFields = superclass.getDeclaredFields();
			fs = ArrayUtils.addAll(fs, superFields);
		}

		for (int i = 0; i < fs.length; i++) {
			Field f = fs[i];
			int modifier = f.getModifiers();
			String desc = "";
			String def = "";
			boolean allowNull = true;
			JsonDoc doc = f.getAnnotation(JsonDoc.class);
			// 没有注解认为不需要出现在JsonDoc中,如果第一个没有加doc,认为下一个是第一个需要生成doc的第一个
			if (doc == null) {
				continue;
			}else {
				desc = doc.description();
				def = doc.def();
				allowNull = doc.allowNull();
				if(!allowNull){
					desc = desc + "(必传)";
				}
			}

			String name = f.getName();
			if (!Modifier.isStatic(modifier)) {
				Class<?> type = f.getType();
				String strTypeName = f.getType().getName();
				try {
					//如果是第一个不加逗号,其他都加逗号
					if(!sb.toString().endsWith(",")&&sb.toString().endsWith("}")){
						sb.append(",");
					}
					if (strTypeName.endsWith("String")) {
						if (StringUtils.isBlank(def)) {
							def = name;
						}
						sb.append(getDefine(model, name, desc, def, "string", null));
					} else if (strTypeName.endsWith("Double")||strTypeName.endsWith("double")) {
						double v = 1.00d;
						if (StringUtils.isNotBlank(def)) {
							v = Double.parseDouble(def);
						}
						sb.append(getDefine(model, name, desc, v, "number", "float"));
					} else if (strTypeName.endsWith("Float")) {
						double v = 1.00f;
						if (StringUtils.isNotBlank(def)) {
							v = Float.parseFloat(def);
						}
						sb.append(getDefine(model, name, desc, v, "number", "float"));
					} else if (strTypeName.endsWith("Date")) {
						long v = 1461918485;
						if (StringUtils.isNotBlank(def)) {
							v = DateHelper.parseToSimpleDateTime(def).getTime();
						}
						sb.append(getDefine(model, name, desc, v, "integer", "int64"));
					} else if (strTypeName.endsWith("Integer")||strTypeName.endsWith("int")) {
						Integer v = 2;
						if (StringUtils.isNotBlank(def)) {
							v = Integer.valueOf(def);
						}
						sb.append(getDefine(model, name, desc, v, "integer", "int32"));
					} else if (strTypeName.endsWith("Long")||strTypeName.endsWith("long")) {
						Long v = 2l;
						if (StringUtils.isNotBlank(def)) {
							v = Long.valueOf(def);
						}
						sb.append(getDefine(model, name, desc, v, "integer", "int64"));
					}else if(strTypeName.endsWith("List")){
						printList(model, sb, f, desc, def, name,page);

					}else if(strTypeName.endsWith("Page")||strTypeName.endsWith("PageVo")){
						printPage(page, sb, f, name,needPage);

					}else{  // object
						sb.append(printObj(type,name));
					}
				} catch (Exception e) {

				}
			}
		}
		return sb.toString();
	}

	/**
	 * 打印page
	 */
	private static void printPage(Class page, StringBuilder sb, Field f, String name, boolean needPage) {
		if(needPage){
			//打印page
			sb.append("\""+name+"\": {\n" +
					"    \"type\": \"object\",\n" +
					"    \"properties\": {\n" +
					"\"pageNumber\": {\n" +
					"                      \"$ref\": \"#/definitions/pageNumber\"\n" +
					"                    },\n" +
					"                    \"pageSize\": {\n" +
					"                      \"$ref\": \"#/definitions/pageSize\"\n" +
					"                    },\n" +
					"                    \"totalCount\": {\n" +
					"                      \"$ref\": \"#/definitions/totalCount\"\n" +
					"                    }," +
					"      \"result\": {\n" +
					"        \"type\": \"array\",\n" +
					"        \"items\": {\n" +
					"          \"type\": \"object\",\n" +
					"          \"properties\": {");
			Class lll = null;

			try {
				Type gt = f.getGenericType();//得到泛型类型
				ParameterizedType pt = (ParameterizedType)gt;
				lll = (Class)pt.getActualTypeArguments()[0];
			} catch (Exception e) {
				lll = page;//如果返回是page,只能使用传进来的泛型对象
			}
			sb.append(getModelDefine(lll,null,true));
			sb.append("}\n" +
					"        }\n" +
					"      }\n" +
					"    }\n" +
					"  }");
		}else {

		}

	}

	/**
	 * 打印list
	 */
	private static void printList(String model, StringBuilder sb, Field f, String desc, String def, String name, Class fanxing) {
		//打印list
		sb.append("\""+name+"\": {\n" +
				"                      \"type\": \"array\",\n" +
				"\"description\":\""+desc+"\","+
				"                      \"items\": ");

		Class lll = null;
		try {
			Type gt = f.getGenericType();//得到泛型类型
			ParameterizedType pt = (ParameterizedType)gt;
			lll = (Class)pt.getActualTypeArguments()[0];
		} catch (Exception e) {
			lll = fanxing;
		}
		//如果是简单类型,与复杂类型分开处理
		if(lll.getName().endsWith("String")){
			String v = "";
			if (StringUtils.isNotBlank(def)) {
				v = (String) JSONArray.fromObject(def).get(0);
			}
			sb.append(getDefine4Param(model, name, desc, v, "string", null,true));
		}else if(lll.getName().endsWith("Double")){
			double v = 1.00d;
			if (StringUtils.isNotBlank(def)) {
				v = (Double) JSONArray.fromObject(def).get(0);
			}
			sb.append(getDefine4Param(model, name, desc, v, "number", "double",true));
		}else if(lll.getName().endsWith("Integer")){
			Integer v = 2;
			if (StringUtils.isNotBlank(def)) {
				v = (Integer) JSONArray.fromObject(def).get(0);
			}
			sb.append(getDefine4Param(model, name, desc, v, "integer", "int32",true));
		}else {
			sb.append(printObjNoLable(lll));
		}
		sb.append("\n" +
				"                    }");
	}

	/**
	 * 打印object
	 */
	private static String printObj(Class<?> type, String name){
		StringBuilder sb = new StringBuilder();
		sb.append("\""+name+"\": {\n" +
				"                  \"type\": \"object\",\n" +
				"                  \"properties\": {");
		sb.append(getModelDefine(type,null,true));
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}

	/**
	 *
	 * @param type
	 */
	private static String printObjNoLable(Class<?> type){
		StringBuilder sb = new StringBuilder();
		sb.append("{\n" +
				"                  \"type\": \"object\",\n" +
				"                  \"properties\": {");
		sb.append(getModelDefine(type,null,true));
		sb.append("}");
		sb.append("}");
		return sb.toString();
	}

	/**
	 * 打印class的引用
	 * @param clazz
	 * @return
	 */
	private static String getModelObj(Class<?> clazz) {
		String className = clazz.getSimpleName();
		char chBegin = Character.toLowerCase(className.charAt(0));
		String model = new StringBuilder().append(chBegin).append(className.substring(1)).toString();

		StringBuilder sb = new StringBuilder("		\"" + model + "\": {\n");
		sb.append("			\"type\": \"object\",\n");
		sb.append("			\"properties\": {\n");

		Field[] fs = clazz.getDeclaredFields();

		for (Field f : fs) {
			int modifier = f.getModifiers();
			String name = f.getName();

			JsonDoc doc = f.getAnnotation(JsonDoc.class);

			if (!Modifier.isStatic(modifier) && doc != null) {
				sb.append("				\"").append(name).append("\": { \"$ref\" : \"#/definitions/").append(model)
						.append(".").append(name).append("\"},\n");
			}
		}
		sb.append("			}\n");
		sb.append("		},\n");

		return sb.toString();
	}

	public static void printModelObj(Class<?> clazz) {
		System.out.println(getModelObj(clazz));
	}

	/**
	 * 返回用的doc
	 * @param model
	 * @param name
	 * @param desc
	 * @param defaultVal
	 * @param type
	 * @param format
	 * @return
	 */
	public static String getDefine(String model, String name, String desc, Object defaultVal, String type,
                                   String format) {
		StringBuilder sb = new StringBuilder("\"").append(name).append("\":");

		return getDefineString(model, name, desc, defaultVal, type, format, sb,true);

	}

	/**
	 * 请求用的生成文档,没有:号的
	 * @param model
	 * @param name
	 * @param desc
	 * @param defaultVal
	 * @param type
	 * @param format
	 * @return
	 */
	public static String getDefine4Param(String model, String name, String desc, Object defaultVal, String type,
                                         String format, boolean allowNull) {
		StringBuilder sb = new StringBuilder("");

		return getDefineString(model, name, desc, defaultVal, type, format, sb,allowNull);

	}

	private static String getDefineString(String model, String name, String desc, Object defaultVal, String type, String format, StringBuilder sb, boolean allowNull) {
		if (format == null) {

			sb.append("{\n")
					.append("\"in\":").append("\"formData\",\n")
					.append("\"name\":").append("\"").append(name).append("\",\n")
					.append("\"description\":").append("\"").append(desc).append("\",\n");
			if(!allowNull){
				sb.append("\"required\": \"true\",\n");
			}
			sb.append("\"default\":");

			//.append("\"required\": ").append("true").append("\",\n")

			// sb.append(defaultVal);
			if (defaultVal instanceof Double) {
				sb.append((Double) defaultVal);
			} else if (defaultVal instanceof Float) {
				sb.append((Float) defaultVal);
			} else if (defaultVal instanceof Integer) {
				sb.append((Integer) defaultVal);
			} else if (defaultVal instanceof Long) {
				sb.append((Long) defaultVal);
			} else {
				sb.append("\"").append((String) defaultVal).append("\"");
			}

			sb.append(",\n");
			sb.append("			\"type\":").append("\"").append(type).append("\"\n").append("		}");
			return sb.toString();
		} else {
			sb.append("{\n")
					.append("\"in\":").append("\"formData\",\n")
					.append("\"name\":").append("\"").append(name).append("\",\n")
					.append("\"description\":").append("\"").append(desc).append("\",\n")
					.append("\"default\":");
			// sb.append(defaultVal);
			if (defaultVal instanceof Double) {
				sb.append((Double) defaultVal);
			} else if (defaultVal instanceof Float) {
				sb.append((Float) defaultVal);
			} else if (defaultVal instanceof Integer) {
				sb.append((Integer) defaultVal);
			} else if (defaultVal instanceof Long) {
				sb.append((Long) defaultVal);
			} else {
				sb.append("\"").append((String) defaultVal).append("\"");
			}
			sb.append(",\n")
					.append("\"type\":").append("\"").append(type).append("\",\n")
					.append("\"format\":").append("\"").append(format).append("\"\n")
					.append("}");
			return sb.toString();
		}
	}



	/**
	 * 根据class获取请求的文档(就是不带:的)
	 * @param clazz
	 * @return
	 */
	public static String getModelDefine4Parameter(Class<?> clazz) {
		String className = clazz.getSimpleName();
		char chBegin = Character.toLowerCase(className.charAt(0));
		String model = new StringBuilder().append(chBegin).append(className.substring(1)).toString();

		StringBuilder sb = new StringBuilder();

		Field[] fs = clazz.getDeclaredFields();

		for (Field f : fs) {
			int modifier = f.getModifiers();
			String desc = "";
			String def = "";
			boolean allowNull = true;
			JsonDoc doc = f.getAnnotation(JsonDoc.class);
			// 没有注解认为需要出现在JsonDoc中
			if (doc == null) {
				continue;
			}else {
				desc = doc.description();
				def = doc.def();
				allowNull = doc.allowNull();
			}
			String name = f.getName();
			if (!Modifier.isStatic(modifier)) {
				Class<?> type = f.getType();
				String strTypeName = f.getType().getName();
				try {
					//如果是第一个不加逗号,其他都加逗号
					if(!sb.toString().endsWith(",")&&sb.toString().endsWith("}")){
						sb.append(",");
					}
					if (strTypeName.endsWith("String")) {
						if (StringUtils.isBlank(def)) {
							def = name;
						}
						sb.append(getDefine4Param(model, name, desc, def, "string", null,allowNull));
					} else if (strTypeName.endsWith("Double")) {
						double v = 1.00d;
						if (StringUtils.isNotBlank(def)) {
							v = Double.parseDouble(def);
						}
						sb.append(getDefine4Param(model, name, desc, v, "number", "float",allowNull));
					} else if (strTypeName.endsWith("Float")) {
						double v = 1.00f;
						if (StringUtils.isNotBlank(def)) {
							v = Float.parseFloat(def);
						}
						sb.append(getDefine4Param(model, name, desc, v, "number", "float",allowNull));
					} else if (strTypeName.endsWith("Date")) {
						long v = 1461918485;
						if (StringUtils.isNotBlank(def)) {
							v = DateHelper.parseToSimpleDateTime(def).getTime();
						}
						sb.append(getDefine4Param(model, name, desc, v, "integer", "int64",allowNull));
					} else if (strTypeName.endsWith("Integer")) {
						Integer v = 2;
						if (StringUtils.isNotBlank(def)) {
							v = Integer.valueOf(def);
						}
						sb.append(getDefine4Param(model, name, desc, v, "integer", "int32",allowNull));
					} else if (strTypeName.endsWith("Long")) {
						Long v = 2l;
						if (StringUtils.isNotBlank(def)) {
							v = Long.valueOf(def);
						}
						sb.append(getDefine4Param(model, name, desc, v, "integer", "int64",allowNull));
					}else if(strTypeName.endsWith("List")){

						Type gt = f.getGenericType();//得到泛型类型
						ParameterizedType pt = (ParameterizedType)gt;
						Class lll = (Class)pt.getActualTypeArguments()[0];
						if(lll.getName().endsWith("String")){
							sb.append(getDefine4Param(model, name, desc, def, "string", null,allowNull));
						}else {
							sb.append("{\n" +
									"  \"name\": \""+name+"\",\n" +
									"  \"in\": \"body\",\n" +
									"  \"description\": \"参数\",\n" +
									"  \"required\": true,\n" +
									"  \"schema\": {\n" +
									"    \"type\": \"object\",\n" +
									"    \"properties\": {");
							sb.append(lineEnd);
							sb.append(getModelDefine(lll,null,true));
							sb.append(lineEnd);
							sb.append("}\n" +
									"}");
							sb.append("}");
						}


					}else{  // object
						sb.append(printObj(type,name));
					}
				} catch (Exception e) {

				}
			}
		}
		return sb.toString();
	}


	public static void main(String[] args) {
		// System.out.println(getDefine("order", "order_number", "订单标号",
		// "12345", "number"));

//		String modelDefine = writeParameters(SearchOrderParam.class);
//		System.out.println(modelDefine);
//		printModelObj(InReposityBakBo.class);
		//OrderForReplaceReturnVo.Color4ReplaceReturnVo a = new OrderForReplaceReturnVo.Color4ReplaceReturnVo();
		//printAllYouWant(a.getClass());
/*		ArrayList<String> strings = new ArrayList<>();
		strings.add("app/v1/order");
		String s = writeDoc("/v1/buyfollow/OrderProcess4app/editJbOrder123.do", strings, "操作id", "描述", "描述", SearchOrderParam.class, InReposityBakBo.class,null,"true");
		System.out.println(s);*/



//		System.out.println(writeParametersWithGet(SearchOrderParam.class));
	}

}
