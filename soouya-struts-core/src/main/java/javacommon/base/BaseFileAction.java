package javacommon.base;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.soouya.common.exception.BusinessException;
import com.soouya.common.util.Code;
import com.soouya.common.util.DateHelper;
import com.soouya.common.util.ErrorUtil;
import com.soouya.common.util.ReadWriteFile;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.sanselan.ImageInfo;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.IImageMetadata;
import org.apache.sanselan.common.ImageMetadata;
import org.apache.struts2.ServletActionContext;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

public abstract class BaseFileAction extends BaseStruts2Action {
	private static final long serialVersionUID = 7830783958569011760L;

	protected Map requestMap = null;
	private List<File> file;
	private List<String> fileContentType;
	private List<String> fileFileName;
	private List<String> field;
	private List<String> link;


	private String OSS_ENDPOINT = "http://oss-cn-shenzhen-internal.aliyuncs.com";
	private String ACCESS_ID = "p8f3efSgaPrh2MJ1";
	private String ACCESS_KEY = "S1rexMBrXHQ7i6RC1gJrwZNUIWsMI4";
	private String BUCKET = "soouya-upload";

	public void setACCESS_ID(String ACCESS_ID) {
		this.ACCESS_ID = ACCESS_ID;
	}

	public void setACCESS_KEY(String ACCESS_KEY) {
		this.ACCESS_KEY = ACCESS_KEY;
	}

	public void setBUCKET(String BUCKET) {
		this.BUCKET = BUCKET;
	}

	public void setOSS_ENDPOINT(String OSS_ENDPOINT) {
		this.OSS_ENDPOINT = OSS_ENDPOINT;
	}

	public String getBUCKET() {
		return BUCKET;
	}

	public String getOSS_ENDPOINT() {
		return OSS_ENDPOINT;
	}

	public String getACCESS_ID() {
		return ACCESS_ID;
	}

	public String getACCESS_KEY() {
		return ACCESS_KEY;
	}

	/**
	 * 简单上传文件,并返回imgUrl
	 *
	 * @return json 字符串
	 */
	public void uploadFile(String folder) {
		try {
			String fileName = "";
			if (file != null && !file.isEmpty()) {
				String uuidParam = getRequest().getParameter("uuid");
				String uuid = null;
				if (isNullOrEmptyString(uuidParam)) {
					uuid = UUID.randomUUID().toString();
				} else {
					uuid = uuidParam;
				}

				File f = file.get(0);
				fileName = fileFileName.get(0);
				String contentType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
				if (contentType.contains("@")) {
					contentType = contentType.substring(0, contentType.indexOf("@"));
				}
				Set<String> set = new HashSet<String>();
				set.add("jpg");
				set.add("png");
				set.add("gif");
				set.add("tif");
				set.add("tiff");
				set.add("icon");
				if (!set.contains(contentType.toLowerCase())) {
					throw new BusinessException("文件不是图片格式");
				}

				fileName = "/upload/" + folder + "/" + uuid + "." + contentType;

				upload2oss(f, fileName);
			}

			JSONObject resJson = new JSONObject();

			accumulateCodeAndMsg(resJson, Code.SUCCESS, getRequest());
			resJson.accumulate("imgUrl", fileName);
			returnJsonP(resJson.toString(), getRequest(), getResponse());

		} catch (Exception e) {
			ErrorUtil.errorHandle(getResponse(), e);
		}
	}


	/**
	 * 简单上传文件,并返回imgUrl
	 *
	 * @return json 字符串
	 */
	public void uploadFileReturnLength(String folder) {
		try {
			String fileName = "";
			Map<String,Integer> map = new HashMap<String,Integer>();
			if (file != null && !file.isEmpty()) {
				String uuidParam = getRequest().getParameter("uuid");
				String uuid = null;
				if (isNullOrEmptyString(uuidParam)) {
					uuid = UUID.randomUUID().toString();
				} else {
					uuid = uuidParam;
				}

				File f = file.get(0);
				fileName = fileFileName.get(0);
				String contentType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
				if (contentType.contains("@")) {
					contentType = contentType.substring(0, contentType.indexOf("@"));
				}
				fileName = "/upload/" + folder + "/" + uuid + "." + contentType;
				//上传文件
				upload2oss(f, fileName);
				//获取长宽
				map = readOssImg(fileName);
			}

			JSONObject resJson = new JSONObject();

			accumulateCodeAndMsg(resJson, Code.SUCCESS, getRequest());
			resJson.accumulate("width", map.get("width"));
			resJson.accumulate("height", map.get("height"));
			resJson.accumulate("imgUrl", fileName);
			returnJsonP(resJson.toString(), getRequest(), getResponse());

		} catch (Exception e) {
			ErrorUtil.errorHandle(getResponse(), e);
		}
	}


