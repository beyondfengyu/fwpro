package com.cus.cms.common.util;

import java.util.Random;

/**
 * @author Andy  2016/3/24.
 * @description
 */
public class NumberUtil {

    /**
     * 生成指定范围的随机数
     * @param min
     * @param max
     * @return
     */
    public static int getRandomNumber(int min,int max){
        Random random = new Random();
        return random.nextInt(max)%(max-min+1) + min;

    }
}
