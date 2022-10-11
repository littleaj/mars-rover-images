package org.github.littleaj.marsroverimages.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.github.littleaj.marsroverimages.cache.SimpleCache;
import org.github.littleaj.marsroverimages.client.MarsImagesClient;
import org.github.littleaj.marsroverimages.schema.NasaPhotosExtractor;
import org.github.littleaj.marsroverimages.schema.NasaPhotosResponse;

public class MarsImagesServiceImpl implements MarsImagesService {
  private final MarsImagesClient client;
  private final SimpleCache cache;
  private final NasaPhotosExtractor transformer;

  public MarsImagesServiceImpl(MarsImagesClient client, SimpleCache cache, int limitPerDay) {
    this.client = client;
    this.cache = cache;
    this.transformer = new NasaPhotosExtractor(limitPerDay);
  }

  @Override
  public Map<String, List<String>> getImages(LocalDate startDate, int days) {
    Map<String, List<String>> result = new TreeMap<>(Comparator.reverseOrder());
    for (int i = 0; i < days; i++) {
      final var currentDate = startDate.minusDays(i).toString();
      var imagesForDay = this.cache.get(
          currentDate.toString(),
          NasaPhotosResponse.class,
          () -> client.getImagesForDay(currentDate),
          resp -> resp.getPhotos().size() > 0
      );

      result.put(currentDate, this.transformer.extract(imagesForDay));
    }
    return result;
  }
}
