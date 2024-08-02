package ro.ebob.input.model;

import java.math.BigDecimal;

public record Product(Long id, String title, String category, Integer stock, BigDecimal price, Meta meta) {
}
