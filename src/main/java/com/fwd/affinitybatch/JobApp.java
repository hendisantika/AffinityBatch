/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fwd.affinitybatch;

import com.fwd.affinitybatch.processor.LoadData;
import com.fwd.affinitybatch.processor.SFTP;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Hendi Santika <hendisantika@gmail.com / hendisantika@yahoo.co.id >
 */
public class JobApp implements Job {

    public static Properties prop = new Properties();
    public static InputStream input;
    // The name of the file to open.
//    private static final String path = "D:\\Tes\\inputFiles\\MEMBER1.TXT";
//    private static final String src ="D:\\Tes\\inputFiles";
//    private static final String src = prop.getProperty("src");
//    private static final String dest = "D:\\Tes\\outputFiles";
//    private static final String dest = prop.getProperty("dest");

    private static final Logger log = Logger.getLogger(JobApp.class.getName());

    private static org.apache.logging.log4j.Logger log4j2 = LogManager.getLogger(LoadData.class);

    public void execute(JobExecutionContext context)
            throws JobExecutionException {

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        try {
            String path = new File(".").getCanonicalPath();
//            input = new FileInputStream("C:\\Users\\idnhsn\\Documents\\NetBeansProjects\\Quartz\\conf\\affinity_config.properties");
            input = new FileInputStream(path + "\\conf\\affinity_config.properties");
            try {
                // load a properties file
                prop.load(input);
            } catch (IOException ex) {
                Logger.getLogger(JobApp.class.getName()).log(Level.SEVERE, null, ex);
            }

            String src = prop.getProperty("src");
            String dest = prop.getProperty("dest");
            String host = prop.getProperty("SftpHost");
            String username = prop.getProperty("SftpUsername");
            String password = prop.getProperty("SftpPassword");
            int port = Integer.parseInt(prop.getProperty("SftpPort"));
            String sftpPath = prop.getProperty("sftp");

            SFTP cek = new SFTP();

            boolean ada = cek.cekFTP(host, username, password, port, sftpPath);

            if (ada) {
                System.out.println("Ada file ... " + dateFormat.format(date));
                log4j2.info("Ada file ... " + dateFormat.format(date));
                cek.Download(host, username, password, port, sftpPath);
                File file = new File(src);
                File[] lstFile = file.listFiles();

                if (lstFile.length > 0) {
                    for (File listFiles : lstFile) {
                        System.out.println("File List : " + listFiles.getName()
                                + " ...... " + dateFormat.format(date));
                        log4j2.info("File List : " + listFiles.getName()
                                + " ...... " + dateFormat.format(date));

                        LoadData load = new LoadData();
//                        List<Member> memberList = new ArrayList<>();
//                        memberList = load.Load(src, dest);
                        load.Load(src, dest);

//                        for (int i = 0; i >= memberList.size(); i++) {
//                            System.out.println("Fullname : " + memberList.get(i).getFullName());
//                        }

                        MoveFiles(src, dest);
                        
                        System.out.println("==========  DONE | " + dateFormat.format(date) + " ==========");
                        log4j2.info("==========  DONE | " + dateFormat.format(date) + " ==========");

                    } // Block for
                } // Block if
                else {
                    System.out.println("Getting files from " + src + " ...... " + dateFormat.format(date));
                    log.log(Level.INFO, "Getting files from {0} ...... {1}", new Object[]{src, dateFormat.format(date)});
                    System.out.println("Path : " + path);
                }
            } else {
                System.out.println("Tidak Ada file ... " + dateFormat.format(date));
            }

//            File file = new File(src);
//            File[] lstFile = file.listFiles();
//
//            if (lstFile.length > 0) {
//                for (File listFiles : lstFile) {
//                    System.out.println("File List : " + listFiles.getName()
//                            + " ...... " + dateFormat.format(date));
//
//                    LoadData load = new LoadData();
//                    load.Load(src, dest);
//
//                    MoveFiles(src, dest);
//
//                } // Block for
//            } // Block if
//            else {
//                System.out.println("Getting files from " + src + " ...... " + dateFormat.format(date));
//                log.log(Level.INFO, "Getting files from {0} ...... {1}", new Object[]{src, dateFormat.format(date)});
//                System.out.println("Path : " + path);
//            }
        } catch (IOException | SQLException | ParseException | InterruptedException ex) {
            Logger.getLogger(JobApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void MoveFiles(String a, String b) throws FileNotFoundException, IOException {
        File src = new File(a);
        File dest = new File(b);

        File fileMove = new File(src.toString());
        if (!fileMove.exists()) {
            System.out.println(src + " Directory doesn't exists");
            log.log(Level.INFO, "{0} Directory doesn''t exists", src);
        }
        File[] listFiles = fileMove.listFiles();
        System.out.println("Jumlah File : " + listFiles.length);
        log.log(Level.INFO, "Jumlah File : {0}", listFiles.length);

        if (listFiles.length > 0) {
            System.out.println("Moving File process .....");
            log.info("Moving File process .....");
            for (File listFile : listFiles) {
                System.out.println(listFile.getName());
                log.info(listFile.getName());
                String x = src + "/" + listFile.getName();
                String y = dest + "/" + listFile.getName();
                File f1 = new File(x);
                f1.renameTo(new File(y));
                System.out.println("Moving file success!");
                log.info("Moving file success!");
            }

        } else {
            // File Not Found
            System.out.println("File Tidak ada!");
            log.info("File Tidak ada!");
            System.out.println("File tidak jadi dipindahkan!");
            log.info("File tidak jadi dipindahkan!");
        }
    }
}
