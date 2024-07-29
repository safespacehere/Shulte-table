package ru.shulte;

import java.sql.*;

public class Registration {
    //@Что то там
    public static int verification(Connection conn, String userLogin, String userPassword, String userName, String userSurname) {
        try {
            Statement statement = conn.createStatement();
            if(userLogin.equals("") || userPassword.equals("") || userName.equals("") || userSurname.equals("")){
                return -1;
            }
            else {
                statement.executeUpdate("INSERT Users(Login, Password, Name, Surname) " +
                        "VALUES ('" + userLogin + "', '" + userPassword + "', '" + userName + "', '" + userSurname + "')");
                return 1;
            }

        } catch (SQLException ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
            System.out.println(ex.getErrorCode());

            return ex.getErrorCode();
        }
    }
}
