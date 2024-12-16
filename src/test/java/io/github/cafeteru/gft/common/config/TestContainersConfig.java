package io.github.cafeteru.gft.common.config;

import jakarta.annotation.PreDestroy;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@Slf4j
@TestConfiguration
public class TestContainersConfig {

  private static final PostgreSQLContainer<?> postgresContainer =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
          .withDatabaseName("test_db")
          .withUsername("test_user")
          .withPassword("test_pass");

  static {
    postgresContainer.setPortBindings(List.of("5433:5432"));
    postgresContainer.start();
  }

  @PreDestroy
  public void stopContainers() {
    postgresContainer.stop();
  }
}

