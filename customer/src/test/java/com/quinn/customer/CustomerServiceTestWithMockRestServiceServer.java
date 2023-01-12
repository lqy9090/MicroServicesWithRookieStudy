package com.quinn.customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpRetryException;
import java.net.URI;
import java.net.http.HttpTimeoutException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2023/1/5 16:49
 **/
@ExtendWith(SpringExtension.class)
@SpringBootTest
class CustomerServiceTestWithMockRestServiceServer {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CustomerService underTest;
    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void registerCustomer() throws JsonProcessingException {
        //given
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "lu",
                "qiuyi",
                "qiuyi@gmail.com"
        );

        mockServer.expect(
                        requestTo("http://localhost:8081/api/v1/fraud-check/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(new FraudCheckResponse(false)))
                );

        //when
        underTest.RegisterCustomer(request);
        mockServer.verify();

    }

    @Test
    void registerCustomerShouldCatchTimoutException(){
        //given
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "lu",
                "qiuyi",
                "qiuyi@gmail.com"
        );

        mockServer.expect(
                        requestTo("http://localhost:8081/api/v1/fraud-check/1"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(request1 -> {
                    try {
                        Thread.sleep(TimeUnit.SECONDS.toMillis(5)); // Delay
                    } catch (InterruptedException e) {
                    }
                    return new MockClientHttpResponse("{result:'fail'}".getBytes(), HttpStatus.OK);
                });

        //when
        assertThatThrownBy(() -> underTest.RegisterCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Request is timeout");

        mockServer.verify();

    }
}