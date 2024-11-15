package ro.ebob.product;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Api {

  public static void main(String[] args) {
    SpringApplication application = new SpringApplication(Api.class);
    application.setBannerMode(Banner.Mode.OFF);
    application.run(args);
  }

  @RequestMapping("/")
  public String home() {
    return "Hello World!";
  }
}
