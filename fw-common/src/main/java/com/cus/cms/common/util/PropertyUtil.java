package com.cus.cms.common.util;


import org.apache.commons.io.FileUtils;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Andy  2015/11/18.
 * @description 配置文件工具类
 */
public class PropertyUtil {
    private static final Logger m_logger = LoggerFactory.getLogger(PropertyUtil.class);
    private static Map<Object,Object> dataMap = new ConcurrentHashMap<Object,Object>();
    private static List<DataFile> fileList=new ArrayList<DataFile>();

    //加载类时启动定时刷新线程，定时获取最新的配置文件数据
    static {
        new Thread(new FileReloadThread()).start();
    }

    /**
     * 根据key获取对应的值
     * @param key
     * @return
     */
    public static String getValByKey(String key){
        return (String)dataMap.get(key);
    }

    /**
     * 根据key获取对应的值，如果key不存在，返回默认值
     * @param key
     * @param defVal 默认值
     * @return
     */
    public static String getValByKey(String key,String defVal){
        String val = (String)dataMap.get(key);
        if(val == null)
            val = defVal;
        return val;
    }

    public static Integer getIntegerByKey(String key){
        return Integer.valueOf(getValByKey(key));
    }

    public static Integer getIntegerByKey(String key, String defVal){
        return Integer.valueOf(getValByKey(key, defVal));
    }

    public static Long getLongByKey(String key){
        return Long.valueOf(getValByKey(key));
    }

    public static Long getLongByKey(String key, String defVal){
        return Long.valueOf(getValByKey(key, defVal));
    }
    /**
     * 加载属性配置文件
     * @param path 文件路径
     */
    public static void addConfigFile(String path) {
        DataFile dataFile = new DataFile(path);
        if(dataFile.exists()){
            loadFileData(dataFile);
            fileList.add(dataFile);
        }
    }

    /**
     * 加载文件的数据
     * @param dataFile
     */
    private static void loadFileData(DataFile dataFile){
        FileInputStream fin = null;
        try{
            fin = FileUtils.openInputStream(dataFile.getFile());
            Properties tmpPro = new Properties();
            tmpPro.load(fin);
            dataMap.putAll(tmpPro);
        }catch (Exception e){
            m_logger.warn("loadFileData load file fail,cause by "+e.getMessage(),e);
        }finally{
            if(fin!=null){
                try {
                    fin.close();
                } catch (IOException e) {
                    m_logger.warn("FileInputStream close fail, cause by "+e.getMessage(),e);
                }
            }
        }
    }

    public static class DataFile {
        private long lastModified;
        private String fileName;
        private File file;

        public DataFile(String fileName) {
            this.fileName = fileName;
            file = new File(fileName);
            lastModified = file.lastModified();
        }

        public boolean isModified() {
            boolean hasMod = false;
            if (file.exists()) {
                hasMod = file.lastModified() - lastModified > 0;
                this.lastModified = file.lastModified();
                return hasMod;
            }
            return hasMod;
        }

        public boolean exists() {
            return file.exists();
        }

        public String getFileName() {
            return fileName;
        }

        public File getFile() {
            return this.file;
        }
    }

    /**
     * 定时加载，获取属性文件中最新的内容
     */
    static class FileReloadThread implements Runnable {
        private static int errCount = 0; //错误的次数
        @Override
        public void run() {
            while(true) {
                try {
                    Thread.sleep(PropertyUtil.getLongByKey("prop_refresh_time", "300000"));//默认5分钟刷新
                    for (DataFile dataFile : fileList) {
                        if (dataFile.exists() && dataFile.isModified()) {
                            PropertyUtil.loadFileData(dataFile);
                        }
                    }
                } catch (InterruptedException e) {
                    m_logger.warn("reload property file fail, cause by " + e.getMessage(), e);
                    errCount++; //错误次数加1
                    if (errCount > 10) break;
                } catch (Exception e) {
                    m_logger.warn("reload property file fail, cause by " + e.getMessage(), e);
                }
                if(fileList.size()==0 && "true".equals(PropertyUtil.getValByKey("if_empty_stop", "true"))){
                    break;
                }
            }
        }
    }
}
