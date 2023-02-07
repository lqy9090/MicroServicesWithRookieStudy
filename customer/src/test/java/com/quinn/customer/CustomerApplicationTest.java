package com.quinn.customer;

import com.github.javafaker.Faker;
import com.quinn.customer.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2023/2/7 20:04
 **/
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CustomerApplicationTest {
    private CustomerService underTest;

    @Autowired
    private CustomerRepository customerRepository;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(3))
                .setReadTimeout(Duration.ofMillis(3))
                .build();
        underTest = new CustomerService(customerRepository, restTemplate);
    }

    @Test
    void getCustomer(){
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = String.format("%s.%s@study.edu", firstName, lastName);
        Customer fakeCustomer = new Customer(
                firstName,
                lastName,
                email
        );

        customerRepository.save(fakeCustomer);
        Customer customer = underTest.getCustomer(1L);
        System.out.println(customer);

        assertEquals(firstName, fakeCustomer.getFirstName() );
        assertEquals(lastName, fakeCustomer.getLastName() );
        assertEquals(email, customer.getEmail() );
    }

    @Test
    void shouldUpdateCustomerSuccess(){
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = String.format("%s.%s@study.edu", firstName, lastName);
        Customer fakeCustomer = new Customer(
                firstName,
                lastName,
                email
        );
        customerRepository.save(fakeCustomer);

        Customer customer = underTest.getCustomer(fakeCustomer.getId());
        System.out.println("create: " + customer);

        Customer aliUpdated = new Customer(customer.getId(), "updatedFirstName", "aliUpdated", fakeCustomer.getEmail());

        underTest.updateCustomer(aliUpdated);

        Optional<Customer> customerUpdated = customerRepository.findById(fakeCustomer.getId());
        System.out.println("update: "+customerUpdated);
    }

    @Test
    void shouldDeleteCustomerSuccess() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = String.format("%s.%s@study.edu", firstName, lastName);
        Customer fakeCustomer = new Customer(
                firstName,
                lastName,
                email
        );
        customerRepository.save(fakeCustomer);

        Customer customer = underTest.getCustomer(fakeCustomer.getId());
        System.out.println("create: " + customer);

        underTest.deleteCustomer(customer.getId());

        assertThatThrownBy(()->{
            underTest.getCustomer(fakeCustomer.getId());
        })
                .isInstanceOf(NotFoundException.class)
                .hasMessage( "customer with id " + customer.getId() + " not found");

    }

    @Test
    void shouldReturnCustomersWithConditionImplementByMethodName() {
        Faker faker = new Faker();
        for (int i = 0; i < 20; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@study.edu", firstName, lastName);
            Customer customer = new Customer(firstName, lastName, email);
            customerRepository.save(customer);
        }

        ClassicCustomersRequest request = new ClassicCustomersRequest(
                "ali",
                "judy",
                "study.edu"
        );

//        List<Customer> customersByCondition = underTest.getCustomersByConditionWithMethodName(request);
//        System.out.println(customersByCondition);
    }

    @Test
    void shouldReturnCustomersWithConditionImplementByJPQL() {
        Customer ali = new Customer(
                1L,
                "ali",
                "ali@ssword",
                "ali@gmail.com"
        );
        Customer ali2 = new Customer(
                2L,
                "ali",
                "ali@ssword",
                "ali@gmail.com"
        );
        customerRepository.saveAll(Arrays.asList(ali, ali2));

//        ClassicCustomersRequest request = new ClassicCustomersRequest(
//                "ali",
//                "ali@gmail.com"
//        );
//
//        List<Customer> customersByCondition = underTest.getCustomersByConditionWithJPQL(request);
//        System.out.println(customersByCondition);
    }

    @Test
    void shouldReturnCustomersWithConditionImplementByNativeSQL() {
        Customer ali = new Customer(
                1L,
                "ali",
                "ali@ssword",
                "ali@gmail.com"
        );
        Customer ali2 = new Customer(
                2L,
                "ali",
                "ali@ssword",
                "ali@gmail.com"
        );
        customerRepository.saveAll(Arrays.asList(ali, ali2));

//        ClassicCustomersRequest request = new ClassicCustomersRequest(
//                "ali",
//                "ali@gmail.com"
//        );
//
//        List<Customer> customersByCondition = underTest.getCustomersByConditionWithNativeSQL(request);
//        System.out.println(customersByCondition);
    }



}
