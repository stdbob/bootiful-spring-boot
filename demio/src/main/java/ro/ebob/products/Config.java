package ro.ebob.products;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class Config {

//    public enum Feature {
//        INPUT("type", "location");
//
//        private String type;
//        private String location;
//        Feature(String type, String location) {
//            this.type = type;
//            this.location = location;
//        }
//
//        public String property() {
//            return String.format("%s.%s", type, location);
//        }
//    }

//    public static final String INPUT_TYPE = "input.type";
//    public static final String INPUT_LOCATION = "input.location";

    public enum Type { FILE }
    public record Input(Type type, String location){}

    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .addModule(new JavaTimeModule())
            .build();
    }

    public String[] features() {
        Parameter[] parameters = Input.class.getConstructors()[0].getParameters();
        String[] names = new String[parameters.length];
        for (int i=0;i<parameters.length;i++) {
            Parameter p = parameters[i];
            if(p.getType().isEnum()) {
                System.out.println(p.getType().getEnumConstants());
            }
            String propertyName = String.format("%s.%s", Input.class.getSimpleName().toLowerCase(), parameters[i].getName().toLowerCase());
            names[i]=propertyName;
        }
        return names;
    }

    public Input input() throws IOException {
        Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("products.properties"));
        Parameter[] parameters = Input.class.getConstructors()[0].getParameters();
        String[] names = new String[parameters.length];
        for (int i=0;i<parameters.length;i++) {
            String propertyName = String.format("%s.%s", Input.class.getSimpleName().toLowerCase(), parameters[i].getName().toLowerCase());
            Object value = properties.getProperty(propertyName, System.getProperty(propertyName));
            System.out.printf("Property: %s = %s (%s)%n", propertyName, value, parameters[i].getType().getEnumConstants() == null ? "string" : Arrays.stream(parameters[i].getType().getEnumConstants()).collect(Collectors.toSet()));
            names[i]=propertyName;
        }
        return new Input(
          Type.valueOf(properties.getProperty(names[0], System.getProperty(names[0], "file")).toUpperCase()),
            Objects.requireNonNull(properties.getProperty(names[1], System.getProperty(names[1])))
        );
    }
}
