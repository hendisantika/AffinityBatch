/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fwd.affinitybatch.processor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 *
 * @author idnhsn
 */
public class Koneksi {

    private static final String DB_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; // MS SQL Server Driver
    private static final String DB_CONNECTION = "jdbc:sqlserver://10.17.50.241;databaseName=ReportTools;"; // MS SQL SERVER URL
    private static final String DB_USER = "developer";
    private static final String DB_PASSWORD = "P@ssw0rd";
    public static final Logger log = Logger.getLogger(LoadData.class.getName());

    private static Connection getDBConnection() {

        Connection dbConnection = null;

        try {

            Class.forName(DB_DRIVER);

        } catch (ClassNotFoundException e) {

            System.out.println(e.getMessage());

        }

        try {

            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
            return dbConnection;

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }

        return dbConnection;

    }

    public String[] selectRecordsFromTable() throws SQLException {

        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;
        String[] isi = new String[2];
        String body = null;
        String subject = null;

        String selectSQL = "SELECT * FROM TbEmailTmp WHERE LetterID = 'MOMENT';";

        try {
            dbConnection = getDBConnection();
            preparedStatement = dbConnection.prepareStatement(selectSQL);
//            preparedStatement.setInt(1, 1001);

            // execute select SQL stetement
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {

                body = rs.getString("EmailContent");
                subject = rs.getString("Subject");
//                String CcTo = rs.getString("CcTo");

//                System.out.println("EmailContent : " + body);
//                System.out.println("Subject : " + subject);
//                System.out.println("CcTo : " + CcTo);

            }
            isi[0] = subject;
            isi[1] = body;

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {

            if (preparedStatement != null) {
                preparedStatement.close();
            }

            if (dbConnection != null) {
                dbConnection.close();
            }
        }
        
        return isi;
    }
    
    
    
//    public static void main(String[] args) throws SQLException {
//        Koneksi kon = new Koneksi();
//        String isi[] = kon.selectRecordsFromTable();
//        
//        String hasil = isi[1].replace("member_name", "Uzumaki Naruto");
//        String hasil2 = hasil.replace("member_number", "8000007");
//        
////        System.out.println("Index 1 : " + isi[0]); // subject
////        System.out.println("Index 2 : " + isi[1]); // Body
//        System.out.println("Before Isi : " + isi[1]);
//        System.out.println("===============================");
//        System.out.println("Before Isi : " + hasil2);
//        
//    }

}
