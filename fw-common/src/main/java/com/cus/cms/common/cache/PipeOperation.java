package com.cus.cms.common.cache;

import redis.clients.jedis.Pipeline;

/**
 * @author Andy  2016/3/14.
 * @description
 */
public interface PipeOperation {
    public Pipeline operate(Pipeline pipe);
}
