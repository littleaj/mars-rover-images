package org.github.littleaj.marsroverimages.schema;

import java.util.ArrayList;

import org.github.littleaj.marsroverimages.schema.NasaPhotosResponse.PhotoElement;

public class SchemaTestUtils {
  private SchemaTestUtils() {}

  public static NasaPhotosResponse getTestResponse(int elementCount, String suffix) {
    var photos = new ArrayList<PhotoElement>();
    for (int i = 1; i <= elementCount; i++) {
      var pe = new PhotoElement();
      pe.img_src = "test" + i + suffix;
      photos.add(pe);
    }
    
    NasaPhotosResponse response = new NasaPhotosResponse();
    response.photos = photos;
    return response;
  }
}
