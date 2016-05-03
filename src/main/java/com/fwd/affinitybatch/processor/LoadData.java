/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fwd.affinitybatch.processor;

import static com.fwd.affinitybatch.JobApp.input;
import static com.fwd.affinitybatch.JobApp.prop;
import com.fwd.affinitybatch.model.Member;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

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
//    public static final Logger log = Logger.getLogger(LoadData.class.getName());
    private static Logger log = LogManager.getLogger(LoadData.class);

    public List<Member> Load(String src, String dest) throws SQLException, IOException, ParseException, InterruptedException {
//    public void Load(String src, String dest) throws SQLException, IOException, ParseException, InterruptedException {
        String path = new File(".").getCanonicalPath();
        input = new FileInputStream(path + "\\conf\\affinity_config.properties");
        // load a properties file
        prop.load(input);

        // This will reference one line at a time
        String line = null;
//        String line;
        String splitBy = ";";
        String error = prop.getProperty("error");

        List<Member> memberList = new ArrayList<>();
        List<Member> duplikasi = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        SendEmail send = new SendEmail();

        boolean ada = false;

        File file = new File(src);
        File[] listFiles = file.listFiles();

        if (listFiles.length > 0) {
            for (File listFile : listFiles) {
                System.out.println("Filename : " + listFile.getAbsolutePath());
                log.info("Filename : " + listFile.getAbsolutePath());

                try {
                    // FileReader reads text files in the default encoding.
                    FileReader fileReader = new FileReader(listFile.getAbsoluteFile());

                    // Always wrap FileReader in BufferedReader.
                    BufferedReader br = new BufferedReader(fileReader);
                    br.readLine();

                    while ((line = br.readLine()) != null) {
                        String[] col = line.split(splitBy);
                        Member data = new Member();

                        data.setMemberId(col[2]);
                        data.setFullName(col[0]);
                        data.setBirthDate(col[1]);
                        data.setMobileNo(col[3]);
                        data.setEmail(col[4]);
                        data.setJoinDate(col[5]);

//                        memberList.add(data);
                        String memberid = col[2];
                        String fullname = col[0];
                        String birthdate = col[1];
                        String mobile = col[3];
                        String email = col[4];
                        String joindate = col[5];
//
                        ada = cekId(col[2]);
                        if (ada) {
                            duplikasi.add(data);
                        } else {
                            memberList.add(data);
                        }
////                        
//                        if (ada) {
//                            System.out.println("Ada MemberID yang sama.\nSilahkan cek data anda kembali!");
//                            log.info("Ada MemberID yang sama.\nSilahkan cek data anda kembali!");
//                            String nama = listFile.getAbsolutePath();
//                            send.sendEmailAttachment(nama);
////                            send.sendEmailAttachment();
//                        } else {
//                            System.out.println("Tidak Ada MemberID yang sama.\nData anda akan diproses!");
//                            log.info("Tidak Ada MemberID yang sama.\nData anda akan diproses!");
//                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                            Date birthDate = sdf.parse(birthdate); //Yeah !! It's my date of birth :-)
//                            AgeCalculator u = new AgeCalculator();
//                            int age = u.calculateAge(birthDate);
//                            //My age is
//                            if ((age >= 12) && (age <= 65)) {
//                                System.out.println("Member NO : " + memberid + "| Full Name : " + fullname + " |Usia : " + age + " | Usia member OK!");
//                                log.info("Member NO : " + memberid + "| Full Name : " + fullname + "|Usia : " + age + " | Usia member OK!");
////                            // Insert record to database 
//                                Koneksi kon = new Koneksi();
//////                        insertRecord(memberid, fullname, birthdate, mobile, email, joindate);
//                                String isi[] = kon.selectRecordsFromTable();
//
//                                
//
//                                String hasil = isi[1].replace("[member_name]", fullname);
//                                String body = hasil.replace("[member_number]", memberid);
//
////                            send.SendEmail(email, isi[0], body);
////                            System.out.println("============== START =============");
////                            System.out.println(memberid + "|" + fullname);
////                            System.out.println(body);
////                            System.out.println("============== END =============");
//                            } else {
//                                System.out.println("Member NO : " + memberid + "| Full Name : " + fullname + "|Usia : " + age + " | Usia member harus >= 17 atau <= 65 th");
//                                log.info("Member NO : " + memberid + "| Full Name : " + fullname + "|Usia : " + age + " | Usia member harus >= 17 atau <= 65 th");
//                            } // Block validasi
//                        }
                    } // Block While - Reading File

//                     ada = cekId(col[2]);
//                        if (ada) {
//                            duplikasi.add(data);
//                        }else{
//                          memberList.add(data);  
//                        }
//                        
                    // Always close files.
                    br.close();

                    int jml = duplikasi.size();
                    System.out.println("Jumlah duplikasi = " + jml);
//                    for (int i = 0; i < jml; i++) {

//                    if ((jml > 0) && (memberList.size() == 0)) {
                    if (jml > 0) {
                        System.out.println("Ada MemberID yang sama.\nSilahkan cek data anda kembali!");
                        log.info("Ada MemberID yang sama.\nSilahkan cek data anda kembali!");
                        String nama = listFile.getAbsolutePath();
                        System.out.println("listFile.getAbsolutePath() : " + listFile.getAbsolutePath());
                        for (Member isi : duplikasi) {
                            //   System.out.println(duplikasi.get(i).getMemberId() + "|" + duplikasi.get(i).);
                            System.out.println(isi.getMemberId() + "|" + isi.getFullName() + "|"
                                    + isi.getBirthDate() + "|" + isi.getEmail() + "|"
                                    + isi.getMobileNo() + "|" + isi.getJoinDate());
                        }
                        // Send Email
                        send.sendEmailAttachment(nama);
                        MoveErrorFiles(src, error);
                    }
                    if ((jml == 0) && (memberList.size() > 0)) {
                        System.out.println("Tidak Ada MemberID yang sama.\nData anda akan diproses!");
                        log.info("Tidak Ada MemberID yang sama.\nData anda akan diproses!");

                        for (Member isi : memberList) {
                            //   System.out.println(duplikasi.get(i).getMemberId() + "|" + duplikasi.get(i).);
                            System.out.println(isi.getMemberId() + "|" + isi.getFullName() + "|"
                                    + isi.getBirthDate() + "|" + isi.getEmail() + "|"
                                    + isi.getMobileNo() + "|" + isi.getJoinDate());

                            Date birthDate = sdf.parse(isi.getBirthDate()); //Yeah !! It's my date of birth :-)
                            AgeCalculator u = new AgeCalculator();
                            int age = u.calculateAge(birthDate);
                            //My age is
                            if ((age >= 12) && (age <= 65)) {
                                System.out.println("Member NO : " + isi.getMemberId() + "| Full Name : " + isi.getFullName() + " |Usia : " + age + " | Usia member OK!");
                                log.info("Member NO : " + isi.getMemberId() + "| Full Name : " + isi.getFullName() + "|Usia : " + age + " | Usia member OK!");
//                            // Insert record to database 
                                Koneksi kon = new Koneksi();
                                insertRecord(isi.getMemberId(), isi.getFullName(), isi.getBirthDate(),
                                        isi.getMobileNo(), isi.getEmail(), isi.getJoinDate());

                                String row[] = kon.selectRecordsFromTable();

                                String hasil = row[1].replace("[member_name]", isi.getFullName());
                                String body = hasil.replace("[member_number]", isi.getMemberId());

                                send.SendEmail(isi.getEmail(), row[0], body);
//                            System.out.println("============== START =============");
//                            System.out.println(memberid + "|" + fullname);
//                            System.out.println(body);
//                            System.out.println("============== END =============");

                                // Move File one by one
                                MoveFiles(src, dest);
                            }

                        }
                    }
                } catch (FileNotFoundException ex) {
                    System.out.println("Directory is empty '" + src + "'");
                    System.out.println("File Not Found '" + src + "'");
                    System.out.println("Error " + ex.getMessage());
                } catch (IOException ex) {
                    System.out.println("Error reading file '" + src + "'");
                    System.out.println("Error " + ex.getMessage());
                    // Or we could just do this: 
                    // ex.printStackTrace();
                }
            }
        }
        printList(memberList);
        
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

    public static boolean cekId(String MEMBER_NO) throws SQLException {

        Connection dbConnection = null;
        PreparedStatement preparedStatement = null;
        boolean id = false;

        String selectSQL = "SELECT COUNT(*) AS jml  FROM MS_MEMBER WHERE MEMBER_NO = ?;";

        try {
            dbConnection = getDBConnection();
            preparedStatement = dbConnection.prepareStatement(selectSQL);
            preparedStatement.setString(1, MEMBER_NO);

            // execute select SQL stetement
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int count = rs.getInt("jml");
                if (count > 0) {
                    id = true;
                    System.out.println("Ada duplikasi MEMBER_NO : " + MEMBER_NO);
                    System.out.println("Jumlahnya : " + count);
                } else {
                    id = false;
                    System.out.println("Tidak ada duplikasi.");
                }
            }

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

        return id;
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

    public static void MoveFiles(String a, String b) throws FileNotFoundException, IOException {
        File src = new File(a);
        File dest = new File(b);

        File fileMove = new File(src.toString());
        if (!fileMove.exists()) {
            System.out.println(src + " Directory doesn't exists");
        }
        File[] listFiles = fileMove.listFiles();
        System.out.println("Jumlah File : " + listFiles.length);

        if (listFiles.length > 0) {
            System.out.println("Moving File process .....");
            for (File listFile : listFiles) {
                System.out.println(listFile.getName());
                String x = src + "/" + listFile.getName();
                String y = dest + "/" + listFile.getName();
                File f1 = new File(x);
                f1.renameTo(new File(y));
                System.out.println("Moving file success!");
            }

        } else {
            // File Nnot Found
            System.out.println("File Tidak ada!");
            System.out.println("File tidak jadi dipindahkan!");
        }
    }

    public static void MoveErrorFiles(String a, String b) throws FileNotFoundException, IOException {
        File src = new File(a);
        File dest = new File(b);

        File fileMove = new File(src.toString());
        if (!fileMove.exists()) {
            System.out.println(src + " Directory doesn't exists");
        }
        File[] listFiles = fileMove.listFiles();
        System.out.println("Jumlah File : " + listFiles.length);

        if (listFiles.length > 0) {
            System.out.println("Moving File process .....");
            for (File listFile : listFiles) {
                System.out.println(listFile.getName());
                String x = src + "/" + listFile.getName();
                String y = dest + "/" + listFile.getName();
                File f1 = new File(x);
                f1.renameTo(new File(y));
                System.out.println("Moving file success!");
            }

        } else {
            // File Nnot Found
            System.out.println("File Tidak ada!");
            System.out.println("File tidak jadi dipindahkan!");
        }
    }

}
