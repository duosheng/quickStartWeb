package javacommon.util;

import com.soouya.common.model.PageRequest;
import javacommon.base.BaseQuery;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 用于分页组件覆盖的类,新的分页组件覆盖此类的bindPageRequest()方法以适合不同的分页创建
 *
 * @author badqiu
 */
public class PageRequestFactory {
    public static final int MAX_PAGE_SIZE = 10000;

    static BeanUtilsBean beanUtils = new BeanUtilsBean();

    static {
        // 用于注册日期类型的转换
        String[] datePatterns = new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS",
                "HH:mm:ss" };
        ConvertRegisterHelper.registerConverters(beanUtils.getConvertUtils(), datePatterns);

        System.out.println("PageRequestFactory.MAX_PAGE_SIZE=" + MAX_PAGE_SIZE);
    }

    public static PageRequest bindPageRequest(PageRequest pageRequest, HttpServletRequest request) {
        return bindPageRequest(pageRequest, request, null);
    }

    public static PageRequest bindPageRequest(PageRequest pageRequest, HttpServletRequest request, String defaultSortColumns) {
        return bindPageRequest(pageRequest, request, defaultSortColumns, BaseQuery.DEFAULT_PAGE_SIZE);
    }
    
    public static PageRequest bindPageRequest(PageRequest pageRequest, HttpServletRequest request, String defaultSortColumns,boolean flag) {
    	return bindPageRequest(pageRequest, request, defaultSortColumns, BaseQuery.DEFAULT_PAGE_SIZE,flag);
    }

    /**
     * 绑定PageRequest的属性值
     * TODO 注意1
     */
    public static PageRequest bindPageRequest(PageRequest pageRequest, HttpServletRequest request, String defaultSortColumns, int defaultPageSize) {
        try {
            Map<String,Object> params = WebUtils.getParametersStartingWith(request, "");
            for (String key : params.keySet()) {
            	if (key.indexOf(".")>=0) {
            		try {
            			Ognl.setValue(key, pageRequest, params.get(key));
            		} catch (OgnlException e) {
            			//暂时不做处理
            		}
            		//params.remove(key);
            	}
            }
            beanUtils.copyProperties(pageRequest, params);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("beanUtils.copyProperties() error", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("beanUtils.copyProperties() error", e.getTargetException());
        }

        pageRequest.setPageNumber(ServletRequestUtils.getIntParameter(request, "pageNumber", 1));
        pageRequest.setPageSize(ServletRequestUtils.getIntParameter(request, "pageSize", defaultPageSize));
        pageRequest.setSortColumns(ServletRequestUtils.getStringParameter(request, "sortColumns", defaultSortColumns));

        if (pageRequest.getPageSize() > MAX_PAGE_SIZE) {
            pageRequest.setPageSize(MAX_PAGE_SIZE);
        }
        return pageRequest;
    }
    /**
     * 绑定PageRequest的属性值
     * flag  是否使用ognl注入
     */
    public static PageRequest bindPageRequest(PageRequest pageRequest, HttpServletRequest request, String defaultSortColumns, int defaultPageSize,boolean flag) {
    	try {
    		Map<String,Object> params = WebUtils.getParametersStartingWith(request, "");
    		if (flag) {
    			for (String key : params.keySet()) {
    				if (key.indexOf(".")>=0) {
    					try {
    						Ognl.setValue(key, pageRequest, params.get(key));
    					} catch (OgnlException e) {
    						// TODO Auto-generated catch block
    						throw new IllegalArgumentException("Ognl.setValue 出错,key:"+key,e);
    						
    					}
    					//params.remove(key);
    				}
    			}
			}
    		beanUtils.copyProperties(pageRequest, params);
    	} catch (IllegalAccessException e) {
    		throw new IllegalArgumentException("beanUtils.copyProperties() error", e);
    	} catch (InvocationTargetException e) {
    		throw new IllegalArgumentException("beanUtils.copyProperties() error", e.getTargetException());
    	}
    	
    	pageRequest.setPageNumber(ServletRequestUtils.getIntParameter(request, "pageNumber", 1));
    	pageRequest.setPageSize(ServletRequestUtils.getIntParameter(request, "pageSize", defaultPageSize));
    	pageRequest.setSortColumns(ServletRequestUtils.getStringParameter(request, "sortColumns", defaultSortColumns));
    	
    	if (pageRequest.getPageSize() > MAX_PAGE_SIZE) {
    		pageRequest.setPageSize(MAX_PAGE_SIZE);
    	}
    	return pageRequest;
    }

    /**
     * 接收参数，组装到bean中
     *
     * @param param
     * @param request
     */
    public static <T extends Object> void bindParam(T param, HttpServletRequest request) {
        BeanUtilsBean beanUtils = new BeanUtilsBean();
        Map params = WebUtils.getParametersStartingWith(request, "");
        try {
            beanUtils.copyProperties(param, params);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}
