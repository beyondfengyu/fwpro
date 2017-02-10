package com.cus.wob.wrapper;
/**
 *
 * @author laochunyu   2014-12-3
 * @description 
 *
 */
public class UploadMapper {
	private boolean success;//操作是否成功
	private String fileName;//文件名
	private String path;//完整的文件路径
	private String msg;//提示信息

	public UploadMapper(){

	}

	public UploadMapper(boolean success, String msg){
		this.success = success;
		this.msg = msg;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}

