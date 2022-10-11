package org.github.littleaj.marsroverimages.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface MarsImagesService {
  Map<String, List<String>> getImages(LocalDate startDate, int days);
}
