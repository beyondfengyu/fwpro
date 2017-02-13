package com.cus.cms.common.util;

import java.io.*;
import java.net.URL;

/**
 * 
 * @author Andy 2015-11-12
 * @description 文件的工具类
 *
 */
public class FileUtil {
//	private static final Logger m_logger = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 获取上下语中文件的绝对路径
	 * @param path 文件的路径
	 * @return
	 */
	public static String getContextAbsolutePath(String path){
		try{
			URL url = Thread.currentThread().getContextClassLoader().getResource(path);
			File file = new File(url.toURI());
			return file.getAbsolutePath();
		}catch (Exception e) {
//			m_logger.warn("getContextAbsolutePath fail,path is: "+path, e);
		}
		return null;
	}

	public static String createDirByDate(String basePath){
		StringBuffer buffer = new StringBuffer();
		String dateStr = DateTimeUtil.getCurrentTime("yyyyMM");
		if(!BlankUtil.isBlank(basePath)){
			buffer.append(basePath);
			if(basePath.lastIndexOf("/")<0)
				buffer.append("/");
		}else{
			buffer.append("/");
		}
		buffer.append(dateStr).append("/");
		File file = new File(buffer.toString());
		if(!file.exists()){
			file.mkdirs();
		}
		return buffer.toString();
	}

	/**
	 * 获取网络请求的数据
	 * @param urls
	 * @return
	 */
	public static String getDataFromNetUrl(String urls){
		StringBuffer sb = new StringBuffer();
		BufferedReader bufr = null;
		try {
			URL url = new URL(urls);
			bufr = new BufferedReader(new InputStreamReader(new BufferedInputStream(url.openStream()), "utf-8"));
			String line;
			while ((line = bufr.readLine()) != null) {
				sb.append(line);
			}
		}catch (Exception e){

		}finally {
			if(bufr!=null){
				try {
					bufr.close();
				} catch (IOException e) {
				}
			}
		}
		return  sb.toString();
	}
}
