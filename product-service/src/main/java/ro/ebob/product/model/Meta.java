package ro.ebob.product.model;

import java.time.Instant;

public record Meta(Instant createdAt, Instant updatedAt, String barcode, String qrCode) {
}
