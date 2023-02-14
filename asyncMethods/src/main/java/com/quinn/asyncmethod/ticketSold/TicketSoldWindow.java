package com.quinn.asyncmethod.ticketSold;

import com.quinn.asyncmethod.AppRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2023/2/9 14:29
 **/
@Service
public class TicketSoldWindow {
    private static final Logger logger = LoggerFactory.getLogger(TicketSoldWindow.class);

    public TicketSoldWindow() {
    }

    @Async
    public void soldTicket(Integer number, Ticket ticket) {
        while (ticket.isFlag()) {
            ticket.ticket(number);
        }
    }


}
