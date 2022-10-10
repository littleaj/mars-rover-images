package org.github.littleaj.marsroverimages.cache;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class SimpleFileCacheTest {
  @TempDir
  File tempDir;

  SimpleFileCache cache;

  @BeforeEach
  void setup() {
    cache = new SimpleFileCache(tempDir);
  }

  @Test
  void nullBaseDirIsIllegal() {
    assertThatThrownBy(() -> {
      new SimpleFileCache(null);
    }).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void getOnEmptyCacheReturnsNull() {
    assertThat(cache.get("nothing", String.class)).isNull();
  }

  @Test
  void putCreatesCacheFile() {
    cache.put("test123", "test-value-123");
    assertThat(new File(tempDir, SimpleFileCache.CACHE_SUBDIRECTORY)).isDirectoryContaining(f -> f.getName().equals("test123"));
  }
  
  @Test
  void getReturnsFileContentsWhenFileExists() {
    cache.put("test456", "test-value-456");
    assertThat(new File(tempDir, SimpleFileCache.CACHE_SUBDIRECTORY)).isDirectoryContaining(f -> f.getName().equals("test456"));
    var value = cache.get("test456", String.class);
    assertThat(value).isEqualTo("test-value-456");
  }

}
