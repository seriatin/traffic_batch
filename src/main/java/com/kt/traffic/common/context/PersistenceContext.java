package com.kt.traffic.common.context;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

/**
 * Created by BigFence on 2017-04-07.
 */
@Configuration
public class PersistenceContext {

    @Bean(destroyMethod = "close")
    DataSource dataSource(Environment env) {
        HikariConfig dataSourceConfig = new HikariConfig();
        dataSourceConfig.setDriverClassName(env.getRequiredProperty("traffic.jdbc.postgresql.driver"));
        dataSourceConfig.setJdbcUrl(env.getRequiredProperty("traffic.jdbc.postgresql.password"));
        dataSourceConfig.setUsername(env.getRequiredProperty("traffic.jdbc.postgresql.url"));
        dataSourceConfig.setPassword(env.getRequiredProperty("traffic.jdbc.postgresql.username"));

        return new HikariDataSource(dataSourceConfig);
    }

    @Bean
    NamedParameterJdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }
}