	public Map readOssImg(String fileName) {
		Map map = new HashMap();
		try {
			OSSClient client = new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY);
			OSSObject object = client.getObject(BUCKET, fileName.substring(1));
			InputStream objectContent = object.getObjectContent();
			File file = File.createTempFile(fileName.substring(0, fileName.indexOf(".")), fileName.substring(fileName.indexOf(".")));
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = objectContent.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			objectContent.close();

			ImageInfo imageInfo = Sanselan.getImageInfo(file);
			IImageMetadata meta = Sanselan.getMetadata(file);
			boolean rotate = false;
			if (meta != null) {
				ArrayList list = meta.getItems();
				for (Object obj : list) {
					ImageMetadata.Item item = (ImageMetadata.Item) obj;
					if (item.getKeyword().equals("Orientation")) {
						int orientation = Integer.parseInt(item.getText());
						if (orientation >= 5) {
							rotate = true;
						}
					}
				}
			}
			int width = imageInfo.getWidth();
			int height = imageInfo.getHeight();
			map.put("rotate", rotate);
			if (rotate) {
				map.put("width", height);
				map.put("height", width);
			} else {
				map.put("width", width);
				map.put("height", height);
			}
			file.delete();
		} catch (Exception e) {
			// ErrorUtil.errorHandle(e);
			map.put("width", 319);
			map.put("height", 319);
		}
		return map;
	}
