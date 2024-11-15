package ro.ebob.product.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import ro.ebob.product.service.Config;

@ConfigurationProperties("service")
public class ApiProperties {

  private Config.Input input;

  public Config.Input getInput() {
    return input;
  }

  public void setInput(Config.Input input) {
    this.input = input;
  }
}
