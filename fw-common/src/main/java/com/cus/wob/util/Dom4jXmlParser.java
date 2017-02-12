package com.cus.wob.util;


import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;

/**
 * 使用Dom4j组件来解析xml文件
 * @author Andy
 * @version 1.0
 * @date 2016/8/3
 */
public class Dom4jXmlParser {
    private static final Logger logger = LoggerFactory.getLogger(Dom4jXmlParser.class);
    private Document document;
    private Element root;

    public Dom4jXmlParser(String filePath) {
        this(filePath,false);
    }
    /**
     *
     * @param filePath
     * @param validate
     */
    public Dom4jXmlParser(String filePath, boolean validate) {
        SAXReader saxReader = new SAXReader(validate);
        try {
            document = saxReader.read(new File(filePath));
            root = document.getRootElement();
        } catch (DocumentException e) {
           logger.error("SAXReader read file error,",e);
        }
    }

    public Element getRoot(){
        return root;
    }

}
