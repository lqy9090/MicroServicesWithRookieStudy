package com.quinn.customer;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static javax.security.auth.callback.ConfirmationCallback.OK;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2023/1/3 20:20
 **/

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerHttpRequestTest {

    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void registryCustomerShouldPass() {
        //given
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "lu",
                "qiuyi",
                "qiuyi@gmail.com"
        );

        //when
        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/v1/customers/register",
                request,
                String.class
        );
        //then
        System.out.println("reponse: " + response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }
}
