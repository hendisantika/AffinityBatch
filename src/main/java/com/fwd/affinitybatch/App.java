/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.fwd.affinitybatch;

import static com.fwd.affinitybatch.JobApp.input;
import static com.fwd.affinitybatch.JobApp.prop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author idnhsn
 */
public class App {

     private static Logger log = LogManager.getLogger(App.class);
    
    public static void main(String[] args) throws SQLException, IOException, ParseException, InterruptedException, SchedulerException {
        // load a properties file
        String path = new File(".").getCanonicalPath();
//        input = new FileInputStream("C:\\Users\\idnhsn\\Documents\\NetBeansProjects\\Quartz\\conf\\affinity_config.properties");
        input = new FileInputStream( path + "\\conf\\affinity_config.properties");
        prop.load(input);

        int x = Integer.parseInt(prop.getProperty("cron"));
        
    	JobDetail job = JobBuilder.newJob(JobApp.class)
		.withIdentity("Aff_Job", "group1").build();
 
    	Trigger trigger = TriggerBuilder
		.newTrigger()
		.withIdentity("Aff_Trigger", "group1")
		.withSchedule(
			CronScheduleBuilder.cronSchedule("0/"+ x + " * * * * ?")) // per 20 detik
//			CronScheduleBuilder.cronSchedule("0 0 10 * * ? *")) // Perhari jam 10 Pagi
		.build();
 
    	//schedule it
    	Scheduler scheduler = new StdSchedulerFactory().getScheduler();
    	scheduler.start();
    	scheduler.scheduleJob(job, trigger);
    }
}
