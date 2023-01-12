package com.quinn.fraud;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2022/12/22 16:04
 **/
public interface FraudCheckHistoryRepository extends JpaRepository<FraudCheckHistory, Long> {
}
