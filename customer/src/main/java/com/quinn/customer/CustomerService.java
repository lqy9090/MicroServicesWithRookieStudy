package com.quinn.customer;

import com.quinn.customer.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;
import java.util.Optional;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2022/12/21 15:31
 **/

@Service
public record CustomerService(CustomerRepository repository, RestTemplate restTemplate) {
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

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

    Customer getCustomer(Long id){
        return repository.findById(id).orElseThrow(() -> {
            NotFoundException notFoundException = new NotFoundException(
                    "customer with id " + id + " not found");

            LOGGER.error("Get customer {}", id, notFoundException);
            return notFoundException;
        });
    }

    public void updateCustomer(Customer customer){
        Optional<Customer> optionalCustomer = repository.findById(customer.getId());
        if (optionalCustomer.isEmpty()) {
            throw new NotFoundException(
                    "Customer  " + customer.getId() + " not exist!");
        }

        optionalCustomer.ifPresent(customer1 -> {
            customer1.setEmail(customer.getEmail());
            customer1.setFirstName(customer.getLastName());
            customer1.setLastName(customer.getLastName());

            repository.save(customer1);
        });
    }

    public void deleteCustomer(Long CustomerId) throws NotFoundException {
        if(!repository.existsById(CustomerId)) {
            throw new NotFoundException(
                    "Customer with id " + CustomerId + " does not exists");
        }
        repository.deleteById(CustomerId);
    }

//    //
//    public List<Customer> getCustomersByConditionWithMethodName(ClassicCustomersRequest request) {
//        System.out.println("getCustomers condition: " + request);
//        List<Customer> customers = customerRepository.findCustomersByNameAndEmailOrderByIdDesc(request.getName(), request.getEmail());
//        return customers;
//    }
//
//    public List<Customer> getCustomersByConditionWithJPQL(ClassicCustomersRequest request) {
//        System.out.println("getCustomers condition: " + request);
//        List<Customer> customers = customerRepository.findCustomersByConditionOrderByIdDesc(request.getName(), request.getEmail());
//        return customers;
//    }
//
//    public List<Customer> getCustomersByConditionWithNativeSQL(ClassicCustomersRequest request) {
//        System.out.println("getCustomers condition: " + request);
//        List<Customer> customers = customerRepository.findCustomersByConditionOrderByIdDesc2(request.getName(), request.getEmail());
//        return customers;
//    }
}
