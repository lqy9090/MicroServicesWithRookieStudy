package com.quinn.customer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;
import java.net.http.HttpTimeoutException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2023/1/5 16:08
 **/
@ExtendWith(MockitoExtension.class)
class CustomerServiceTestWithMockito {
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService underTest;

    @Test
    void RegistryCustomerSuccessAndIsNotFraudulent() {
        //given
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "lu",
                "qiuyi",
                "qiuyi@gmail.com"
        );
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        Mockito.when(customerRepository.saveAndFlush(customer)).thenReturn(customer);//打桩

        Mockito.when(restTemplate.getForObject(
                        "http://localhost:8081/api/v1/fraud-check/{customerId}",//打桩
                        FraudCheckResponse.class,
                        customer.getId()))
                .thenReturn(new FraudCheckResponse(false));

        //when
        underTest.RegisterCustomer(request);

        //then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).saveAndFlush(customerArgumentCaptor.capture());
        Customer captorValue = customerArgumentCaptor.getValue();

        assertThat(captorValue).isEqualTo(customer);
    }

    @Test
    void RegistryCustomerSuccessAndIsFraudulent() {
        //given
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "lu",
                "qiuyi",
                "qiuyi@gmail.com"
        );
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        Mockito.when(customerRepository.saveAndFlush(customer)).thenReturn(customer);//打桩

        Mockito.when(restTemplate.getForObject(
                        "http://localhost:8081/api/v1/fraud-check/{customerId}",//打桩
                        FraudCheckResponse.class,
                        customer.getId()))
                .thenReturn(new FraudCheckResponse(true));

        //when
        //then
        assertThatThrownBy(() -> underTest.RegisterCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Request is fraudulent");

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).saveAndFlush(customerArgumentCaptor.capture());
        Customer captorValue = customerArgumentCaptor.getValue();

        assertThat(captorValue).isEqualTo(customer);

    }

    @Test
    void registerCustomerShouldCatchTimoutException() {
        //given
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "lu",
                "qiuyi",
                "qiuyi@gmail.com"
        );
        Customer customer = Customer.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .build();

        Mockito.when(customerRepository.saveAndFlush(customer)).thenReturn(customer);//打桩

        Mockito.when(restTemplate.getForObject(
                        "http://localhost:8081/api/v1/fraud-check/{customerId}",//打桩
                        FraudCheckResponse.class,
                        customer.getId()))
                .thenCallRealMethod()
                .thenThrow(new ResourceAccessException("Read timed out", new SocketTimeoutException("Read timed out")));

        //when
        //then
        assertThatThrownBy(() -> underTest.RegisterCustomer(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Request is timeout");

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerRepository).saveAndFlush(customerArgumentCaptor.capture());
        Customer captorValue = customerArgumentCaptor.getValue();

        assertThat(captorValue).isEqualTo(customer);

    }
}