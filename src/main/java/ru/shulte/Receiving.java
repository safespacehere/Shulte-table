package ru.shulte;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

public class Receiving {
    public static Vector<Result> receive(Connection conn, int userId) {
        try {
            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM Results " +
                    "WHERE UserId = '" + userId + "'");
            Vector<Result> res = new Vector<Result>();
            int[] T = new int[5];
            while (resultSet.next()) {
                T[0] = resultSet.getInt(4);
                T[1] = resultSet.getInt(5);
                T[2] = resultSet.getInt(6);
                T[3] = resultSet.getInt(7);
                T[4] = resultSet.getInt(8);
                res.add(new Result(resultSet.getInt(1), resultSet.getInt(2), resultSet.getTimestamp(3), T,
                        resultSet.getFloat(9), resultSet.getFloat(10), resultSet.getFloat(11)));
            }
            return res;

        } catch (SQLException ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
            System.out.println(ex.getErrorCode());
        }
        return null;
    }
}
