package org.core.util;

public class Utils {
  // Returns random number between 1 and 100 inclusive
  public static int getRandomId() {
    return (int) (Math.random() * 100) + 1;
  }
}
