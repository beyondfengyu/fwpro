package com.cus.cms.admin.util;

import org.aeonbits.owner.Config;

/**
 * @author Andy
 */
@Config.Sources("classpath:/config/server.properties")
public interface ServerConfig extends Config{

    @Config.Key("master")
    @Config.DefaultValue("true")
    boolean isMaster();
}
