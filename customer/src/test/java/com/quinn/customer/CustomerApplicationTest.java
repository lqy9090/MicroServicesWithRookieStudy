package com.quinn.customer;

import com.alibaba.fastjson.JSON;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2023/1/3 20:20
 **/

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerApplicationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void registryCustomerShouldPass() throws Exception {
        //given
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "lu",
                "qiuyi",
                "qiuyi@gmail.com"
        );
        JSONObject json = new JSONObject();
        json.put("firstName", "john");
        json.put("lastName", "dan");
        json.put("email", "john@gmail.com");

        //when-then
            this.mockMvc.perform(post("/api/v1/customers/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content(json.toString()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
