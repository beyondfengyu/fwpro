package com.cus.wob.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author laochunyu 2015-11-12
 * @description 
 *
 */
public class RequestUtil{
	private static final Pattern IS_LICIT_IP_PATTERN = Pattern.compile("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$");
	
    public static String getRealIp(HttpServletRequest request){
        String ip = request.getHeader("X-Forwarded-For");
        if(BlankUtil.isBlank(ip))
            return request.getRemoteAddr();
        int index = ip.lastIndexOf(',');
        String lastip = ip.substring(index + 1).trim();
        if("127.0.0.1".equals(lastip) || !isLicitIp(lastip))
            return request.getRemoteAddr();
        else
            return lastip;
    }

    public static boolean isLicitIp(String ip){
        if(BlankUtil.isBlank(ip))
            return false;
        Matcher m = IS_LICIT_IP_PATTERN.matcher(ip);
        return m.find();
    }

    public static Set<String> getClientIpSet(HttpServletRequest request){
        Set<String> ipSet = new HashSet<String>();
        String ip = request.getHeader("X-Forwarded-For");
        if(!BlankUtil.isBlank(ip)){
            String ips[] = ip.split(",");
            String as[];
            int j = (as = ips).length;
            for(int i = 0; i < j; i++){
                String tmpip = as[i];
                if(!"127.0.0.1".equals(tmpip) && isLicitIp(tmpip))
                    ipSet.add(tmpip.trim());
            }
        }
        if(ipSet.size() == 0)
            ipSet.add(request.getRemoteAddr());
        return ipSet;
    }

    

}