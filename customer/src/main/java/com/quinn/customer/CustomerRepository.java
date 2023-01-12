package com.quinn.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2022/12/22 14:34
 **/
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(nativeQuery = true, value ="SELECT c.id, c.first_name, c.last_name, c.email FROM Customer c where c.email = ?1")
    Customer getCustomerByEmail(String email);
}
