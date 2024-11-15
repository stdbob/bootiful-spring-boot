package ro.ebob.product.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ro.ebob.product.service.Config;
import ro.ebob.product.service.ProductService;
import ro.ebob.product.service.ProductServiceFactory;

import java.io.IOException;

@Configuration
@EnableConfigurationProperties(ApiProperties.class)
public class ApiConfig {

  @Bean
  public ProductService productService(ApiProperties properties) throws IOException {
    return ProductServiceFactory.service(new Config.Input(properties.getInput().type(), properties.getInput().location()));
  }

//  @Bean
//  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//    return args -> {
//      System.out.println("Let's inspect the beans provided by Spring Boot:");
//      String[] beanNames = ctx.getBeanDefinitionNames();
//      Arrays.sort(beanNames);
//      for (String beanName : beanNames) {
//        System.out.println(beanName);
//      }
//
//    };
//  }

}
