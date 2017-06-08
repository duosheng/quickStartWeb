package javacommon.util;

import javacommon.base.JsonDoc;
import javacommon.base.RegexType;
import javacommon.base.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by xuyuli on 2016/11/15.
 */
public class ValidateUtil {

    private static JsonDoc dv;

    //解析的入口
    public static void valid(Object object){
        if(object==null){
            throw new BusinessException("10005","param参数注入失败");
        }
        //获取object的类型
        Class<? extends Object> clazz=object.getClass();
        //获取该类型声明的成员
        Field[] fields=clazz.getDeclaredFields();
        //遍历属性
        for(Field field:fields){
            JsonDoc doc = field.getAnnotation(JsonDoc.class);
            if (doc == null) {
                continue;
            }

            //对于private私有化的成员变量，通过setAccessible来修改器访问权限
            field.setAccessible(true);
            validate(field,object);
            //重新设置会私有权限
            field.setAccessible(false);

        }
    }


    public static void validate(Field field,Object object){

        Object value = null;

        //获取对象的成员的注解信息
        dv=field.getAnnotation(JsonDoc.class);
        try {
            value=field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if(dv==null)return;
        String fieldName = field.getName();

        /*************注解解析工作开始******************/
        if(!dv.allowNull()){
            if(value==null|| StringUtils.isBlank(value.toString())){
                throw new BusinessException("10005",fieldName+"不能为空");
            }
        }
        int modifier = field.getModifiers();
        if (!Modifier.isStatic(modifier)) {
            Class<?> type = field.getType();
            String strTypeName = field.getType().getName();
            if (strTypeName.endsWith("String")) {
                if(value!=null){
                    String val = String.valueOf(value);
                    if(dv.size()!=0 && (dv.size() < val.length())){
                        throw new BusinessException("10005",fieldName+"的长度大于限定值("+dv.size()+")");
                    }
                }
                validateString(value, fieldName);
            } else if (strTypeName.endsWith("Double")||strTypeName.endsWith("double")) {

            } else if (strTypeName.endsWith("Float")) {

            } else if (strTypeName.endsWith("Date")) {

            } else if (strTypeName.endsWith("Integer")||strTypeName.endsWith("int")) {

            } else if (strTypeName.endsWith("Long")||strTypeName.endsWith("long")) {

            }else if(strTypeName.endsWith("List")){

            }else{  // object
                valid(object);
            }
        }

        /*************注解解析工作结束******************/
    }

    private static void validateString(Object value, String fieldName) {
        if(dv.regexType()!= RegexType.NONE){
            switch (dv.regexType()) {
                case NONE:
                    break;
                case SPECIALCHAR:
                    if(RegexUtils.hasSpecialChar(value.toString())){
                        throw new BusinessException("10005",fieldName+"不能含有特殊字符");
                    }
                    break;
                case CHINESE:
                    if(RegexUtils.isChinese2(value.toString())){
                        throw new BusinessException("10005",fieldName+"不能含有中文字符");
                    }
                    break;
                case EMAIL:
                    if(!RegexUtils.isEmail(value.toString())){
                        throw new BusinessException("10005",fieldName+"地址格式不正确");
                    }
                    break;
                case IP:
                    if(!RegexUtils.isIp(value.toString())){
                        throw new BusinessException("10005",fieldName+"地址格式不正确");
                    }
                    break;
                case NUMBER:
                    if(!RegexUtils.isNumber(value.toString())){
                        throw new BusinessException("10005",fieldName+"不是数字");
                    }
                    break;
                case PHONENUMBER:
                    if(!RegexUtils.isPhoneNumber(value.toString())){
                        throw new BusinessException("10005",fieldName+"不是数字");
                    }
                    break;
                default:
                    break;
            }
        }

        if(!dv.regexExpression().equals("")){
            if(value.toString().matches(dv.regexExpression())){
                throw new BusinessException("10005",fieldName+"格式不正确");
            }
        }
    }
}
