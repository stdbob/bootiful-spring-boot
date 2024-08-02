package ro.ebob.output;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static org.assertj.core.api.Assertions.assertThat;


class Base64ServiceTest {

  private static Base64Service base64;

  @BeforeAll
  static void setAllUp() {
    base64 = new Base64Service();
  }

  @Test
  void testByteCopy() {
    byte[] abc = new byte[]{'a', 'b', 'c'};
    byte[] de = new byte[]{'d', 'e'};
    byte[] abcde = new byte[abc.length + de.length];
    System.arraycopy(abc, 0, abcde, 0, abc.length);
    System.arraycopy(de, 0, abcde, abc.length, de.length);
    assertThat(abcde).isEqualTo(new byte[]{'a', 'b', 'c', 'd', 'e'});
  }

  @Test
  void testService() throws IOException, URISyntaxException {
    Path inputPath = Path.of(this.getClass().getClassLoader().getResource("products.json").toURI());
//    Path outPath = Paths.get("out.txt");
//    Files.deleteIfExists(outPath);

    ByteArrayOutputStream writer = new ByteArrayOutputStream(194);
    try(BufferedReader reader = Files.newBufferedReader(inputPath,StandardCharsets.UTF_8)){
//        BufferedOutputStream writer = new BufferedOutputStream(Files.newOutputStream(outPath, StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
      String line;
      while((line = reader.readLine()) != null){
        byte[] b64 = base64.write(line);
        assertThat(b64).asBase64Encoded().isBase64();
        writer.write(b64, 0, b64.length);
        writer.write(base64.newLine(), 0, base64.newLine().length);
      }
    }

    ByteArrayInputStream bais = new ByteArrayInputStream(writer.toByteArray());
    try(BufferedReader reader = new BufferedReader(new InputStreamReader(bais))){
      String line;
      while((line = reader.readLine()) != null){
        assertThat(line).isBase64();
      }
    }
  }
}