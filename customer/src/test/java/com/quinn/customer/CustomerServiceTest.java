package com.quinn.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;
import java.time.Duration;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2023/1/3 20:10
 **/
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerServiceTest {
    private CustomerService underTest;

    @Autowired
    private CustomerRepository customerRepository;

//    @Autowired
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .build();
        underTest = new CustomerService(customerRepository, restTemplate);
    }

    @Test
    void registerCustomerShouldPassAndFraudHistoryPass() {
        //given
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "lu",
                "qiuyi",
                "qiuyi@gmail.com"
        );
        //when
        underTest.RegisterCustomer(request);

        //then
        Customer expected = customerRepository.getCustomerByEmail("qiuyi@gmail.com");
        assertThat(expected.getFirstName()).isEqualTo("lu");
        assertThat(expected.getLastName()).isEqualTo("qiuyi");

    }

    @Test
    void registerCustomerShouldCatchTimoutException() {
        //given
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "lu",
                "qiuyi",
                "qiuyi@gmail.com"
        );

//        long startMillis = System.currentTimeMillis();
//
////        Throwable throwable = catchThrowable(() -> {
//            restTemplate.getForObject(
//                            "http://localhost:8081/api/v1/fraud-check/{customerId}",//打桩
//                            FraudCheckResponse.class,
//                            1L);
////                }
////        );
//        long endMillis = System.currentTimeMillis();
//        System.out.println("Execution time: " + (endMillis - startMillis));
//        assertThat(throwable).hasRootCauseInstanceOf(SocketTimeoutException.class);

        assertThatThrownBy(() -> underTest.RegisterCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Request is timeout");
    }

}