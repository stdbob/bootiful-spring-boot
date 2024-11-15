package ro.ebob.product.service;

public class Config {

    public enum Type { FILE }
    public record Input(Type type, String location){}
}
