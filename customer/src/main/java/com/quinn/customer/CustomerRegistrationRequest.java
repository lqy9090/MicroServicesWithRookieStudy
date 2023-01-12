package com.quinn.customer;

import lombok.Data;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2022/12/21 15:28
 **/

public record CustomerRegistrationRequest(
        String firstName,
        String lastName,
        String email
) {
}
