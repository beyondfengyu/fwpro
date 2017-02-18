package com.cus.cms.common.model;

/**
 * @author Andy
 */
public interface RedisKey {
    /**
     * redis key标准格式类
     *
     * @author kun.zhang@downjoy.com
     */

    interface TEST_MONGODB_CACHE {
        String keyprefix = "test:mongodb";//前缀
        String keyformat = keyprefix + "%s";//后缀
        int expire = 60 * 60 * 24;//缓存存活时间1天
    }
}
