package ro.ebob.product.service;

import ro.ebob.product.model.Product;

import java.io.File;
import java.util.List;

class ProductServiceFile implements ProductService {

  private final ProductMapper mapper;
  private final ProductRepository repository;

  ProductServiceFile(File file) {
    this(new ProductRepository(file), new ProductMapper(ProductServiceFactory.objectMapper()));
  }

  ProductServiceFile(ProductRepository repository, ProductMapper mapper) {
    this.mapper = mapper;
    this.repository = repository;
  }

  @Override
  public List<Product> products() {
    return repository.products().stream().map(mapper).toList();
  }
}
