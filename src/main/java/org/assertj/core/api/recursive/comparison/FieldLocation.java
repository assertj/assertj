package org.assertj.core.api.recursive.comparison;

import java.util.Comparator;
import java.util.Objects;

// TODO should understand Map keys as field
public class FieldLocation implements Comparable<FieldLocation> {

  // TODO test
  private static final Comparator<FieldLocation> COMPARATOR = Comparator.comparing(FieldLocation::getFieldPath);

  private String fieldPath;
  // private boolean whereverInGraph = false;
  // private boolean matches(Field field, Field parent); ?

  @Override
  public int compareTo(final FieldLocation other) {
    return COMPARATOR.compare(this, other);
  }

  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof FieldLocation)) return false;
    FieldLocation castOther = (FieldLocation) other;
    return Objects.equals(fieldPath, castOther.fieldPath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fieldPath);
  }

  public FieldLocation(String fieldPath) {
    Objects.requireNonNull(fieldPath, "a field path can't be null");
    this.fieldPath = fieldPath;
  }

  public String getFieldPath() {
    return fieldPath;
  }

  @Override
  public String toString() {
    return String.format("FieldLocation [fieldPath=%s]", fieldPath);
  }

  public boolean matches(String concatenatedPath) {
    return fieldPath.equals(concatenatedPath);
  }

}
