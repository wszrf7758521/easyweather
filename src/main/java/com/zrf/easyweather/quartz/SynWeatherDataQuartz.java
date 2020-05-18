package com.zrf.easyweather.quartz;

import com.zrf.easyweather.entity.CityInfo;
import com.zrf.easyweather.entity.WeatherRecord;
import com.zrf.easyweather.service.CityInfoService;
import com.zrf.easyweather.service.WeatherRecordService;
import com.zrf.easyweather.vo.WeatherInfoVO;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;


public class SynWeatherDataQuartz extends QuartzJobBean {

    @Autowired
    private CityInfoService cityInfoService;

    @Autowired
    private WeatherRecordService weatherRecordService;

    //高德天气api uesrkey
    public static final String key= "b9ed8c84bc76324f1f4027c0b5a17f1d";

    private final Logger logger = LoggerFactory.getLogger(SynWeatherDataQuartz.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("syn weather quartz task " + new Date());
        this.synCityInfo();
      //  this.sendMail();
    }

    private void synCityInfo(){
        List<CityInfo> cityInfoList = cityInfoService.findAll();
        RestTemplate restTemplate = new RestTemplate();
        String adcode = null;
        String url = null;
        if(cityInfoList != null && cityInfoList.size() > 0){
            logger.info("--------开始同步城市天气信息----------");
            for (CityInfo c:cityInfoList
            ) {
                adcode = c.getAdcode();
                if("100000".equals(adcode) || "900000".equals(adcode)){
                    continue;
                }
                url = "https://restapi.amap.com/v3/weather/weatherInfo?city=" + adcode + "&key=" + key;
                WeatherInfoVO result = restTemplate.getForObject(url, WeatherInfoVO.class);

                if("OK".equals(result.getInfo())){
                    WeatherRecord weatherRecord = new WeatherRecord();
                    weatherRecord.setAdcode(result.getLives().get(0).getAdcode());
                    weatherRecord.setWeather(result.getLives().get(0).getWeather());
                    weatherRecord.setHumidity(result.getLives().get(0).getHumidity());
                    weatherRecord.setWindpower(result.getLives().get(0).getWindpower());
                    weatherRecord.setWinddirection(result.getLives().get(0).getWinddirection());
                    weatherRecord.setTemperature(result.getLives().get(0).getTemperature());
                    weatherRecord.setCity(result.getLives().get(0).getCity());
                    weatherRecord.setReportTime(result.getLives().get(0).getReporttime());
                    weatherRecord.setProvince(result.getLives().get(0).getProvince());
                    weatherRecordService.insertDate(weatherRecord);
                    logger.info("--------正在同步" + result.getLives().get(0).getCity() + "天气信息----------");
                }
            }
            logger.info("--------同步城市天气信息完成----------");
        }

    }


//    private void sendMail(){
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo("742607128@qq.com");
//        mailMessage.setSubject("test weather");
//        mailMessage.setText("---------1333-------");
//        mailMessage.setFrom(from);
//
//        mailSender.send(mailMessage);
//    }

    private void sendMail() {
        Context context = new Context();
        context.setVariable("id","ispringboot");
        String emailContent = templateEngine.process("emailTemplate", context);
        this.sendHtmlMail("742607128@qq.com","EasyWeather-每日天气预报",emailContent);
    }

    public void sendHtmlMail(String to, String subject, String contnet) {
        MimeMessage message = mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(contnet, true);
            helper.setFrom(from);
            mailSender.send(message);
        }
        catch (Exception e){
            e.printStackTrace();
       }
    }


}
