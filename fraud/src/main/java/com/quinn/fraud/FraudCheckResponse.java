package com.quinn.fraud;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2022/12/22 16:18
 **/

public record FraudCheckResponse(
         boolean isFraudulent
) {

}
