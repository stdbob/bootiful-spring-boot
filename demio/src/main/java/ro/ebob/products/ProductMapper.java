package ro.ebob.products;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ro.ebob.products.model.Product;

import java.util.function.Function;

class ProductMapper implements Function<String, Product> {

  private final ObjectMapper mapper;

  public ProductMapper(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public Product apply(String str) {
    try {
      return mapper.readValue(str, Product.class);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException(e);
    }
  }
}
