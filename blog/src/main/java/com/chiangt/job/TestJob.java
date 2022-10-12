package com.chiangt.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestJob {

    //每隔5秒执行一次
    @Scheduled(cron = "0/5 * * * * ?")
    public void testJob(){
//        System.out.println("定时任务测试");
    }
}
