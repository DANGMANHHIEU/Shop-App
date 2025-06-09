package com.example.shopapp.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class FlyWayConfig {

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    @Value("${spring.flyway.locations}")
    private String[] flywayLocations;

    @Bean
    public Flyway flyway(){
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource())
                .locations(flywayLocations)
                .baselineOnMigrate(true) //default baseline is V1
                .baselineVersion("0")
                .load();

        flyway.migrate(); // run .sql

        return flyway;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(datasourceUrl);
        dataSource.setUsername(datasourceUsername);
        dataSource.setPassword(datasourcePassword);
        return dataSource;
    }
}
