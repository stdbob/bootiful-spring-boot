package ro.ebob.output;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64Service {

  private final Base64.Encoder encoder;
  private final Base64.Decoder decoder;
  private final Charset charset;

  public Base64Service(Charset charset) {
    this.charset = charset;
    this.decoder = Base64.getDecoder();
    this.encoder = Base64.getEncoder();
  }

  public Base64Service() {
    this.charset = StandardCharsets.UTF_8;
    this.decoder = Base64.getDecoder();
    this.encoder = Base64.getEncoder();
  }

  public byte[] write(byte[] b, byte[] separator) {
    //byte[] separator = System.lineSeparator().getBytes(StandardCharsets.UTF_8);
    byte[] newB = new byte[separator.length + b.length] ;
    System.arraycopy(b, 0, newB, 0, b.length);
    System.arraycopy(separator, 0, newB, b.length, separator.length);
    return encoder.encode(newB);
  }

  public byte[] write(byte[] b) {
    return encoder.encode(b);
  }

  public byte[] write(String b, boolean addNewLine) {
    return addNewLine ? write(b.getBytes(charset))  : write(b.getBytes(charset), System.lineSeparator().getBytes(charset));
  }

  public String read(byte[] b) {
    return new String(decoder.decode(b), charset);
  }

  public String read(String b) {
    return read(b.getBytes(charset));
  }
}
