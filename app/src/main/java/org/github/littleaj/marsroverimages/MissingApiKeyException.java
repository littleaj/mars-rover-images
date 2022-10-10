package org.github.littleaj.marsroverimages;

public class MissingApiKeyException extends Exception {

  public MissingApiKeyException(String message) {
    super(message);
  }

  public MissingApiKeyException(String message, Throwable cause) {
    super(message, cause);
  }
  
}
