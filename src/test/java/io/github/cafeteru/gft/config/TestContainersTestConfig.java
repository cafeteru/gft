package io.github.cafeteru.gft.config;

import jakarta.annotation.PreDestroy;
import javax.sql.DataSource;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestContainersTestConfig {

  private static final PostgreSQLContainer<?> postgresContainer =
      new PostgreSQLContainer<>("postgres:latest")
          .withDatabaseName("test_db")
          .withUsername("test_user")
          .withPassword("test_pass");

  static {
    postgresContainer.start();
  }

  @Bean
  public DataSource dataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.postgresql.Driver");
    dataSource.setUrl(postgresContainer.getJdbcUrl());
    dataSource.setUsername(postgresContainer.getUsername());
    dataSource.setPassword(postgresContainer.getPassword());
    return dataSource;
  }

  @PreDestroy
  public void stopContainer() {
    postgresContainer.stop();
  }
}
