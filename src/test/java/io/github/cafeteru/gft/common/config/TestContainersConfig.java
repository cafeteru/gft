package io.github.cafeteru.gft.common.config;

import jakarta.annotation.PreDestroy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@Slf4j
@TestConfiguration
public class TestContainersConfig {

  private static final PostgreSQLContainer<?> postgresContainer =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:13"))
          .withDatabaseName("test_db")
          .withUsername("test_user")
          .withPassword("test_pass");

  static {
    postgresContainer.setPortBindings(List.of("5433:5432"));
    postgresContainer.start();
    waitForDatabaseToBeReady();
  }

  private static void waitForDatabaseToBeReady() {
    try (final Connection connection = DriverManager.getConnection(
        postgresContainer.getJdbcUrl(),
        postgresContainer.getUsername(),
        postgresContainer.getPassword())) {
      if (!connection.isValid(5)) {
        throw new IllegalStateException("Database connection is not valid");
      }
    } catch (SQLException e) {
      throw new RuntimeException("Failed to connect to the database", e);
    }
  }

  @PreDestroy
  public void stopContainers() {
    postgresContainer.stop();
  }
}

