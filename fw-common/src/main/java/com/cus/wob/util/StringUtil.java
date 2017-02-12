package com.cus.wob.util;

import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Andy  2015/12/29
 * @description
 */
public class StringUtil {

    /**
     * 判断验证码
     * @param inCode
     * @return
     */
    public static boolean checkCode(String inCode,HttpSession session){
        String sCode = (String) session.getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        if(!BlankUtil.isBlank(inCode)&&!BlankUtil.isBlank(sCode)){
            String code = EncrytcUtil.encodeMD5String(sCode.toLowerCase());
            if(inCode.equals(code)){   //判断验证码是否正确
                return true;
            }
        }
        return false;
    }

    /**
     *  过滤javascript标签
     *为了可以链式使用过滤功能，即使为空也返回空字符串
     *
     * @param text
     * @return
     */
    public static String filterJavaScript(String text){
        if(BlankUtil.isBlank(text))
            return "";

        return Pattern.compile("<script.*?>.*?</script>", Pattern.CASE_INSENSITIVE).matcher(text).replaceAll("");
    }

    /**
     * 过滤特殊字符，符合于过滤SQL注入的文本
     *为了可以链式使用过滤功能，即使为空也返回空字符串
     *
     * @param text
     * @return
     */
    public static String filterSpecial(String text){
        if(BlankUtil.isBlank(text))
            return "";
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern   p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(text);
        return   m.replaceAll("").trim();
    }

    /**
     * 截取指定长度的字符串
     *
     * @param text
     * @param size
     * @return
     */
    public static String filterSubstring(String text,int size){
        if(BlankUtil.isBlank(text)){
            return "";
        }
        if(text.length()>size){
            text = text.substring(0,size);
        }
        return text;
    }

    public static void main(String[] args){
        String ddd = "ssdfs;MMMM##OOO,,,.0000'333\"--PPPP";
        ddd = StringUtil.filterSubstring(StringUtil.filterJavaScript(StringUtil.filterSpecial(ddd)),18);

        System.out.println(filterSpecial(ddd));
    }
}
