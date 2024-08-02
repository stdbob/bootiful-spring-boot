package ro.ebob.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ro.ebob.input.model.Product;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceTest {

  private static ObjectMapper objectMapper;

  @BeforeAll
  static void setAllUp() {
    objectMapper = new JsonConfig().objectMapper();
  }

  @Test
  void testModel() throws IOException, URISyntaxException {
    ProductService productService = new ProductService(objectMapper);
    List<Product> products = productService.products(this.getClass().getClassLoader().getResource("products.json"), "products");
    assertThat(products).hasSizeGreaterThan(0);

    List<String> items = productService.productsJson(this.getClass().getClassLoader().getResource("products.json"), "products");

    System.out.println(items.get(0));

    Path outPath = Paths.get("out.txt");
    Files.deleteIfExists(outPath);
    Base64.Encoder encoder = Base64.getEncoder();
    for(Iterator<String> it = items.iterator();it.hasNext();){
      Files.write(outPath, encoder.encode(it.next().getBytes(StandardCharsets.UTF_8)), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
      Files.write(outPath, System.lineSeparator().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

//
//    try(Base64Writer writer = new Base64Writer(new FileOutputStream("out.txt"))){
//      for(Iterator<String> it = items.iterator();it.hasNext();){
//        writer.write(it.next().getBytes(StandardCharsets.UTF_8));
//        writer.write(System.lineSeparator().getBytes(StandardCharsets.UTF_8));
//        writer.flush();
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }

    Base64.Decoder decoder = Base64.getDecoder();
    try(BufferedReader reader = new BufferedReader(new FileReader(outPath.toFile()))) {
      String line;
      while((line = reader.readLine()) != null){
        System.out.println(line);
        System.out.println(new String(decoder.decode(line)));
      }
    }
  }

}