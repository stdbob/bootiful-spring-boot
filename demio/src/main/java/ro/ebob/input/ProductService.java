package ro.ebob.input;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import ro.ebob.input.model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

  private final ObjectMapper mapper;

  public ProductService(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  public List<Product> products(URL productsSource, String fieldName) throws IOException {
    JsonNode node = fieldName != null ? mapper.readTree(productsSource).get("products") : mapper.readTree(productsSource);
    return mapper.treeToValue(node, new TypeReference<List<Product>>(){});
  }

  public List<String> productsJson(URL productsSource, String fieldName) throws IOException {
    JsonNode node = fieldName != null ? mapper.readTree(productsSource).get("products") : mapper.readTree(productsSource);
    ArrayNode arrayNode = (ArrayNode) node;
    List<String> results = new ArrayList<>(arrayNode.size());
    for(int i = 0; i < arrayNode.size(); i++) {
      results.add(mapper.writeValueAsString(arrayNode.get(i)));
    }
    return results;
  }
}
