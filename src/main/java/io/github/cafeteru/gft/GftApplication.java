package io.github.cafeteru.gft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(namedQueriesLocation = "classpath:queries.properties")
public class GftApplication {

	public static void main(String[] args) {
		SpringApplication.run(GftApplication.class, args);
	}

}
