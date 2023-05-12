package com.daopattern.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connecter {
    
    private static final String url = "jdbc:postgresql://localhost:5432/pessoas_db?verifyServerCertificate=false&userSSL=true";
    private static final String user = "postgres";
    private static final String pass = "root";
    
    public static Connection getConnection() {

        try 
        {
            return DriverManager.getConnection(url, user, pass);
        } 
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
