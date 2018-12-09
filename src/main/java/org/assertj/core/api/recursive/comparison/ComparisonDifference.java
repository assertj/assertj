package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static java.lang.String.join;

import java.util.List;
import java.util.Optional;

public class ComparisonDifference {

  private static final String PATH = "%s differ:%n" +
                                     "- actual value   : %s%n" +
                                     "- expected value : %s";

  List<String> path;
  final Object actual;
  final Object other;
  Optional<String> additionalInformation;

  public ComparisonDifference(List<String> path, Object actual, Object other) {
    this(path, actual, other, null);
  }

  public ComparisonDifference(List<String> path, Object actual, Object other, String description) {
    this.path = path;
    this.actual = actual;
    this.other = other;
    this.additionalInformation = Optional.ofNullable(description);
  }

  public String getPath() {
    return join(".", path);
  }

  public Object getActual() {
    return actual;
  }

  public Object getOther() {
    return other;
  }

  public Optional<String> getDescription() {
    return additionalInformation;
  }

  @Override
  public String toString() {
    return additionalInformation.isPresent()
        ? format("Difference [path=%s, actual=%s, other=%s, description=%s]", path, actual, other, additionalInformation.get())
        : format("Difference [path=%s, actual=%s, other=%s]", path, actual, other);
  }

  public String multilineDescription() {
    return additionalInformation.isPresent()
        ? format(PATH + "%n%s", getPath(), actual, other, additionalInformation.get())
        : format(PATH, getPath(), actual, other);
  }

}