package org.github.littleaj.marsroverimages.schema;

import java.io.Serializable;
import java.util.List;

public class NasaPhotosResponse implements Serializable {
  private static final long serialVersionUID = 42L;

  public static class PhotoElement implements Serializable {
    private static final long serialVersionUID = 8455L;

    String img_src;

    public String getImgSrc() {
      return img_src;
    }
  }

  List<PhotoElement> photos;

  public List<PhotoElement> getPhotos() {
    return photos;
  }
}