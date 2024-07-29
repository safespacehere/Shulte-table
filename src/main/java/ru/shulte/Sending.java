package ru.shulte;

import java.sql.*;

public class Sending {
    //@Что то там
    public static int send(Connection conn, Result res) {
        try {
            Statement statement = conn.createStatement();

            statement.executeUpdate("INSERT Results(UserId, Data, T1, T2, T3, T4, T5, jobEfficiency, workabilityDegree, mentalStability) " +
                    "VALUES (" + res.userId + ", CURRENT_TIMESTAMP(), " + res.T[0] + ", " +
                    res.T[1] + ", " + res.T[2] + ", " + res.T[3] + ", " + res.T[4] +
                    ", " + res.jobEfficiency + ", " + res.workabilityDegree + ", " + res.mentalStability +")");
            return 1;
        }
        catch (SQLException ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
            System.out.println(ex.getErrorCode());

            return ex.getErrorCode();
        }
    }
}
