package com.quinn.customer;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2022/12/21 15:31
 **/

@Service
public record CustomerService(CustomerRepository repository, RestTemplate restTemplate) {

    public void RegisterCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        repository.saveAndFlush(customer);

        try {
            FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
                    "http://localhost:8082/api/v1/fraud-check/{customerId}",
                    FraudCheckResponse.class,
                    customer.getId()
            );

            if (fraudCheckResponse.isFraudulent()) {
                throw new IllegalStateException("Request is fraudulent");
            }
        } catch (ResourceAccessException e) { //重试机制
            if (e.getCause() instanceof SocketTimeoutException)
                throw new IllegalStateException("Request is timeout");

        }
    }
}
