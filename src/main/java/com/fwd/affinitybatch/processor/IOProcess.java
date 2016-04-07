/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fwd.affinitybatch.processor;

import java.io.File;

/**
 *
 * @author idnhsn
 */
public class IOProcess {

    public String getAllFiles(File curDir) {
        String filename = null;

        File[] filesList = curDir.listFiles();
        
        int jml = filesList.length;
        
        System.out.println("Number of File : " + jml);
        
        for (File f : filesList) {
            if (f.isDirectory()) {
                getAllFiles(f);
            }
            if (f.isFile()) {
                System.out.println(f.getName());
            }
        }
        
        return filename;
    }
}
