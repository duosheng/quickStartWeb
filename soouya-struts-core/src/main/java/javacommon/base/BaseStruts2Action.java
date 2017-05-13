package javacommon.base;

import com.opensymphony.xwork2.ActionSupport;
import com.soouya.common.model.PageRequest;
import com.soouya.common.util.Code;
import com.soouya.common.util.DateHelper;
import com.soouya.common.util.ErrorUtil;
import com.soouya.common.util.ObjectUtils;
import javacommon.util.ConvertRegisterHelper;
import javacommon.util.JsonReturnUtil;
import javacommon.util.PageRequestFactory;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BaseStruts2Action extends ActionSupport implements RequestAware {
	private static final long serialVersionUID = 7830783958569011760L;
	protected Map requestMap = null;
	private IBaseManager manager;
	private BaseEntity entity;
	private BaseQuery query;
	private String id = null;
	private String ids = null;
	protected static final int MAXIDS = 30;
	protected static Log log = LogFactory.getLog(BaseStruts2Action.class);



	protected String checkImgCode(boolean removeSession) {
		String code = (String) getSession().getAttribute("imgCode");
		String imgCode = getRequest().getParameter("imgCode");
		if (StringUtils.isBlank(imgCode) || StringUtils.isBlank(code) || !imgCode.equals(code)) {
			return "20013";
		} else {
			if (removeSession) {
				getSession().removeAttribute("imgCode");
			}
			return null;
		}
	}

	protected void export(List<Map> listMap) throws Exception {
		Class clazz = getEntity().getClass();
		String name = clazz.getSimpleName();

		getResponse().reset();
		getResponse().setHeader("Content-disposition",
				"attachment;filename=" + name + "_" + DateHelper.getSimpleDate(new Date(), "yyyyMMdd") + ".xls");
		getResponse().setContentType("application/vnd.ms-excel;charset=GB2312");
		OutputStream os = getResponse().getOutputStream();
		WritableWorkbook wwb = Workbook.createWorkbook(os);
		WritableSheet ws = wwb.createSheet(name, 0);
		if (listMap != null && listMap.size() > 0) {
			String[] columnAlias;
			Set keys = null;
			keys = listMap.get(0).keySet();
			if (keys != null && keys.size() > 0) {
				columnAlias = new String[keys.size()];
				int i = 0;
				for (Object key : keys) {
					columnAlias[i] = key.toString();
					Label label = new Label(i, 0, columnAlias[i]);
					ws.addCell(label);
					i++;
				}

				for (i = 0; i < listMap.size(); i++) {
					int j = 0;
					for (Object field : keys) {
						Label label = new Label(j, i + 1, String.valueOf(listMap.get(i).get(field)));
						ws.addCell(label);
						j++;
					}
				}
			}
		}
		wwb.write();
		wwb.close();
	}

	static {
		// 注册converters
		ConvertRegisterHelper.registerConverters();
	}

	@Override
	public void setRequest(Map request) {
		requestMap = request;
	}

	public <T extends PageRequest> T newQuery(Class<T> queryClazz, String defaultSortColumns) {
		PageRequest query = PageRequestFactory.bindPageRequest(
				org.springframework.beans.BeanUtils.instantiateClass(queryClazz), getRequest(),
				defaultSortColumns);
		return (T) query;
	}
	
	public <T extends PageRequest> T newQueryForOngl(Class<T> queryClazz, String defaultSortColumns,boolean flag) {
		PageRequest query = PageRequestFactory.bindPageRequest(
				org.springframework.beans.BeanUtils.instantiateClass(queryClazz), getRequest(),
				defaultSortColumns, flag);
		return (T) query;
	}

	/**
	 * 添加、编辑、删除
	 */
	public void saveOrUpdate() {
		String oper = getRequest().getParameter("oper");
		try {
			if (oper != null && oper.equals("del")) {
				getManager().deleteById(getRequest().getParameter("id"));
			} else {
				getManager().saveOrUpdate(getEntity());
			}
			JSONObject resJson = new JSONObject();
			accumulateCodeAndMsg(resJson, Code.SUCCESS, getRequest());
			returnJsonP(resJson.toString(), getRequest(), getResponse());
			return;
		} catch (Exception e) {
			ErrorUtil.errorHandle(getResponse(), e);
			return;
		}
	}







	public HttpServletRequest getRequest() {
		HttpServletRequest request = ServletActionContext.getRequest();
		return request;
	}

	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	public IBaseManager getManager() {
		return manager;
	}

	public void setManager(IBaseManager manager) {
		this.manager = manager;
	}

	public BaseEntity getEntity() {
		return entity;
	}

	public void setEntity(BaseEntity entity) {
		this.entity = entity;
	}

	public BaseQuery getQuery() {
		return query;
	}

	public void setQuery(BaseQuery query) {
		this.query = query;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public HttpSession getSession() {
		return getRequest().getSession(false);
	}

	protected boolean isNullOrEmptyString(Object o) {
		return ObjectUtils.isNullOrEmptyString(o);
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////


	/**
	 * msg国际化
	 *
	 * @param request
	 * @param code
	 * @return
	 */
	public static String getMsg(HttpServletRequest request, String code) {
		return JsonReturnUtil.getMsg(request,code);
	}

	/**
	 * 如果含有jsonp参数，返回jsonP,否则返回普通json
	 *
	 * @param jsonStr
	 * @param request
	 * @param response
	 */
	public static void returnJsonP(String jsonStr, HttpServletRequest request, HttpServletResponse response) {
		JsonReturnUtil.returnJsonP(jsonStr, request, response);
	}

	/**
	 *
	 * @param jsonStr
	 *            要返回的json对象
	 * @param response
	 */
	public static void returnJson(String jsonStr, HttpServletResponse response) {
		JsonReturnUtil.returnJson(jsonStr, response);
	}
	/**
	 * @param vo 要返回的json对象
	 */
	public static void returnJson(BaseVo vo) {
		HttpServletResponse response1 = ServletActionContext.getResponse();
		HttpServletRequest request = ServletActionContext.getRequest();
		String code = "1"; //SUCCESS
		vo.setSuccess(code);
		vo.setMsg(getMsg(request, code));
		JsonReturnUtil.returnJson(JSONObject.fromObject(vo).toString(), response1);
	}

	/**
	 * 要返回的分页json对象
	 */
	public static void returnPageJson(com.soouya.common.model.Page page, List list) {
		PageVo pageVo = new PageVo<>();
		Page returnPage = new Page<>();
		returnPage.setResult(list);
		returnPage.setPageNumber(page.getPageNumber());
		returnPage.setPageSize(page.getPageSize());
		returnPage.setTotalCount(page.getTotalCount());
		pageVo.setPage(returnPage);
		returnJson(pageVo);
	}

	/**
	 * 组装msg和code到json
	 *
	 * @param resJson
	 * @param code
	 */
	public static void accumulateCodeAndMsg(JSONObject resJson, String code, HttpServletRequest request) {
		JsonReturnUtil.accumulateCodeAndMsg(resJson, code, request);
	}

	/**
	 * 组装msg和code到json
	 */
	public static void accumulateCodeAndMsg(BaseVo vo,String code, HttpServletRequest request) {
		vo.setSuccess(code);
		vo.setMsg(getMsg(request, code));
	}

	/**
	 * 组装msg和code到json
	 *
	 * @param resJson
	 * @param code
	 * @param msg
	 * @param request
	 */
	public static void accumulateCodeAndMsg(JSONObject resJson, String code, String msg, HttpServletRequest request) {
		JsonReturnUtil.accumulateCodeAndMsg(resJson,code,msg,request);
	}

	/**
	 * msg 中文
	 *
	 * @param code
	 * @return
	 */
	public static String getCnMsg(String code) {
		return JsonReturnUtil.getCnMsg(code);
	}

	public void returnErrorJsonP(HttpServletRequest request, HttpServletResponse response, String errCode){
		JsonReturnUtil.returnErrorJsonP(request,response,errCode);
	}


}
