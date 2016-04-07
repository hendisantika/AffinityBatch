/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fwd.affinitybatch;

import com.fwd.affinitybatch.processor.LoadData;
import java.sql.SQLException;

/**
 *
 * @author idnhsn
 */
public class App {

    // The name of the file to open.
    private static final String path = "D:\\Tes\\inputFiles\\MEMBER1.TXT";
//    private static final String path = "D:\\Tes\\inputFiles\\";

    public static void main(String[] args) throws SQLException {
        LoadData load = new LoadData();
        load.Load(path);
//        File curDir = new File(path);
//        IOProcess io = new IOProcess();
//        io.getAllFiles(curDir);

//        File file = new File(path);
//        File[] listFiles = file.listFiles();
////        
//        System.out.println("Jumlah : " + listFiles.length + "\n Yaitu : ");
//
//        if (listFiles.length > 0) {
//            for (File listFile : listFiles) {
//                System.out.println(listFile.getName());
//                load.Load(listFile.getName());
//            }
//        }
    }
}
