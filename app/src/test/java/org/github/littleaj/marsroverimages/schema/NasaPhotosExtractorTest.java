package org.github.littleaj.marsroverimages.schema;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;

public class NasaPhotosExtractorTest {

  private NasaPhotosExtractor extractor;

  @BeforeEach
  void setup() {
    extractor = new NasaPhotosExtractor(Integer.MAX_VALUE);
  }

  @Test
  void extractReturnsListOfPhotosInResponse() {
    NasaPhotosResponse response = SchemaTestUtils.getTestResponse(3, "");
    var extracted = extractor.extract(response);
    assertThat(extracted).containsExactly("test1", "test2", "test3");
  }

  @Test
  void limitTruncatesList() {
    extractor = new NasaPhotosExtractor(1);
    var extracted = extractor.extract(SchemaTestUtils.getTestResponse(3, ""));
    assertThat(extracted).containsExactly("test1");
  }

  @Test
  void negativeLimitThrowsIllegalArgumentException() {
    Assertions.assertThatCode(() -> new NasaPhotosExtractor(0)).doesNotThrowAnyException();
    assertThatThrownBy(() -> new NasaPhotosExtractor(-1))
        .isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> new NasaPhotosExtractor(Integer.MIN_VALUE))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
