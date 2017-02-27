package com.cus.cms.admin.util;
/**
 *
 * @author Andy   2014-12-2
 * @description 允许的图片的类型
 * 
 */
public enum ImageType {
	PNG,
	BMP,
	JPG,
	JPEG,
	GIF;
	
	public static boolean contains(String type){
		try{
			ImageType.valueOf(type.toUpperCase());
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
}

