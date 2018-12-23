package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static java.lang.String.join;

import java.util.List;
import java.util.Optional;

import org.assertj.core.configuration.ConfigurationProvider;
import org.assertj.core.presentation.Representation;
import org.assertj.core.util.Objects;

public class ComparisonDifference {

  private static final String TEMPLATE = "field/property '%s' differ:%n" +
                                         "- actual value   : %s%n" +
                                         "- expected value : %s%s";

  List<String> path;
  final Object actual;
  final Object expected;
  Optional<String> additionalInformation;

  public ComparisonDifference(List<String> path, Object actual, Object other) {
    this(path, actual, other, null);
  }

  public ComparisonDifference(List<String> path, Object actual, Object other, String additionalInformation) {
    this.path = path;
    this.actual = actual;
    this.expected = other;
    this.additionalInformation = Optional.ofNullable(additionalInformation);
  }

  public String getPath() {
    return join(".", path);
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
        ? format("ComparisonDifference [path=%s, actual=%s, other=%s, additionalInformation=%s]", path, actual, expected,
                 additionalInformation.get())
        : format("ComparisonDifference [path=%s, actual=%s, other=%s]", path, actual, expected);
  }

  public String multiLineDescription() {
    // use the default configured representation
    return multiLineDescription(ConfigurationProvider.CONFIGURATION_PROVIDER.representation());
  }

  public String multiLineDescription(Representation representation) {

    String actualRepresentation = representation.toStringOf(getActual());
    String expectedRepresentation = representation.toStringOf(getExpected());

    boolean sameRepresentation = Objects.areEqual(actualRepresentation, expectedRepresentation);
    String unambiguousActualRepresentation = sameRepresentation
        ? representation.unambiguousToStringOf(getActual())
        : actualRepresentation;
    String unambiguousExpectedRepresentation = sameRepresentation
        ? representation.unambiguousToStringOf(getExpected())
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

}