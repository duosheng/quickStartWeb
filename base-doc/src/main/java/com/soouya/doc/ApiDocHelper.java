package com.base.doc;

import javacommon.base.exception.BusinessException;
import javacommon.util.NullClass;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by xuyuli on 2016/10/31.
 *
 * 返回list 使用 vo = ListVo.class   page  传泛型
 * 返回page 直接 page  传泛型
 */
public class ApiDocHelper {

	private final static String lineEnd = "\n";

	//预处理    预想的功能: 使用classload的机制,替换TagsEnum类,骗过java虚拟机
	static {

	}

	public static void main(String[] args) {
		//"com.base.com.base.doc.TestController"
		//"/home/xuyuli/mock/web/com.base.doc/redwood-only-new.json"
		if (args.length < 2) {
			System.out.println("参数数量不对");
			return;
		}
		String className = args[0];
		String jsonPath = args[1];
		List<Class> clazzs = new ArrayList<>();
		try {
			if (className.indexOf("-")>0) {
				String[] split = className.split("-");
				for (String s : split) {
					if (StringUtils.isNotBlank(s)) {
						Class<?> aClass = Class.forName(s);
						clazzs.add(aClass);
					}
				}
			}else {
				Class<?> aClass = Class.forName(className);
				clazzs.add(aClass);
			}

		} catch (ClassNotFoundException e) {
			throw new BusinessException(e);
		}
		//读取json文件
		String jsonStr = readFile(jsonPath);
		JSONObject json = JSONObject.fromObject(jsonStr);
		//更改json
		for (Class clazz : clazzs) {
			changeJson(clazz,  json);
		}
		//再写入文件
		writeJson(jsonPath,json);

	}

	private static void changeJson(Class clazz, JSONObject json){
		if (clazz==null) {
			System.out.println("clazz为null,执行失败");
			return;
		}
		if (clazz == TagsEnum.class) {
			//更新tags标签
			String tagsJsonStr = writeTagsDoc();
			JSONArray jSONArray = JSONArray.fromObject(tagsJsonStr);
			json.put("tags",jSONArray);
		}else {
			// 更新controller文件,即接口
			String pathJsonStr = writeDoc(clazz);
			if (StringUtils.isBlank(pathJsonStr)) {
				return;
			}
			JSONObject paths = json.getJSONObject("paths");
			JSONObject jsonObject = JSONObject.fromObject(pathJsonStr);
			for (String key: (Set<String>)jsonObject.keySet()) {
				if(paths.containsKey(key)){
					paths.put(key,jsonObject.getJSONObject(key));
				}else {
					paths.accumulate(key,jsonObject.getJSONObject(key));
				}
			}
		}

	}


	private static String writeTagsDoc() {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(lineEnd);
		TagsEnum[] tags = TagsEnum.values();
		//构建tag数组字符串
		StringBuilder tagstr = new StringBuilder();
		for (TagsEnum tag : tags) {
			String name = tag.getName();
			String description = tag.getDescription();
			tagstr.append("{\n" +
					"      \"name\": \""+name+"\",\n" +
					"      \"description\": \""+description+"\"\n" +
					"    },");
		}
		//去掉最后一个逗号
		String substring = "";
		if (StringUtils.isNotBlank(tagstr.toString())) {
			substring = tagstr.toString().substring(0, tagstr.toString().length() - 1);
		}
		sb.append(substring);
		sb.append(" ]").append(lineEnd);
		return sb.toString();
	}

