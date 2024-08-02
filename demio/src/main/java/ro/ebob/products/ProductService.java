package ro.ebob.products;

import ro.ebob.products.model.Product;

import java.util.List;

public class ProductService {

  private final ProductMapper mapper;
  private final ProductRepository repository;

  public ProductService(ProductRepository repository,ProductMapper mapper) {
    this.mapper = mapper;
    this.repository = repository;
  }

  public List<Product> products() {
    return repository.products().stream().map(mapper).toList();
  }
}
