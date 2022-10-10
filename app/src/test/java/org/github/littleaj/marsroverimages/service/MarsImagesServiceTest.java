package org.github.littleaj.marsroverimages.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.github.littleaj.marsroverimages.schema.SchemaTestUtils.getTestResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.github.littleaj.marsroverimages.cache.SimpleCache;
import org.github.littleaj.marsroverimages.cache.SimpleFileCache;
import org.github.littleaj.marsroverimages.client.MarsImagesClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MarsImagesServiceTest {

  private MarsImagesService service;

  @TempDir
  File tempDir;

  @Mock
  MarsImagesClient mockClient;

  SimpleCache cache;

  @BeforeEach
  void setup() {
    cache = spy(new SimpleFileCache(tempDir));
    service = new MarsImagesService(mockClient, cache, Integer.MAX_VALUE);
  }

  @Test
  void getImagesReturnsMapFromDayToImageList() {
    LocalDate jun5_2020 = LocalDate.parse("2020-06-05");
    doReturn(getTestResponse(3, "-service-test")).when(cache).get(anyString(), any(), any(), any());
    
    Map<String, List<String>> images = service.getImages(jun5_2020, 1);
    assertThat(images).containsOnlyKeys(jun5_2020.toString());
    assertThat(images).hasEntrySatisfying(jun5_2020.toString(), t -> {
      assertThat(t).containsExactly("test1-service-test", "test2-service-test", "test3-service-test");
    });
  }

  @Test
  void getImagesRespectsLimit() {
    final var mar10_1999 = LocalDate.parse("1999-03-10");
    when(mockClient.getImagesForDay(anyString()))
        .then(invocation -> {
          var date = invocation.getArgument(0, String.class);
          return getTestResponse(3, "_" + date);
        });
    service = new MarsImagesService(mockClient, cache, 2);
    var images = service.getImages(mar10_1999, 1);
    assertThat(images).size().isEqualTo(1);
    assertThat(images).containsOnlyKeys(mar10_1999.toString());
    assertThat(images).hasEntrySatisfying(mar10_1999.toString(), list -> {
      assertThat(list).containsExactly("test1_1999-03-10", "test2_1999-03-10");
    });
  }

  @Test
  void getImagesWithMultipleDaysReturnsMultipleLists() {
    final var oct09_2022 = LocalDate.parse("2022-10-09");
    final var oct08_2022 = oct09_2022.minusDays(1);
    final var oct07_2022 = oct08_2022.minusDays(1);

    when(mockClient.getImagesForDay(anyString()))
        .then(invocation -> {
          var date = invocation.getArgument(0, String.class);
          return getTestResponse(3, "_" + date);
        });
    
    service = new MarsImagesService(mockClient, cache, 1);
    var images = service.getImages(oct09_2022, 3);
    assertThat(images).size().isEqualTo(3);
    assertThat(images).containsOnlyKeys(oct09_2022.toString(), oct08_2022.toString(), oct07_2022.toString());
    assertThat(images).hasEntrySatisfying(oct09_2022.toString(), value -> {
      assertThat(value).containsExactly("test1_" + oct09_2022.toString());
    });
    assertThat(images).hasEntrySatisfying(oct08_2022.toString(), value -> {
      assertThat(value).containsExactly("test1_" + oct08_2022.toString());
    });
    assertThat(images).hasEntrySatisfying(oct07_2022.toString(), value -> {
      assertThat(value).containsExactly("test1_" + oct07_2022.toString());
    });
  }

  @Test
  void limitEnforcedAfterCaching() {
    when(mockClient.getImagesForDay(anyString()))
        .thenReturn(getTestResponse(4, "_cachetest"));
    service = new MarsImagesService(mockClient, cache, 1);
    LocalDate date = LocalDate.parse("2020-12-21");
    var result1 = service.getImages(date, 1);
    assertThat(result1).containsOnlyKeys(date.toString());
    assertThat(result1).hasEntrySatisfying(date.toString(), value -> {
      assertThat(value).hasSize(1);
    });

    service = new MarsImagesService(mockClient, cache, 3);
    var result2 = service.getImages(date, 1);
    assertThat(result2).containsOnlyKeys(date.toString());
    assertThat(result2).hasEntrySatisfying(date.toString(), value -> {
      assertThat(value).hasSize(3);
    });
    verify(cache, times(2)).get(anyString(), any());
  }
}
