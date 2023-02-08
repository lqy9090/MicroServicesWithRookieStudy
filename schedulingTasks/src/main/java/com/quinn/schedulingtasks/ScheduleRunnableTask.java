package com.quinn.schedulingtasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2023/2/8 13:35
 **/
public class ScheduleRunnableTask implements Runnable{
    private String message;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public ScheduleRunnableTask(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        log.info(dateFormat.format(new Date())+" Runnable Task with "+message
                +" on thread "+Thread.currentThread().getName());
    }
}
