package com.dzs.projectframe.bean;

import com.dzs.projectframe.utils.FileUtils;

import java.io.FileNotFoundException;
import java.util.Objects;

/**
 * 文件上传类
 *
 * @author DZS dzsdevelop@163.com
 * @version V1.0
 * @date 2015-12-23 上午9:47:02
 */
public class Upload {
	
	private byte[] data; // 上传文件的数据
	
	private String fileName = "test";// 文件名称
	
	private String formName;// type="file"表单字段所对应的name属性值
	
	private String contentType; // 内容类型。不同的图片类型对应不同的值，具体请参考Multimedia
	
	
	public static void uploadImage(String fileName, String formName) throws FileNotFoundException {
		new Upload(fileName, formName, "image/jpeg");
	}
	
	public Upload(byte[] data, String fileName, String formName, String contentType) {
		this.data = data;
		this.fileName = fileName;
		this.formName = formName;
		this.contentType = contentType;
	}
	
	public Upload(String filePath, String formName, String contentType) throws FileNotFoundException {
		int beginIndex = filePath.lastIndexOf(Objects.requireNonNull(System.getProperty("file.separator")));// The value of file.separator
		if (beginIndex < 0) {
			beginIndex = filePath.lastIndexOf("/");
		}
		this.fileName = filePath.substring(beginIndex + 1, filePath.length());
		this.formName = formName;
		if (contentType != null)
			this.contentType = contentType;
		if (data == null) {
			data = FileUtils.file2byte(filePath);
		}
	}
	
	
	public byte[] getData() {
		return data;
	}
	
	public void setData(byte[] data) {
		this.data = data;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFormName() {
		return formName;
	}
	
	public void setFormName(String formName) {
		this.formName = formName;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
}
