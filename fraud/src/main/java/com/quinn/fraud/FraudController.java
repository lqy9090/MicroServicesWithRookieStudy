package com.quinn.fraud;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2022/12/22 16:09
 **/
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/fraud-check")
public class FraudController {
    private final FraudCheckService fraudCheckService;

    @GetMapping(path = "{customerId}")
    public FraudCheckResponse isFraudster(@PathVariable("customerId") Long id) {
        boolean isFraudulentCustomer = fraudCheckService.isFraudulentCustomer(id);

        return new FraudCheckResponse(isFraudulentCustomer);
    }
}
