package sqlserver;

import org.example.sqlserver.DBUtil;
import org.junit.Test;

import java.sql.*;
import java.util.*;

public class TestDBUtil {
    @Test
    public void testConnection() {
        Connection connection = DBUtil.getConnection();
        ResultSet resultSet = null;
        Statement statement = null;
        List<Map> list = new ArrayList<>();
        try {
            statement = connection.createStatement();
            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT * from AQ_WSJC_DevAndRealData awdard";
            resultSet = statement.executeQuery(selectSql);
            //获取键名
            ResultSetMetaData md = resultSet.getMetaData();
            int columnCount = md.getColumnCount();//获取行的数量

            // Print results from select statement
            while (resultSet.next()) {
                Map rowData = new HashMap();//声明Map
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i), resultSet.getObject(i));//获取键名及值
                }
                list.add(rowData);
//                System.out.println(resultSet.getString(0) + " " + resultSet.getString(3));
            }

        } catch (Exception e) {

        } finally {
            DBUtil.close(resultSet, null, connection);
        }

        System.out.println("the result");
    }
}
