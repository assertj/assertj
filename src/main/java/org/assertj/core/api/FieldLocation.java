package org.assertj.core.api;

// TODO should understand Map keys as field
public class FieldLocation implements Comparable<FieldLocation> {
  private String fieldPath;
  // private boolean whereverInGraph = false;
  // private boolean matches(Field field, Field parent); ?

  @Override
  public int compareTo(FieldLocation o) {
    return 0;
  }
}
