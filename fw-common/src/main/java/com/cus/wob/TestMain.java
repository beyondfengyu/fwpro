package com.cus.wob;

//import com.artofsolving.jodconverter.DocumentConverter;
//import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
//import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
//import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

import java.io.File;

/**
 * @author Andy
 * @version 1.0
 * @date 2016/11/17
 */
public class TestMain {


    public static void convert(String input, String output){
//        File inputFile = new File(input);
//        File outputFile = new File(output);
//        OpenOfficeConnection connection = new SocketOpenOfficeConnection("localhost",8100);
//        try {
//            connection.connect();
//            DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
//            converter.convert(inputFile, outputFile);
//        } catch(Exception e) {
//            e.printStackTrace();
//        } finally {
//            try{ if(connection != null){connection.disconnect(); connection = null;}}catch(Exception e){}
//        }
        System.out.println("ddddd");
    }

    public static void main(String[] args){
        convert("F:\\我的文档\\attachment.doc","F:\\我的文档\\attachment.pdf");
    }
}
