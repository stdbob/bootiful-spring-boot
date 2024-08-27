package ro.ebob.products;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceTest {

  private static ProductService productService;

  @BeforeAll
  static void setAllUp() throws IOException {

//    ProductRepository productRepository = new ProductRepository(Paths.get(ProductServiceTest.class.getClassLoader().getResource("products.json").toURI()));
//
//    ObjectMapper objectMapper = new JsonConfig().objectMapper();
//    ProductMapper productMapper = new ProductMapper(objectMapper);


    System.setProperty("input.location", Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("products.json")).getFile());
    Config config = new Config();

//    Config.Input input = config.input();
//    productService = ProductServiceFactory.service(new Config.Input(Config.Type.FILE, Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("products.json")).getFile()));
    productService = ProductServiceFactory.service(config.input());
  }

  @Test
  void testService() {
    assertThat(productService.products()).isNotEmpty();
  }
}