package com.quinn.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2023/1/9 11:23
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassicCustomersRequest{
    private String firstName;
    private String lastName;
    private String email;


}
