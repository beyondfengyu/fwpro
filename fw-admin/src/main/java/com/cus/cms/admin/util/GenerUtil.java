package com.cus.cms.admin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Andy
 */
public class GenerUtil {
    private static final Logger logger = LoggerFactory.getLogger(GenerUtil.class);

    /**
     * 调用代码同步脚本。请在linux环境下进行调用。
     *
     * @return 命令状态码：-1 无返回码， 0 正常， > 0 同步代码失败
     * @throws IOException
     * @throws InterruptedException
     */
    public static int rsyncGenerFile(String classPath) throws IOException, InterruptedException {
        if (classPath.charAt(classPath.length() - 1) != '/') {
            classPath += "/";
        }

        String[] cmds = {"/bin/bash", classPath + "page_file_push.sh"};
        Process pro = Runtime.getRuntime().exec(cmds);
        pro.waitFor();
        InputStream in = pro.getInputStream();
        BufferedReader read = new BufferedReader(new InputStreamReader(in));
        InputStream err = pro.getErrorStream();
        BufferedReader outRead = new BufferedReader(new InputStreamReader(err));
        StringBuilder cout = new StringBuilder();
        StringBuilder cerr = new StringBuilder();
        String line = null;
        while ((line = read.readLine()) != null) {
            cout.append(line + "\n");
        }
        logger.info("rsyncGenerFile cout:{}", cout.toString());

        while ((line = outRead.readLine()) != null) {
            cerr.append(line + "\n");
        }
        if (cerr.length() > 0) {
            logger.warn("rsyncGenerFile cerr:{}", cerr.toString());
        }

        //提取返回码
        int exitCode = -1;
        if (cout.lastIndexOf("exit:") > 0) {
            String exStr = cout.substring(cout.lastIndexOf("exit:") + 5, cout.length() - 1);
            exitCode = Integer.parseInt(exStr);
        }
        logger.info("rsyncGenerFile exit code : {}", exitCode);
        return exitCode;
    }
}
