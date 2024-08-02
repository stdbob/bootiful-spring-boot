package ro.ebob.products;

import ro.ebob.products.model.Product;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class ProductRepository {

  private final Path source;

  public ProductRepository(Path source) {
    this.source = source;
  }

  public List<String> products() {
    try {
      return Files.readAllLines(source);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String products(int id) {
    return products().get(id);
  }

}
