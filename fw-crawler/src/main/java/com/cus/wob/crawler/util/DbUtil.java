package com.cus.wob.crawler.util;

import java.sql.*;

/**
 * @author Andy
 */
public class DbUtil {

    private static final String driver = "com.mysql.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/fwpro";
    private static final String username = "root";
    private static final String password = "123";


    public static Connection createConn(){
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void closeConn(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        Connection conn = DbUtil.createConn();


        // 要执行的SQL语句
        String sql = "insert into fw_log(txt) SELECT ? " +
                "FROM DUAL WHERE NOT EXISTS(SELECT id FROM fw_log WHERE txt = ?)";

        // statement用来执行SQL语句
        PreparedStatement statement = conn.prepareStatement(sql);

        for(int i=0;i<30;i++) {
            statement.setString(1,i+"_ddd");
            statement.setString(2,i+"_ddd");
            statement.addBatch();
        }
        statement.executeBatch();
        DbUtil.closeConn(conn);
    }
}