	/**
	 * @param clazz
	 *            生成文档的class
	 */
	private static String writeDoc(Class clazz) {

		String simpleName = clazz.getSimpleName();
		String name = clazz.getName();

		String basePath = "";
		String[] split = name.split("\\.");
		String modelName = split[2];
		String actionName = "";
		if(simpleName.endsWith("Action")){
			actionName = simpleName.substring(0, simpleName.length() - 6);
		}else {
			actionName = simpleName.substring(0, simpleName.length() - 10);
		}

		// actionName = xiaoxie(actionName);
		basePath = "/" + modelName + "/" + actionName + "/";
		StringBuilder sb = new StringBuilder();
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			String methodName = method.getName();
			ApiDoc doc = method.getAnnotation(ApiDoc.class);
			if (doc == null) {
				continue;
			}
			//获取参数类型
			Class param = doc.param();
			if (param == NullClass.class) {
				Class<?>[] parameterTypes = method.getParameterTypes();
				//TODO暂时只支持一个参数
				if (parameterTypes.length > 0) {
					param = parameterTypes[0];
				}else {
					param = NullClass.class;
				}

			}
			//获取返回值类型
			Class vo = doc.vo();
			if (vo == NullClass.class) {   //用户没有显性的写vo参数,取方法的默认返回
				vo = method.getReturnType();
			}
			//返回给前端的是page,其中的泛型参数需要手动传入
			Class page = doc.page();
			if (page == NullClass.class && doc.vo() == NullClass.class) {
				Type type = method.getGenericReturnType();// 获取返回值类型
				if (type instanceof ParameterizedType) { // 判断获取的类型是否是参数类型
					Type[] typesto = ((ParameterizedType) type).getActualTypeArguments();// 强制转型为带参数的泛型类型
					page = (Class) typesto[0];
				}
			}
			ApiDoc.Need_Page need_page = doc.needPage();
			boolean needPage = true;
			if(need_page == ApiDoc.Need_Page.NO){
				needPage = false;
			}
			String sunmmary = doc.desc();

			// 如果传的vo是null,取泛型的类型组装PageVo
//			if (vo.getName().endsWith("NullClass")) {
//				vo = PageVo.class;
//			}
			ApiDoc.Old_Style old_style = doc.oldStyle();
			boolean oldStyle = true;
			if(old_style == ApiDoc.Old_Style.NO){
				oldStyle = false;
			}
			String apiPath = "";
			if(simpleName.endsWith("Action")){
				apiPath = basePath + methodName + ".do";
			}else{
				apiPath = basePath + methodName;
			}
			String mock = "false";
			ApiDoc.Mock mock1 = doc.mock();
			if (mock1 == ApiDoc.Mock.YES) {
				mock = "true";
			}
			List<String> tagList = new ArrayList<>();
			TagsEnum[] tag = doc.tag();
			for (TagsEnum tagsEnum : tag) {
				tagList.add(tagsEnum.getName());
			}
			String s = JsonDocHelper.writeDoc(apiPath, tagList, actionName + "_" + methodName + "Id", sunmmary+"   "+apiPath, sunmmary, param,
					vo, page, mock,needPage,oldStyle);
			sb.append(s);
			// System.out.println(s);
			sb.append(",");

		}

		// 去掉最后一个逗号并打印
//		System.out.println(sb.toString().substring(0, sb.toString().length() - 1));
		String substring = "";
		if (StringUtils.isNotBlank(sb.toString())) {
			substring = "{"+sb.toString().substring(0, sb.toString().length() - 1)+"}";
		}


		return substring;

	}

	/**
	 * 首字母小写
	 *
	 * @param str
	 * @return
	 */
	public static String xiaoxie(String str) {
		char[] chars = new char[1];
		chars[0] = str.charAt(0);
		String temp = new String(chars);
		if (chars[0] >= 'A' && chars[0] <= 'Z') {
			return (str.replaceFirst(temp, temp.toLowerCase()));
		} else {
			return str;
		}
	}

	/**
	 * 读取文件
	 * @param Path
	 * @return
	 */
	public static String readFile(String Path){
		BufferedReader reader = null;
		String laststr = "";
		try{
			FileInputStream fileInputStream = new FileInputStream(Path);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while((tempString = reader.readLine()) != null){
				laststr += tempString;
			}
			reader.close();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return laststr;
	}

	//给定路径与Json文件，存储到硬盘
	public static void writeJson(String fullFileName, Object json){
		BufferedWriter writer = null;
		File file = new File(fullFileName);
		//如果文件不存在，则新建一个
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//写入
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(writer != null){
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//        System.out.println("文件写入成功！");
	}


}
