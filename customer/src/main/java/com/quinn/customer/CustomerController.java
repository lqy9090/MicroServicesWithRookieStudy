package com.quinn.customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

import java.net.http.HttpTimeoutException;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2022/12/21 15:25
 **/

@Slf4j
@RestController
@RequestMapping("api/v1/customers")
public record CustomerController(CustomerService customerService) {

    @PostMapping("/register")
    public void RegistryCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest) throws Exception {
        log.info("new customer registration {}", customerRegistrationRequest);
        customerService.RegisterCustomer(customerRegistrationRequest);
    }

}
