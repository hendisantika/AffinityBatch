/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fwd.affinitybatch.processor;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author IDNHSN
 */
public class SFTP {

    private static Logger log = LogManager.getLogger(LoadData.class);
    public static Properties prop = new Properties();
    public static InputStream input;

    public boolean cekFTP(String host, String username, String password, int port, String path) throws FileNotFoundException, IOException {
        boolean ada = false;
        // load a properties file
        String AppPath = new File(".").getCanonicalPath();
        input = new FileInputStream(AppPath + "\\conf\\affinity_config.properties");
        prop.load(input);
        JSch jsch = new JSch();
        Session session = null;
        String remote = prop.getProperty("sftp");

        try {
            session = jsch.getSession(username, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            // ls all file in sftp directory
            Vector<ChannelSftp.LsEntry> list = sftpChannel.ls(remote + "/*.txt");
            if (list.size() > 0) {
                ada = true;
            }

            sftpChannel.exit();
            session.disconnect();
        } catch (JSchException | SftpException e) {
            System.out.println("Error : " + e.getMessage());
        }

        return ada;
    }

    public void Download(String host, String username, String password, int port, String path) throws FileNotFoundException, IOException {
        // load a properties file
        String AppPath = new File(".").getCanonicalPath();
        input = new FileInputStream(AppPath + "\\conf\\affinity_config.properties");
        prop.load(input);
        JSch jsch = new JSch();
        Session session = null;
        String remote = prop.getProperty("sftp");
        String local = prop.getProperty("src");

        try {
            session = jsch.getSession(username, host, port);
            session.setConfig("StrictHostKeyChecking", "no");
            session.setPassword(password);
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;

            // ls all file in sftp directory
            int i = 0;
//            Vector<ChannelSftp.LsEntry> list = sftpChannel.ls("*.*");
            Vector<ChannelSftp.LsEntry> list = sftpChannel.ls(remote + "/*.txt");
            for (ChannelSftp.LsEntry entry : list) {
                System.out.println("File " + i + " : " + entry.getFilename());
                sftpChannel.get(remote + entry.getFilename(), local + entry.getFilename());
                sftpChannel.rm(remote + entry.getFilename());
                i++;
            }

//            ArrayList<String> list2 = new ArrayList<String>();
//            Vector<LsEntry> entries = sftpChannel.ls(remote);
//            for (LsEntry entry : entries) {
//                if (entry.getFilename().toLowerCase().endsWith(".txt")) {
//                    list2.add(entry.getFilename());
//                }
//            }

//            System.out.println("==============================");
//            System.out.println("Jumlah : " + list2.size());
//            for (int j = 0; j < list2.size(); j++) {
////                LsEntry get = entries.get(j);
//                System.out.println("Tes2 : " + list2.toString());
//            }
            sftpChannel.exit();
            session.disconnect();
        } catch (JSchException | SftpException e) {
            System.out.println("Error : " + e.getMessage());
        }
    }
}
