package com.soouya.common.util;

import javacommon.base.exception.BusinessException;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.processors.PropertyNameProcessor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.*;

/**
 * json数据转换工具类
 *
 * @author soouya
 *
 */
public class JsonConfigUtil {

	/**
	 * 过滤json key 与 修改key值
	 *
	 * @param keys
	 * @param map<preName,newName>
	 * @return
	 */
	@Deprecated
	@SuppressWarnings("rawtypes")
	public static JsonConfig getJsonConfig(Class clazz, String[] keys, final Map<String, String> map) {
		JsonConfig jsonConfig = new JsonConfig();
		// 过滤不需要的key

		List<String> excludeKey = new ArrayList<String>();
		if(keys != null){
			excludeKey.addAll(Arrays.asList(keys));
		}

		excludeKey.add("null");

		String[] stringKeys = new String[excludeKey.size()];
		jsonConfig.setExcludes(excludeKey.toArray(stringKeys));
		// 时间修改为时间戳
		convertTime(jsonConfig);
		// 修改key值
		convertKeys(jsonConfig, clazz, map);
		return jsonConfig;
	}

	private static List<Method> getMethodList(Class clazz){
		List<Method> lst = new ArrayList<Method>();
		while(clazz != Object.class){
			Method[] methodList = clazz.getDeclaredMethods();
			lst.addAll(Arrays.asList(methodList));

			clazz = clazz.getSuperclass();
		}
		return lst;
	}

	/**
	 *
	 * @param clazz
	 * @param includeKey
	 * @param map
	 * @return
	 */
	public static JsonConfig newIncludeJsonConfig(Class clazz, String[] includeKey, final Map<String, String> map) {
		HashSet<String> includeSet = new HashSet<String>(Arrays.asList(includeKey));
		List<Method> methodList = getMethodList(clazz);

		List<String> excludeKeyList = new ArrayList<String>();
		for(Method method : methodList){
			int modifier = method.getModifiers();
			if(Modifier.isPublic(modifier) && !Modifier.isStatic(modifier)){
				String name = method.getName();
				if(name.startsWith("get")){
					String filedName = Character.toLowerCase(new Character(name.charAt(3))) + name.substring(4);
					if(!includeSet.contains(filedName)){
						excludeKeyList.add(filedName);
					}
				}else if(name.startsWith("is")){
					String filedName = Character.toLowerCase(new Character(name.charAt(2))) + name.substring(3);
					if(!includeSet.contains(filedName)){
						excludeKeyList.add(filedName);
					}
				}
			}
		}


		JsonConfig jsonConfig = new JsonConfig();
		// 过滤不需要的key
		String[]  excludeKey = new String[excludeKeyList.size()];
		excludeKeyList.toArray(excludeKey);
		jsonConfig.setExcludes(excludeKey);

		// 时间修改为时间戳
		convertTime(jsonConfig);
		// 修改key值
		convertKeys(jsonConfig, clazz, map);
		return jsonConfig;
	}

	/**
	 * 修改key值
	 *
	 * @param map<preName,newName>
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static void convertKeys(JsonConfig jsonConfig, Class clazz, final Map<String, String> map) {
		if (map == null || map.isEmpty()) {
			return;
		}
		// 更新key的名称
		PropertyNameProcessor namepro = new PropertyNameProcessor() {
			@Override
			public String processPropertyName(Class clazz, String name) {
				if (map != null && map.containsKey(name)) {
					name = map.get(name).toString();
				}
				return name;
			}
		};
		jsonConfig.registerJsonPropertyNameProcessor(clazz, namepro);
	}

	/**
	 * 时间转换器,返回时间戳
	 *
	 * @param jsonConfig
	 */
	public static void convertTime(JsonConfig jsonConfig) {
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			@Override
			public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
				return this.process(value);
			}

			@Override
			public Object processArrayValue(Object value, JsonConfig arg1) {
				return this.process(value);
			}

