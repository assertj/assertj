package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.recursive.comparison.FieldLocation.fielLocation;
import static org.assertj.core.error.ShouldBeEqualByComparingFieldByFieldRecursively.shouldBeEqualByComparingFieldByFieldRecursively;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.configuration.ConfigurationProvider;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.presentation.Representation;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

public class RecursiveComparisonAssert {

  private Object actual;
  private Representation customRepresentation = ConfigurationProvider.CONFIGURATION_PROVIDER.representation();
  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;
  private RecursiveComparisonDifferenceCalculator recursiveComparisonDifferenceCalculator;
  @VisibleForTesting
  WritableAssertionInfo assertionInfo;
  @VisibleForTesting
  Failures failures;
  @VisibleForTesting
  Objects objects;

  // TODO propagate assertion info from ...
  public RecursiveComparisonAssert(Object actual, RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    this.actual = actual;
    this.recursiveComparisonConfiguration = recursiveComparisonConfiguration;
    this.assertionInfo = new WritableAssertionInfo(customRepresentation);
    recursiveComparisonDifferenceCalculator = new RecursiveComparisonDifferenceCalculator();
    failures = Failures.instance();
    objects = Objects.instance();;
  }

  /** {@inheritDoc} */
  @CheckReturnValue
  public RecursiveComparisonAssert as(String description, Object... args) {
    assertionInfo.description(description, args);
    return this;
  }

  public RecursiveComparisonAssert ignoringNullFields() {
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    return this;
  }

  public RecursiveComparisonAssert isEqualTo(Object expected) {
    // deals with both actual and expected being null
    if (actual == expected) return this;
    if (expected == null) {
      // for the assertion to pass, actual must be null but this is not the case since actual != expected
      // => we fail expecting actual to be null
      objects.assertNull(assertionInfo, actual);
    }
    // at this point expected is not null, which means actual must not be null for the assertion to pass
    objects.assertNotNull(assertionInfo, actual);
    // at this point both actual and expected are not null, we can compare them recursively!
    List<ComparisonDifference> differences = determineDifferencesWith(expected);
    if (!differences.isEmpty()) throw failures.failure(assertionInfo, shouldBeEqualByComparingFieldByFieldRecursively(actual,
                                                                                                                      expected,
                                                                                                                      differences,
                                                                                                                      recursiveComparisonConfiguration,
                                                                                                                      assertionInfo.representation()));
    return this;
  }

  private List<ComparisonDifference> determineDifferencesWith(Object expected) {
    return recursiveComparisonDifferenceCalculator.determineDifferences(actual, expected, recursiveComparisonConfiguration);
  }

  public RecursiveComparisonAssert ignoringFields(String... fieldsToIgnore) {
    recursiveComparisonConfiguration.ignoreFields(fieldsToIgnore);
    return this;
  }

  public RecursiveComparisonAssert ignoringOverriddenEqualsForFields(String... fieldPaths) {
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFields(fieldPaths);
    return this;
  }

  public RecursiveComparisonAssert ignoringOverriddenEqualsForTypes(Class<?>... types) {
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForTypes(types);
    return this;
  }

  public RecursiveComparisonAssert ignoringOverriddenEqualsByRegexes(String... regexes) {
    recursiveComparisonConfiguration.ignoreOverriddenEqualsByRegexes(regexes);
    return this;
  }

  public RecursiveComparisonAssert withStrictTypeChecking() {
    recursiveComparisonConfiguration.strictTypeChecking(true);
    return this;
  }

  public RecursiveComparisonAssert withComparatorForField(String fieldLocation, Comparator<?> comparator) {
    recursiveComparisonConfiguration.registerComparatorForField(fielLocation(fieldLocation), comparator);
    return this;
  }

  @SafeVarargs
  public final RecursiveComparisonAssert withComparatorForFields(Map.Entry<String, Comparator<?>>... comparatorByFields) {
    Stream.of(comparatorByFields).forEach(this::withComparatorForField);
    return this;
  }

  // syntactic sugar
  private RecursiveComparisonAssert withComparatorForField(Map.Entry<String, Comparator<?>> comparatorByField) {
    return withComparatorForField(comparatorByField.getKey(), comparatorByField.getValue());
  }

  public <T> RecursiveComparisonAssert withComparatorForType(Class<T> type, Comparator<? super T> comparator) {
    recursiveComparisonConfiguration.registerComparatorForType(type, comparator);
    return this;
  }

  // can't type with <T> since the classes type have no reason to all be related to T.
  @SafeVarargs
  public final RecursiveComparisonAssert withComparatorForTypes(Map.Entry<Class<?>, Comparator<Object>>... comparatorByTypes) {
    Stream.of(comparatorByTypes)
          .forEach(comparatorByType -> withComparatorForType(comparatorByType.getKey(), comparatorByType.getValue()));
    return this;
  }
}
