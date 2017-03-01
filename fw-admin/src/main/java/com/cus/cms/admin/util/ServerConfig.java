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

    @Config.Key("web_html_path")
    @Config.DefaultValue("/data/static")
    String webHtmlPath();

    @Config.Key("mob_html_path")
    @Config.DefaultValue("/data/static")
    String mobHtmlPath();
}
