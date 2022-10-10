package org.github.littleaj.marsroverimages.schema;

import java.util.ArrayList;
import java.util.logging.Logger;

public class NasaPhotosExtractor {
  private static final Logger LOGGER = Logger.getLogger(NasaPhotosExtractor.class.getName());

  public final int photosLimit;

  public NasaPhotosExtractor(int photosLimit) {
    if (photosLimit < 0) {
      throw new IllegalArgumentException("photosLimit must be nonnegative");
    }
    LOGGER.info("Extractor created with limit=" + photosLimit);
    this.photosLimit = photosLimit;
  }

  public ArrayList<String> extract(NasaPhotosResponse response) {
    var result = new ArrayList<String>();
    response.getPhotos().stream().limit(photosLimit).forEach(pe -> {
      result.add(pe.getImgSrc());
    });
    return result;
  }
}
