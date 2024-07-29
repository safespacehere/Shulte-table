package ru.shulte;

import java.sql.*;

public class Authorization {
    public static int validation(Connection conn, String userLogin, String userPassword) {
        try {
            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT Id FROM Users " +
                    "WHERE Login = '" + userLogin + "' and Password = '" + userPassword + "'");
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return -1;
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
        return -2;
    }
/*
    public static byte[] getMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return messageDigest;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 должен быть поддержан вашей Java Virtual Machine.", e);
        }
    }*/
}