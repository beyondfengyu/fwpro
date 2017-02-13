package com.cus.cms.common.frame.mvc;

import java.io.File;
import java.net.URL;

/**
 * @author Andy  2016/2/25.
 * @description
 */
public class ContextUtil {

    public static String getContextPath(){
        return getContextPath("/");
    }

    /**
     * 获取系统的真实路径
     * @return
     */
    public static String getContextPath(String filePath){
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(filePath);
            File cherryConfig = new File(url.toURI());
            return cherryConfig.getAbsolutePath();
        }catch (Exception e){

        }
        return null;
    }

}
