package com.zrf.easyweather.quartz;

import com.zrf.easyweather.entity.User;
import com.zrf.easyweather.entity.WeatherRecord;
import com.zrf.easyweather.enums.WeatherEnum;
import com.zrf.easyweather.service.UserService;
import com.zrf.easyweather.service.WeatherRecordService;
import com.zrf.easyweather.vo.SuggestVO;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SendMailQuartz extends QuartzJobBean {

    private final Logger logger = LoggerFactory.getLogger(SynWeatherDataQuartz.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;


    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private WeatherRecordService weatherRecordService;

    @Autowired
    private UserService userService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("quartz task " + new Date());
        //1.查询所有用户信息
        List<User> userList = userService.findAll();
        int count = 0;
        int stopedCount = 0;
        if(userList.size() > 0){
            for (User user:userList) {
                if("1".equals(user.getStatus())){
                    logger.info("已停止对" + user.getUserName() + "发送邮件");
                    stopedCount++;
                    continue;
                }
                WeatherRecord weatherRecord =  this.queryCityWeather(user.getAdcode());//查询天气信息
                int currentCount = this.sendMail(weatherRecord,user);//发送天气邮件
                count = count + currentCount;
            }
        }
        logger.info("邮件发送任务完成，共" + userList.size() + "个用户，冻结用户" + stopedCount + "人，应对" + (userList.size() - stopedCount) +
                    "个用户发送邮件，实际成功发送" + count + "人,发送失败" +  (userList.size() - stopedCount - count) + "个。");
    }

    private WeatherRecord queryCityWeather(String adcode){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String datetime = sdf.format(date);
        return  weatherRecordService.queryWeatherByCodeAndTime(adcode,datetime);
    }

    private int sendMail(WeatherRecord weatherRecord,User user) {
        Context context = new Context();

        //天气信息
        context.setVariable("province",weatherRecord.getProvince());
        context.setVariable("cityName",weatherRecord.getCity());
        context.setVariable("temp",weatherRecord.getTemperature());
        context.setVariable("weather",weatherRecord.getWeather());
        context.setVariable("windDirection",weatherRecord.getWinddirection());
        context.setVariable("windPower",weatherRecord.getWindpower());
        context.setVariable("humidity",weatherRecord.getHumidity());
        context.setVariable("userName",user.getUserName());

        //生成建议
        SuggestVO suggestVO = this.suggest(weatherRecord);
        context.setVariable("wear",suggestVO.getWear());
        context.setVariable("umbrella",suggestVO.getUmbrella());
        context.setVariable("washcar",suggestVO.getWashcar());

        String emailContent = templateEngine.process("emailTemplate", context);
        return this.sendHtmlMail(user.getEmail(),"EasyWeather-每日天气预报",emailContent,user);
    }


    public int sendHtmlMail(String to, String subject, String contnet,User user) {
        MimeMessage message = mailSender.createMimeMessage();
        try{
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(contnet, true);
            helper.setFrom(new InternetAddress(from,"EasyWeather","UTF-8"));
            mailSender.send(message);
            logger.info("已发送邮件至"+ user.getUserName());
            return 1;
        }
        catch (Exception e){
            e.printStackTrace();
            return 0;
        }
    }




    private SuggestVO suggest(WeatherRecord weatherRecord){
        String weather = weatherRecord.getWeather();
        SuggestVO suggestVO = new SuggestVO();
        int temp = Integer.parseInt(weatherRecord.getTemperature());

        //洗车和是否带伞建议
        if(weather.equals(WeatherEnum.RAINY)){
            suggestVO.setWashcar("有雨，不适应洗车。");
            suggestVO.setUmbrella("建议带伞。");
        }else if(weather.equals(WeatherEnum.SAND)){
            suggestVO.setWashcar("有扬沙，不适应洗车。");
            suggestVO.setUmbrella("建议带伞。");
        }else {
            suggestVO.setWashcar("适宜洗车，随时关注天气变化。");
            suggestVO.setUmbrella("无需带伞。");
        }

        //穿衣和出行建议
        if(temp >= 40){
            suggestVO.setWear("停止户外露天作业，对老弱病幼人群采取保护措施。");
        }
        if(temp >= 37 && temp < 40){
            suggestVO.setWear("避免在高温时段进行户外活动，进行必要的防暑降温措施。");
        }
        if(temp >= 35 && temp < 37){
            suggestVO.setWear("天气闷热，适宜着丝麻、轻棉的短衣、短裙、短裤等。");
        }
        if(temp >= 33 && temp < 35){
            suggestVO.setWear("天气炎热，适宜短衫、短裙、短裤等夏季装。");
        }
        if(temp >= 28 && temp < 33){
            suggestVO.setWear("天气较热，适宜棉麻面料的衬衫、薄长裙、薄T恤等夏季装。");
        }
        if(temp >= 25 && temp < 28){
            suggestVO.setWear("天气偏热，适宜着短衫、短裙、T恤等夏季服装。");
        }
        if(temp >= 23 && temp < 25){
            suggestVO.setWear("天气暖和，适宜着单层棉麻短套装、T恤衫、薄牛仔衫裤、休闲服等春秋国度装。");
        }
        if(temp >= 21 && temp < 23){
            suggestVO.setWear("天气温暖，适宜着长袖衬衫加单裤、薄型绵衫等春秋过渡装。");
        }
        if(temp >= 18 && temp < 21){
            suggestVO.setWear("天气温和，适宜着单层薄衫裤、薄型棉纱、长裤、长袖T恤等春秋过渡装。");
        }
        if(temp >= 15 && temp < 18){
            suggestVO.setWear("天气温凉，适宜着马甲衬衫、长裤、羊毛衫等春秋服装。");
        }
        if(temp >= 13 && temp < 15){
            suggestVO.setWear("天气微凉，适宜着一件羊毛衫、夹克衫、西服套装等春秋服装。");
        }
        if(temp >= 11 && temp < 13){
            suggestVO.setWear("天气较凉，适宜着厚外套加毛衣、大衣、西服套装等春秋服装。");
        }
        if(temp >= 8 && temp < 11){
            suggestVO.setWear("天气凉，适宜着一到两件羊毛衫、大衣、皮夹克等春秋着装。");
        }
        if(temp >= 5 && temp < 8){
            suggestVO.setWear("天气微冷，适宜着毛衣、风衣、大衣、皮夹克、外套等厚型春秋装。");
        }
        if(temp >= 0 && temp < 5){
            suggestVO.setWear("天气较冷，适宜着薄棉衣、皮夹克加羊毛衫等冬季服装。");
        }
        if(temp >= -5 && temp < 0){
            suggestVO.setWear("天气冷，冬季着装：棉衣、羽绒衣、冬大衣、皮夹克等。");
        }
        if(temp >= -10 && temp < -5){
            suggestVO.setWear("天气寒冷，适宜着薄棉衣、皮夹克加羊毛衫、厚呢外套、呢帽、手套等冬季服装。");
        }
        if(temp < -10){
            suggestVO.setWear("温度极低，尽量少外出；建议着厚棉衣、厚羽绒服、冬大衣、裘皮大衣、棉帽、棉皮手套、棉皮靴等隆冬着装。");
        }
        return suggestVO;
    }




}
