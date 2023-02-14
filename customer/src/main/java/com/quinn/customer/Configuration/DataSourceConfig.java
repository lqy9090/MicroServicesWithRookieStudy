package com.quinn.customer.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2023/2/7 16:23
 **/
//@Configuration
public class DataSourceConfig {
    /**
     * DataSource 配置
     * @Primary指定当前数据库是主库
     */
    @ConfigurationProperties(prefix = "spring.datasource.pgsql")
    @Bean(name = "pgsqlDataSource")
    public DataSource pgsqlDataSource() throws SQLException {
        return DataSourceBuilder.create().build();
    }

    /**
     * DataSource 配置
     */
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.h2")
    @Bean(name = "h2DataSource")
    public DataSource h2DataSource() throws SQLException {
        return DataSourceBuilder.create().build();
    }
}
