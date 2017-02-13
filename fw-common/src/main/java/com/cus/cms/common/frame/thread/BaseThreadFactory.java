package com.cus.cms.common.frame.thread;

import org.slf4j.Logger; import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Andy  2015/12/7.
 * @description
 */
public class BaseThreadFactory implements ThreadFactory {
    private static final Logger m_logger = LoggerFactory.getLogger(BaseThreadFactory.class);
    static final AtomicInteger poolNumber = new AtomicInteger(1);
    final AtomicInteger threadNumber = new AtomicInteger(1);
    final ThreadGroup threadGroup;
    final String namePrefix;

    public BaseThreadFactory() {
        SecurityManager securityManager = System.getSecurityManager();
        threadGroup = (securityManager == null) ? Thread.currentThread().getThreadGroup() :
                securityManager.getThreadGroup();
        namePrefix = "pool-"+poolNumber.getAndIncrement()+"-thread-";
    }

    public BaseThreadFactory(String poolName){
        SecurityManager securityManager = System.getSecurityManager();
        threadGroup = (securityManager == null) ? Thread.currentThread().getThreadGroup() :
                securityManager.getThreadGroup();
        namePrefix = poolName+"-pool-"+poolNumber.getAndIncrement()+"-thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(threadGroup,r,namePrefix+threadNumber.getAndIncrement(),0);
        if(thread.isDaemon()){
            thread.setDaemon(false);
        }
        if(thread.getPriority() != Thread.NORM_PRIORITY){
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}
