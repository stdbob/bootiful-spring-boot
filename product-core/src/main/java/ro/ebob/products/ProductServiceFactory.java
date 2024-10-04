package ro.ebob.products;

import java.io.File;
import java.util.Objects;

import static ro.ebob.products.Config.Type.FILE;

public class ProductServiceFactory {

  private ProductServiceFactory() {
  }

  public static ProductService service(Config.Input input) {
    if (Objects.requireNonNull(input.type()) == FILE) {
      return new ProductServiceFile(new File(input.location()));
    }
    return new ProductServiceFile(new File("products.json"));

  }
}
