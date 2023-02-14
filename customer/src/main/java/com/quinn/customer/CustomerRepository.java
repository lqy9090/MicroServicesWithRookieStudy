package com.quinn.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2022/12/22 14:34
 **/
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query(nativeQuery = true,
            value ="SELECT c.id, c.first_name, c.last_name, c.email FROM Customer c where c.email = ?1")
    Customer getCustomerByEmail(String email);

    List<Customer> findCustomersByFirstNameIsContainingAndEmailIsContainingOrderByIdDesc(String firstName, String email);

    @Query(value = "FROM Customer c WHERE c.firstName like %?1 and c.email like %?2  order by c.id desc ")
    List<Customer> findCustomersByConditionOrderByIdDesc(String firstName, String email);

    @Query(nativeQuery = true,
            value = "SELECT * FROM customer c WHERE c.first_name like %?1 and c.email like %?2 order by c.id desc")
    List<Customer> findCustomersByConditionOrderByIdDesc2(String firstName, String email);

    @Query(nativeQuery = true,
            value = "UPDATE Customer c SET c.email = ?1 WHERE c.id = ?2")
    void updateCustomerEmailById(String email, Long id);
}
