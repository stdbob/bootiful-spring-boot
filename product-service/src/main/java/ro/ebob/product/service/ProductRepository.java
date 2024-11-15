package ro.ebob.product.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

class ProductRepository {

  private final File source;

  public ProductRepository(File file) {
    this.source = file;
  }

  public List<String> products() {
    try {
      return Files.readAllLines(source.toPath(), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public String products(int id) {
    return products().get(id);
  }

}
