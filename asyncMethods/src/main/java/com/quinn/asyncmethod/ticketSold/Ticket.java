package com.quinn.asyncmethod.ticketSold;

import lombok.Synchronized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2023/2/9 14:01
 **/
@Component
public class Ticket{
    private static final Logger logger = LoggerFactory.getLogger(Ticket.class);

    private int num;//票数量
    private boolean flag=true;//若为false则售票停止

    public Ticket() {
    }

    public Ticket(int num, boolean flag){
        this.num=num;
    }

    public int getNum() {
        return num;
    }

    public boolean isFlag() {
        return flag;
    }

    @Synchronized
    public void ticket(Integer winNumber){
        if(num<=0){
            flag=false;
            return;
        }
        try {
            Thread.sleep(200);//模拟延时操作
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        //输出当前窗口号以及出票序列号
        logger.info("Window: "+winNumber+" 售出票序列号："+num--);
    }
}
