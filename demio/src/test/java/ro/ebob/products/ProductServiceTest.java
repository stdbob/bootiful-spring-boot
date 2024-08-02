package ro.ebob.products;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ro.ebob.products.config.JsonConfig;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceTest {

  private static ProductService productService;

  @BeforeAll
  static void setAllUp() throws URISyntaxException {

    ProductRepository productRepository = new ProductRepository(Paths.get(ProductServiceTest.class.getClassLoader().getResource("products.json").toURI()));

    ObjectMapper objectMapper = new JsonConfig().objectMapper();
    ProductMapper productMapper = new ProductMapper(objectMapper);
    productService = new ProductService(productRepository, productMapper);
  }

  @Test
  void testService() {
    assertThat(productService.products()).isNotEmpty();
  }
}