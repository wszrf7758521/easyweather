package com.zrf.easyweather.quartz;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail teatQuartzDetail(){
        return JobBuilder.newJob(SynWeatherDataQuartz.class).withIdentity("testQuartz").storeDurably().build().
                getJobBuilder().newJob(SendMailQuartz.class).withIdentity("mailQuartz").storeDurably().build();
    }

    @Bean
    public Trigger testQuartzTrigger(){
//        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                .withIntervalInSeconds(86400)  //设置时间周期单位秒
//                .repeatForever();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 5 * * ?");
        return TriggerBuilder.newTrigger().forJob(teatQuartzDetail())
                .withIdentity("testQuartz")
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    @Bean
    public Trigger mailQuartzTrigger(){
//        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                .withIntervalInSeconds(86400)  //设置时间周期单位秒
//                .repeatForever();
       // CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 20 6 * * ?");
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("20 9 16 * * ?");
        return TriggerBuilder.newTrigger().forJob(teatQuartzDetail())
                .withIdentity("mailQuartz")
                .withSchedule(cronScheduleBuilder)
                .build();
    }
}
