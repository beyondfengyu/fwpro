package com.cus.cms.admin.util;

import com.cus.cms.common.util.BlankUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author laochunyu   2014-9-4
 * @description 文件帮助类
 */
public class AdminUtil {

    private static AtomicLong genrator = new AtomicLong();

    /**
     * 通过UUID产生唯一的字符串
     *
     * @return
     */
    public static String generByUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 以当前时间产生唯一的字符串
     *
     * @return
     */
    public static String generByDate() {
        Date now = new Date();
        Long time = now.getTime() + genrator.getAndIncrement();
        return time.toString();
    }

    /**
     * 普通类获取WEB绝对路径的方法
     *
     * @param cls  类的类对象
     * @param path 相对路径,前面应该加上/
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static final String getAbsolutePath(Class cls, String path) {
        String tmp = cls.getClassLoader().getResource("").getPath();
        String basePath = null;
        // 对 Windows 下获取 物理路径 做 特殊处理
        if ("\\".equals(File.separator)) {
            basePath = tmp.substring(1, tmp.indexOf("WEB-INF") - 1);
        } else {
            basePath = tmp.substring(0, tmp.indexOf("WEB-INF") - 1);
        }
        //注意basePath后面没有加斜杠
        return basePath + path;
    }

    /**
     * 获取项目的绝对路径
     *
     * @param request
     * @param path    相对路径
     * @return
     */
    public static final String getAbsolutePath(HttpServletRequest request, String path) {
        if (BlankUtil.isBlank(path)) {
            path = "";//默认路径
        } else {
            if (path.startsWith("/")) {
                //去除路径前面的斜杠
                path = path.substring(1);
            }
        }
        return request.getSession().getServletContext().getRealPath(path);
    }

    /**
     * 获取文件的类型,如：txt, png, bmp
     *
     * @param filePath 文件路径
     * @return
     */
    public static final String getFileType(String filePath) {
        if (!BlankUtil.isBlank(filePath)) {
            //判断路径字符串是否正确
            if (filePath.contains("."))
                return filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
        }
        return null;
    }

    /**
     * 获取文件的类型
     *
     * @return
     */
    public static final String getFileType(File file) {
        String path = null;
        if (file != null && file.exists()) {
            path = file.getAbsolutePath();
        }
        return getFileType(path);
    }

    /**
     * 获取 root 目录下所有的文件
     *
     * @param rootPath 根目录路径
     * @return
     */
    public static final List<File> getFileInRoot(String rootPath) {
        if (!BlankUtil.isBlank(rootPath)) {
            File root = new File(rootPath);
            return getFileInRoot(root);
        }
        return null;
    }

    /**
     * 获取 root 目录下所有的文件
     *
     * @param root 根文件
     * @return
     */
    public static final List<File> getFileInRoot(File root) {
        List<File> files = null;
        //判断根目录或文件是否存在
        if (root != null && root.exists()) {
            files = new ArrayList<File>();
            if (root.isDirectory()) {
                //目录，获取目录下的所有文件
                File[] cList = root.listFiles();
                for (File f : cList) {
                    //以当前子目录做为根目录，获取下一层次的所有文件
                    files.addAll(getFileInRoot(f));
                }
            } else {//文件，加入列表返回
                files.add(root);
            }
        }
        return files;
    }

    /**
     * 获取根文件下匹配指定正则表达式的所有文件
     *
     * @param root  根文件
     * @param regex 文件名称的正则表达式
     * @return
     */
    public static final List<File> getFileInRoot(File root, String regex) {
        if (BlankUtil.isBlank(regex) || "*".equals(regex)) {
            getFileInRoot(root);//默认匹配所有类型
        }
        //判断根目录或文件是否存在
        if (root != null && root.exists()) {
            List<File> files = new ArrayList<File>();
            if (root.isDirectory()) {
                //目录，获取目录下的所有文件
                File[] cList = root.listFiles();
                for (File f : cList) {
                    //以当前子目录做为根目录，获取下一层次的所有文件
                    files.addAll(getFileInRoot(f, regex));
                }
            } else {//文件，加入列表返回
                if (root.getName().matches(regex))
                    files.add(root);
            }
            return files;
        }
        return null;
    }


    public static void main(String[] args) throws IOException {

    }
}

