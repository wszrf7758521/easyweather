package com.zrf.easyweather.quartz;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail weatherQuartzDetail(){
        return JobBuilder.newJob(SynWeatherDataQuartz.class).withIdentity("weatherQuartz").storeDurably().build();
    }

    @Bean
    public JobDetail mailQuartzDetail(){
        return JobBuilder.newJob(SendMailQuartz.class).withIdentity("mailQuartz").storeDurably().build();
    }
    @Bean
    public Trigger testQuartzTrigger(){
//        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                .withIntervalInSeconds(86400)  //设置时间周期单位秒
//                .repeatForever();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 5 * * ?");
        return TriggerBuilder.newTrigger().forJob(weatherQuartzDetail())
                .withIdentity("weatherQuartz")
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    @Bean
    public Trigger mailQuartzTrigger(){
//        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                .withIntervalInSeconds(86400)  //设置时间周期单位秒
//                .repeatForever();
          CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 30 6 * * ?");
//        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("20 33 20 * * ?");
        return TriggerBuilder.newTrigger().forJob(mailQuartzDetail())
                .withIdentity("mailQuartz")
                .withSchedule(cronScheduleBuilder)
                .build();
    }
}
