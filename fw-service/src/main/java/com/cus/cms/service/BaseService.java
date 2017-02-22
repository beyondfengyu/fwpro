package com.cus.cms.service;


import com.cus.cms.common.cache.RedisEnv;
import com.cus.cms.common.model.wen.FwPage;
import com.cus.cms.common.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Andy   2015-11-10
 * @description
 */
public abstract class BaseService{

    protected final Logger m_logger = LoggerFactory.getLogger(getClass());
    @Autowired
    protected RedisEnv redisEnv;
    @Autowired
    protected SnowFlake snowFlake;

    public RedisEnv getRedisEnv() {
        return redisEnv;
    }

    public SnowFlake getSnowFlake() {
        return snowFlake;
    }


}