/*
	//简单上传到阿里云oss
	protected void upload2oss(File file, String fileName) {
		try {
			OSSClient client = new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY);
			InputStream content = new FileInputStream(file);
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(file.length());
			client.putObject(BUCKET, fileName.substring(1), content, meta);
			content.close();
		} catch (Exception e) {
			log.error("aliyun exception-------", e);
			e.printStackTrace();
			throw new BusinessException(Code.FATAL, "服务器错误");
		}
	}
*/
	//简单上传到阿里云oss
	protected void upload2oss(File file, String fileName) {
		try {
			OSSClient client = new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY);
			InputStream content = new FileInputStream(file);
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentLength(file.length());
			client.putObject(BUCKET, fileName.substring(1), content, meta);
			content.close();
		} catch (Exception e) {
			log.error("aliyun exception-------", e);
			e.printStackTrace();
			throw new BusinessException(Code.FATAL, "服务器错误");
		}
	}

	/**
	 * base upload
	 *
	 * @param fileName
	 * @param obj
	 * @param column
	 * @return
	 */
	protected boolean uploadFile(File file, String fileName, Object obj, String column) {
		boolean success = true;
		try {
			/* upload to local 上传到本地 */
//			FileInputStream fis = new FileInputStream(file);
//			File fs = new File(fileName);
//			FileOutputStream fos = new FileOutputStream(fs);
//			int len = 0;
//			byte[] buffer = new byte[1024];
//
//			while ((len = fis.read(buffer)) != -1) {
//				fos.write(buffer, 0, len);
//			}
//			fos.flush();
//			fos.close();
//			fis.close();

			/* upload to oss */
			upload2oss(file, fileName);
			PropertyDescriptor pd = new PropertyDescriptor(column, obj.getClass());
			Method getMethod = pd.getReadMethod();
			Object val = getMethod.invoke(obj);
			String value = "";
			if (val == null || val.toString().equals("")) {
				value = fileName;
			} else {
				value = val.toString() + "," + fileName;
				String[] values = value.toString().split(",");
				if (values.length > 10) {
					throw new BusinessException(Code.INVALID_PARAM, "图片个数不能超过10个");
				}
			}
			Method setMethod = pd.getWriteMethod();
			setMethod.invoke(obj, value);

		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("aliyun exception-------", e);
			e.printStackTrace();
			throw new BusinessException(Code.FATAL, "服务器错误");
		}
		return success;
	}

	/**
	 * 上传文件,并将url注入到obj中
	 * @param folder
	 * @param obj
	 * @return
	 */
	protected int uploadFiles(String folder, Object obj) {
		int num = 0;
		int fileSize = file == null ? 0 : file.size();
		int linkSize = link == null ? 0 : link.size();
		int fieldSize = field == null ? 0 : field.size();
		if (fieldSize == 0) {
			throw new BusinessException(Code.INVALID_PARAM, "字段个数必须大于0个");
		}
		if (fileSize > 0 && linkSize > 0) {
			throw new BusinessException(Code.INVALID_PARAM, "图片个数必须大于0个");
		}
		if ((fileSize + linkSize) != fieldSize) {
			throw new BusinessException(Code.INVALID_PARAM, "字段个数必须等于图片个数");
		}
		if ((fileSize + linkSize) > 10) {
			throw new BusinessException(Code.INVALID_PARAM, "图片个数不能超过10个");
		}
		for (int i = 0; i < field.size(); i++) {
			String uuidParam = getRequest().getParameter("uuid");
			String uuid = null;
			if (isNullOrEmptyString(uuidParam)) {
				uuid = UUID.randomUUID().toString();
			} else {
				uuid = uuidParam;
			}

			String fileName = null;
			File f = null;
			if (file != null) {
				fileName = fileFileName.get(i);
				f = file.get(i);
			} else if (link != null) {
				fileName = link.get(i);
				ReadWriteFile.httpDownload(fileName, uuid);
				f = new File(uuid);
			}
			String contentType = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			if (contentType.contains("@")) {
				contentType = contentType.substring(0, contentType.indexOf("@"));
			}
			String column = field.get(i);
			fileName = "/upload/" + folder + "/" + uuid + "." + contentType;
			boolean success = uploadFile(f, fileName, obj, column);
			if (!success) {
				throw new BusinessException(Code.FATAL, "上传图片到云端出错了");
			}
		}
		num = field.size();
		return num;
	}

	public List<File> getFile() {
		return file;
	}

	public void setFile(List<File> file) {
		this.file = file;
	}

	public List<String> getFileContentType() {
		return fileContentType;
	}

	public void setFileContentType(List<String> fileContentType) {
		this.fileContentType = fileContentType;
	}

	public List<String> getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(List<String> fileFileName) {
		this.fileFileName = fileFileName;
	}

	public List<String> getLink() {
		return link;
	}

	public void setLink(List<String> link) {
		this.link = link;
	}

	public List<String> getField() {
		return field;
	}

	public void setField(List<String> field) {
		if (field != null && field.size() == 1) {
			String[] fields = field.get(0).split(",");
			for (int i = 0; i < fields.length; i++) {
				field.set(i, fields[i]);
			}
		}
		this.field = field;
	}

	/** 导出到excel */
	public static void exportToExcel(String fileName, List<Map<String, String>> dataList) {
		try {
			if (StringUtils.isEmpty(fileName)) {
				return;
			}
			if (dataList == null || dataList.isEmpty()) {
				return;
			}
			ServletActionContext.getResponse().reset();
			ServletActionContext.getResponse().setHeader("Content-disposition", "attachment;filename=" + fileName + "_"
					+ DateHelper.getSimpleDate(new Date(), "yyyyMMdd") + ".xls");
			ServletActionContext.getResponse().setContentType("application/vnd.ms-excel;charset=GB2312");
			OutputStream os = ServletActionContext.getResponse().getOutputStream();
			WritableWorkbook wwb = Workbook.createWorkbook(os);
			WritableSheet ws = wwb.createSheet(fileName, 0);
			if (dataList != null && dataList.size() > 0) {
				Set keys = dataList.get(0).keySet();
				if (keys != null && keys.size() > 0) {
					for (int i = 0; i < dataList.size(); i++) {
						int j = 0;
						for (Object field : keys) {
							Label label = new Label(j, i + 1, String.valueOf(dataList.get(i).get(field)));
							ws.addCell(label);
							j++;
						}
					}
				}
			}
			wwb.write();
			wwb.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(Code.FATAL, "导出失败");
		}
	}
}
