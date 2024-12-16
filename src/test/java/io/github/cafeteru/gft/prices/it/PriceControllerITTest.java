package io.github.cafeteru.gft.prices.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import io.github.cafeteru.gft.common.config.TestContainersConfig;
import io.github.cafeteru.gft.domain.model.PriceRS;
import io.github.cafeteru.gft.prices.infrastructure.adapter.in.api.dto.ErrorDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestContainersConfig.class)
@ActiveProfiles("test")
class PriceControllerITTest {

  private static final String GET_PRICE = "/prices/getPrice";

  private final int idProduct = 35455;
  private final int idBrand = 1;

  @LocalServerPort
  private int port;

  @BeforeEach
  void setUp() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = port;
  }

  @Test
  void when_get_invalid_date_should_return_bad_request() {
    final var result = RestAssured.given()
        .queryParams("applicationDate", "00.00")
        .queryParams("idProduct", idProduct)
        .queryParams("idBrand", idBrand)
        .get(GET_PRICE)
        .then()
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .extract()
        .body()
        .as(ErrorDto.class);
    assertEquals("Invalid LocalDateTime: 00.00", result.getMessage());
  }

  @Test
  void when_get_params_but_not_found_prices_should_return_no_content() {
    RestAssured.given()
        .queryParams("applicationDate", "2030-06-14-10.00.00")
        .queryParams("idProduct", idProduct)
        .queryParams("idBrand", idBrand)
        .get(GET_PRICE)
        .then()
        .statusCode(HttpStatus.NO_CONTENT.value());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "2020-06-14-14.59.59",
      "2020-06-14-18.30.01",
      "2020-06-14-23.59.59",
      "2020-06-15-11.00.01",
      "2020-06-15-15.59.59"
  })
  void when_getPrice_without_conflicts_should_return_one_result(String applicationDate) {
    final var result = RestAssured.given()
        .queryParams("applicationDate", applicationDate)
        .queryParams("idProduct", idProduct)
        .queryParams("idBrand", idBrand)
        .get(GET_PRICE)
        .then()
        .statusCode(HttpStatus.OK.value())
        .extract()
        .body()
        .as(PriceRS.class);
    assertEquals(35.50, result.getFinalPrice().floatValue());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "2020-06-14-15.00.00",
      "2020-06-14-18.30.00",
      "2020-06-15-00.00.00",
      "2020-06-15-11.00.00",
      "2020-06-15-16.00.00"
  })
  void when_getPrice_without_conflicts_should_return_the_highest_priority_result(
      String applicationDate) {
    final var result = RestAssured.given()
        .queryParams("applicationDate", applicationDate)
        .queryParams("idProduct", idProduct)
        .queryParams("idBrand", idBrand)
        .get(GET_PRICE)
        .then()
        .statusCode(HttpStatus.OK.value())
        .extract()
        .body()
        .as(PriceRS.class);
    assertNotEquals(35.50, result.getFinalPrice().floatValue());
  }
}
