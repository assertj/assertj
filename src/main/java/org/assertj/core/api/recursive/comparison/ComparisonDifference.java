package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static java.lang.String.join;
import static org.assertj.core.util.Objects.areEqual;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.assertj.core.configuration.ConfigurationProvider;
import org.assertj.core.presentation.Representation;

public class ComparisonDifference implements Comparable<ComparisonDifference> {

  private static final String TEMPLATE = "field/property '%s' differ:%n" +
                                         "- actual value   : %s%n" +
                                         "- expected value : %s%s";

  final List<String> path;
  final String concatenatedPath;
  final Object actual;
  final Object expected;
  final Optional<String> additionalInformation;


  public ComparisonDifference(List<String> path, Object actual, Object other) {
    this(path, actual, other, null);
  }

  public ComparisonDifference(List<String> path, Object actual, Object other, String additionalInformation) {
    Objects.requireNonNull(path, "a path can't be null");
    this.path = Collections.unmodifiableList(path);
    this.concatenatedPath = join(".", this.path);
    this.actual = actual;
    this.expected = other;
    this.additionalInformation = Optional.ofNullable(additionalInformation);
  }

  public String getPath() {
    return concatenatedPath;
  }

  public Object getActual() {
    return actual;
  }

  public Object getExpected() {
    return expected;
  }

  public Optional<String> getDescription() {
    return additionalInformation;
  }

  @Override
  public String toString() {
    return additionalInformation.isPresent()
        ? format("ComparisonDifference [path=%s, actual=%s, other=%s, additionalInformation=%s]", concatenatedPath, actual, expected,
                 additionalInformation.get())
        : format("ComparisonDifference [path=%s, actual=%s, other=%s]", concatenatedPath, actual, expected);
  }

  public String multiLineDescription() {
    // use the default configured representation
    return multiLineDescription(ConfigurationProvider.CONFIGURATION_PROVIDER.representation());
  }

  public String multiLineDescription(Representation representation) {

    String actualRepresentation = representation.toStringOf(actual);
    String expectedRepresentation = representation.toStringOf(expected);

    boolean sameRepresentation = areEqual(actualRepresentation, expectedRepresentation);
    String unambiguousActualRepresentation = sameRepresentation
        ? representation.unambiguousToStringOf(actual)
        : actualRepresentation;
    String unambiguousExpectedRepresentation = sameRepresentation
        ? representation.unambiguousToStringOf(expected)
        : expectedRepresentation;

    String additionalInfo = additionalInformation.map(ComparisonDifference::formatOnNewline)
                                                 .orElse("");
    return format(TEMPLATE,
                  getPath(),
                  unambiguousActualRepresentation,
                  unambiguousExpectedRepresentation,
                  additionalInfo);
  }

  private static String formatOnNewline(String info) {
    return format("%n%s", info);
  }

  @Override
  public boolean equals(final Object other) {
    if (!(other instanceof ComparisonDifference)) {
      return false;
    }
    ComparisonDifference castOther = (ComparisonDifference) other;
    return Objects.equals(concatenatedPath, castOther.concatenatedPath)
           && Objects.equals(actual, castOther.actual)
           && Objects.equals(expected, castOther.expected)
           && Objects.equals(additionalInformation, castOther.additionalInformation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(concatenatedPath, actual, expected, additionalInformation);
  }

  @Override
  public int compareTo(final ComparisonDifference other) {
    // we don't use '.' to join path before comparing them as it would make a.b < aa
    return join("", this.path).compareTo(join("", other.path));
  }

}