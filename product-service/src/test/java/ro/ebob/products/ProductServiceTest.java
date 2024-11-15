package ro.ebob.products;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ro.ebob.product.service.Config;
import ro.ebob.product.service.ProductService;
import ro.ebob.product.service.ProductServiceFactory;

import java.io.IOException;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceTest {

  private static ProductService productService;

  @BeforeAll
  static void setAllUp() throws IOException {
    System.setProperty("input.location", Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("products.json")).getFile());
    Config.Input input = ProductServiceFactory.fromProperties();
    productService = ProductServiceFactory.service(input);
  }

  @Test
  void testService() {
    assertThat(productService.products()).isNotEmpty();
  }
}