			// 处理Date 类型,返回时间戳
			private Object process(Object value) {
				try {
					if (value == null) {
						return 0;
					} else if (value instanceof Date)
						return ((Date) value).getTime();
					else {
						return 0;
					}
				} catch (Exception e) {
					throw new BusinessException(Code.FATAL, "时间转换为时间戳失败");
				}
			}
		});
	}


	/**
	 * money转换器,分->元
	 *
	 * @param jsonConfig
	 * @param keys
	 */
	public static void convertCentToYuan(JsonConfig jsonConfig, final String[] keys) {
		final List keyList = Arrays.asList(keys);
		jsonConfig.registerJsonValueProcessor(Integer.class, new JsonValueProcessor() {
			@Override
			public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
				if (keyList.contains(key)) {
					return this.process(value);
				}
				return value;
			}

			@Override
			public Object processArrayValue(Object value, JsonConfig arg1) {
				return this.process(value);
			}

			// 分->元
			private Object process(Object value) {
				try {
					if (value == null) {
						return 0d;
					}
					if (!(value instanceof Integer)) {
						return 0d;
					}
					Integer intValue = (Integer) value;
					return MoneyUtil.getRmbYuan(intValue);
				} catch (Exception e) {
					throw new BusinessException(Code.FATAL, "金额转换为元失败");
				}
			}
		});
		jsonConfig.registerJsonValueProcessor(Long.class, new JsonValueProcessor() {
			@Override
			public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
				if (keyList.contains(key)) {
					return this.process(value);
				}
				return value;
			}

			@Override
			public Object processArrayValue(Object value, JsonConfig arg1) {
				return this.process(value);
			}

			// 分->元
			private Object process(Object value) {
				try {
					if (value == null) {
						return 0d;
					}
					if (!(value instanceof Long)) {
						return 0d;
					}
					Long intValue = (Long) value;
					return MoneyUtil.getRmbYuan(intValue);
				} catch (Exception e) {
					throw new BusinessException(Code.FATAL, "金额转换为元失败");
				}
			}
		});
	}

	/**
	 * 截取银行卡号中间位数替换为*******，保留前两位和后4位, 如44992920111838338 -> 44***********8338
	 *
	 * @param jsonConfig
	 * @param keys
	 */
	public static void convertToxxx(JsonConfig jsonConfig, final String[] keys) {
		final List keyList = Arrays.asList(keys);
		jsonConfig.registerJsonValueProcessor(String.class, new JsonValueProcessor() {
			@Override
			public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
				if (keyList.contains(key) && value instanceof String) {
					return this.process(value);
				}
				return value;
			}

			@Override
			public Object processArrayValue(Object value, JsonConfig arg1) {
				return this.process(value);
			}

			private Object process(Object value) {
				try {
					if (value == null) {
						return String.valueOf("");
					}
					String bankAccount = String.valueOf(value);
					if (bankAccount.length() <= 10) {
						return "**** **** ****";
					}
					return "**** **** **** " + bankAccount.substring(bankAccount.length() - 4);
				} catch (Exception e) {
					throw new BusinessException(Code.FATAL, "银行账号转换为***失败");
				}
			}
		});
	}

	/**
	 * 数字，如果是空，则返回某个数字
	 *
	 * @param jsonConfig
	 */
	public static void convertToOtherNumber(JsonConfig jsonConfig, final String[] keys, final Object targetNumber) {
		final List keyList = Arrays.asList(keys);
		jsonConfig.registerJsonValueProcessor(BigDecimal.class, new JsonValueProcessor() {
			@Override
			public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
				if (keyList.contains(key)) {
					if (value == null) {
						return targetNumber;
					}
					return this.process(value);
				}
				return value;
			}

			@Override
			public Object processArrayValue(Object value, JsonConfig arg1) {
				return this.process(value);
			}

			private Object process(Object value) {
				try {
					if (value == null) {
						return targetNumber;
					}
					return value;
				} catch (Exception e) {
					throw new BusinessException(Code.FATAL, "空数字转换失败");
				}
			}
		});
	}
}
