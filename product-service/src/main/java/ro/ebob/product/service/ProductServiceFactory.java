package ro.ebob.product.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static ro.ebob.product.service.Config.Type.FILE;

public class ProductServiceFactory {

  private static final Logger logger = Logger.getLogger(ProductServiceFactory.class.getName());

  private ProductServiceFactory() {
  }

  public static ProductService service(Config.Input input) {
    if (Objects.requireNonNull(input.type()) == FILE) {
      return new ProductServiceFile(new File(input.location()));
    }
    return new ProductServiceFile(new File("products.json"));
  }

  public static ObjectMapper objectMapper() {
    return JsonMapper.builder()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .addModule(new JavaTimeModule())
        .build();
  }

  public static String[] features() {
    Parameter[] parameters = Config.Input.class.getConstructors()[0].getParameters();
    String[] names = new String[parameters.length];
    for (int i=0;i<parameters.length;i++) {
      Parameter p = parameters[i];
      if(p.getType().isEnum()) {
        logger.info(() -> p.getType().getEnumConstants() == null ? "n/a" : Arrays.toString(p.getType().getEnumConstants()));
      }
      String propertyName = String.format("%s.%s", Config.Input.class.getSimpleName().toLowerCase(), parameters[i].getName().toLowerCase());
      names[i]=propertyName;
    }
    return names;
  }

  public static Config.Input fromProperties() throws IOException {
    Properties properties = new Properties();
    properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("products.properties"));
    Parameter[] parameters = Config.Input.class.getConstructors()[0].getParameters();
    String[] names = new String[parameters.length];
    for (int i=0;i<parameters.length;i++) {
      String propertyName = String.format("%s.%s", Config.Input.class.getSimpleName().toLowerCase(), parameters[i].getName().toLowerCase());
      Object value = properties.getProperty(propertyName, System.getProperty(propertyName));
      logger.log(Level.INFO, "{0} = {1} ({2})", new Object[]{propertyName, value, parameters[i].getType().getEnumConstants() == null ? "string" : Arrays.stream(parameters[i].getType().getEnumConstants()).collect(Collectors.toSet())});
      names[i]=propertyName;
    }
    return new Config.Input(
        Config.Type.valueOf(properties.getProperty(names[0], System.getProperty(names[0], "file")).toUpperCase()),
        Objects.requireNonNull(properties.getProperty(names[1], System.getProperty(names[1])))
    );
  }
}
