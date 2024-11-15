package ro.ebob;

import ro.ebob.product.Config;
import ro.ebob.product.service.ProductService;
import ro.ebob.product.service.ProductServiceFactory;

import java.util.Objects;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String location = Objects.requireNonNull(Main.class.getClassLoader().getResource("products.json")).getFile();
        if(args.length > 0) {
            location = args[0];
        }
        ProductService productService = ProductServiceFactory.service(new Config.Input(Config.Type.FILE, location));
        productService.products().forEach(System.out::println);
    }
}