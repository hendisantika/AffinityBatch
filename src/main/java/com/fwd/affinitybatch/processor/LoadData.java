/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fwd.affinitybatch.processor;

import com.fwd.affinitybatch.model.Member;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author idnhsn
 */
public class LoadData {

//    private static final String DB_DRIVER = "com.mysql.jdbc.Driver"; // MySQL Driver
//    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306;afinity;"; // MySQL URL
    private static final String DB_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; // MS SQL Server Driver
    private static final String DB_CONNECTION = "jdbc:sqlserver://10.17.62.12;databaseName=AFFINITY;"; // MS SQL SERVER URL
    private static final String DB_USER = "developer";
    private static final String DB_PASSWORD = "P@ssw0rd";
    public static final Logger log = Logger.getLogger(LoadData.class.getName());

    public List<Member> Load(String fileName) throws SQLException {
        // This will reference one line at a time
        String line = null;
//        String line;
        String splitBy = ";";
        List<Member> memberList = new ArrayList<>();

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader br = new BufferedReader(fileReader);
            br.readLine();

//            File file = new File(fileName);
//            File[] listFiles = file.listFiles();
//
//            if (listFiles.length > 0) {
//                for (File listFile : listFiles) {
//
//                    
//                }
//            }
            while ((line = br.readLine()) != null) {
                String[] col = line.split(splitBy);
                Member data = new Member();

                data.setMemberId(col[2]);
                data.setFullName(col[0]);
                data.setBirthDate(col[1]);
                data.setMobileNo(col[3]);
                data.setEmail(col[4]);
                data.setJoinDate(col[5]);

                memberList.add(data);

                String memberid = col[2];
                String fullname = col[0];
                String birthdate = col[1];
                String mobile = col[3];
                String email = col[4];
                String joindate = col[5];

                // Insert record to database 
//                insertRecord(memberid, fullname, birthdate, mobile, email, joindate);
            }
            printList(memberList);
            // Always close files.
            br.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Directory is empty '" + fileName + "'");
            System.out.println("File Not Found '" + fileName + "'");
            System.out.println("Error " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
            System.out.println("Error " + ex.getMessage());
            // Or we could just do this: 
            // ex.printStackTrace();
        }

        return memberList;
    }

    public static void printList(List<Member> ListToPrint) {
        for (int i = 0; i < ListToPrint.size(); i++) {
            System.out.println("Member [" + i + "]-[ MemberID = " + ListToPrint.get(i).getMemberId()
                    + ", Full Name = " + ListToPrint.get(i).getFullName()
                    + ", Birth Date = " + ListToPrint.get(i).getBirthDate()
                    + ", Mobile No = " + ListToPrint.get(i).getMobileNo()
                    + ", Email = " + ListToPrint.get(i).getEmail()
                    + ", Join Date = " + ListToPrint.get(i).getJoinDate()
                    + "]");
        }
    }

    public static void insertRecord(String memberid, String fullname, String birthdate,
            String mobile, String email, String joindate) throws SQLException {

        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;

        // MySQL 
//        String updateTableSQL = "insert into member"
//                + "(memberid, fullname, birthdate, mobile, email, joindate)" 
//                + "values (?, ?, ?, ?, ?, ?)";
        // MS SQL Server 
        String updateTableSQL = "INSERT INTO MS_MEMBER"
                + "(MEMBER_NO, MEMBER_NAME, BIRTH_DATE, MOBILE_NO, EMAIL, JOIN_DATE)"
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try {
            dbConnection = getDBConnection();
            preparedStatement = dbConnection.prepareStatement(updateTableSQL);

            preparedStatement.setString(1, memberid);
            preparedStatement.setString(2, fullname);
            preparedStatement.setString(3, birthdate);
            preparedStatement.setString(4, mobile);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, joindate);

            // execute insert SQL stetement
            preparedStatement.executeUpdate();

            System.out.println("Record Data has been inserted to Database AFFINITY!");

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

    }

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

}
