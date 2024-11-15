package ro.ebob.product.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ro.ebob.product.service.ProductService;
import ro.ebob.product.model.Product;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping
  public List<Product> products() {
    return productService.products();
  }

  @GetMapping("/{id}")
  public Product products(@PathVariable(name = "id") Integer id) {
    return productService.products().get(id-1);
  }




}
