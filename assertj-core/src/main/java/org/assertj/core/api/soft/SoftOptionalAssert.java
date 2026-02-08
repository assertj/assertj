package org.assertj.core.api.soft;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.assertj.core.annotation.Beta;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.AbstractOptionalAssert;
import org.assertj.core.api.AssertionErrorCollector;
import org.assertj.core.api.Condition;
import org.assertj.core.api.OptionalAssert;
import org.assertj.core.api.RecursiveComparisonAssert;
import org.assertj.core.api.ThrowingConsumer;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.description.Description;
import org.assertj.core.presentation.Representation;

@Beta
public final class SoftOptionalAssert<VALUE> implements SoftAssert {
  private final AssertionErrorCollector errorCollector;

  private final OptionalAssert<VALUE> optionalAssert;

  public SoftOptionalAssert(Optional<VALUE> actual, AssertionErrorCollector errorCollector) {
    this.errorCollector = errorCollector;
    this.optionalAssert = assertThat(actual);
  }

  public Optional<VALUE> actual() {
    return optionalAssert.actual();
  }

  public SoftOptionalAssert<VALUE> as(String description, Object[] args) {
    optionalAssert.as(description,args);
    return this;
  }

  public SoftOptionalAssert<VALUE> as(Supplier<String> descriptionSupplier) {
    optionalAssert.as(descriptionSupplier);
    return this;
  }

  public <SOFT_ASSERT extends SoftAssert> SOFT_ASSERT asInstanceOf(
      DefaultSoftAssertFactory<?, SOFT_ASSERT> softAssertFactory) {
    return softAssertFactory.createSoftAssert(actual(), errorCollector);
  }

  public SoftOptionalAssert<VALUE> as(Description description) {
    optionalAssert.as(description);
    return this;
  }

  public AbstractCharSequenceAssert asString() {
    return optionalAssert.asString();
  }

  public SoftOptionalAssert<VALUE> contains(VALUE expectedValue) {
    try {
      optionalAssert.contains(expectedValue);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> containsInstanceOf(Class<?> clazz) {
    try {
      optionalAssert.containsInstanceOf(clazz);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> containsSame(VALUE expectedValue) {
    try {
      optionalAssert.containsSame(expectedValue);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> describedAs(String description, Object[] args) {
    optionalAssert.describedAs(description,args);
    return this;
  }

  public SoftOptionalAssert<VALUE> describedAs(Supplier<String> descriptionSupplier) {
    optionalAssert.describedAs(descriptionSupplier);
    return this;
  }

  public SoftOptionalAssert<VALUE> describedAs(Description description) {
    optionalAssert.describedAs(description);
    return this;
  }

  public SoftOptionalAssert<VALUE> descriptionText() {
    optionalAssert.descriptionText();
    return this;
  }

  public SoftOptionalAssert<VALUE> doesNotHave(Condition<? super Optional<VALUE>> condition) {
    try {
      optionalAssert.doesNotHave(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> doesNotHaveSameClassAs(Object other) {
    try {
      optionalAssert.doesNotHaveSameClassAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> doesNotHaveSameHashCodeAs(Object other) {
    try {
      optionalAssert.doesNotHaveSameHashCodeAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> doesNotHaveToString(String otherToString) {
    try {
      optionalAssert.doesNotHaveToString(otherToString);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> doesNotHaveToString(String expectedStringTemplate,
      Object[] args) {
    try {
      optionalAssert.doesNotHaveToString(expectedStringTemplate,args);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> doesNotHaveValue(VALUE expectedValue) {
    try {
      optionalAssert.doesNotHaveValue(expectedValue);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> doesNotMatch(Predicate<? super Optional<VALUE>> predicate) {
    try {
      optionalAssert.doesNotMatch(predicate);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> doesNotMatch(Predicate<? super Optional<VALUE>> predicate,
      String predicateDescription) {
    try {
      optionalAssert.doesNotMatch(predicate,predicateDescription);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public boolean equals(Object obj) {
    return optionalAssert.equals(obj);
  }

  public <U> SoftOptionalAssert<U> flatMap(Function<? super VALUE, Optional<U>> mapper) {
    return new SoftOptionalAssert<>(optionalAssert.flatMap(mapper).actual(), errorCollector);
  }

  public SoftObjectAssert<VALUE> get() {
    return new SoftObjectAssert<>(optionalAssert.get().actual(), errorCollector);
  }

  public <SOFT_ASSERT extends SoftAssert> SOFT_ASSERT get(
      DefaultSoftAssertFactory<?, SOFT_ASSERT> softAssertFactory) {
    return softAssertFactory.createSoftAssert(actual().get(), errorCollector);
  }

  public WritableAssertionInfo getWritableAssertionInfo() {
    return optionalAssert.getWritableAssertionInfo();
  }

  public SoftOptionalAssert<VALUE> has(Condition<? super Optional<VALUE>> condition) {
    try {
      optionalAssert.has(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public int hashCode() {
    return optionalAssert.hashCode();
  }

  public SoftOptionalAssert<VALUE> hasSameClassAs(Object other) {
    try {
      optionalAssert.hasSameClassAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> hasSameHashCodeAs(Object other) {
    try {
      optionalAssert.hasSameHashCodeAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> hasToString(String expectedToString) {
    try {
      optionalAssert.hasToString(expectedToString);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> hasToString(String expectedStringTemplate, Object[] args) {
    try {
      optionalAssert.hasToString(expectedStringTemplate,args);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> hasValue(VALUE expectedValue) {
    try {
      optionalAssert.hasValue(expectedValue);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> hasValueMatching(Predicate<? super VALUE> predicate) {
    try {
      optionalAssert.hasValueMatching(predicate);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> hasValueMatching(Predicate<? super VALUE> predicate,
      String description) {
    try {
      optionalAssert.hasValueMatching(predicate,description);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> hasValueSatisfying(Consumer<VALUE> requirement) {
    try {
      optionalAssert.hasValueSatisfying(requirement);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> hasValueSatisfying(Condition<? super VALUE> condition) {
    try {
      optionalAssert.hasValueSatisfying(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> hasValueSatisfying(ThrowingConsumer<VALUE> requirement) {
    try {
      optionalAssert.hasValueSatisfying(requirement);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> is(Condition condition) {
    try {
      optionalAssert.is(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isEmpty() {
    try {
      optionalAssert.isEmpty();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isEqualTo(Object expected) {
    try {
      optionalAssert.isEqualTo(expected);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isExactlyInstanceOf(Class type) {
    try {
      optionalAssert.isExactlyInstanceOf(type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isIn(Iterable values) {
    try {
      optionalAssert.isIn(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isIn(Object[] values) {
    try {
      optionalAssert.isIn(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isInstanceOf(Class<?> type) {
    try {
      optionalAssert.isInstanceOf(type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isInstanceOfAny(Class[] types) {
    try {
      optionalAssert.isInstanceOfAny(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isInstanceOfSatisfying(Class type, Consumer requirements) {
    try {
      optionalAssert.isInstanceOfSatisfying(type,requirements);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isNot(Condition condition) {
    try {
      optionalAssert.isNot(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isNotEmpty() {
    try {
      optionalAssert.isNotEmpty();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isNotEqualTo(Object other) {
    try {
      optionalAssert.isNotEqualTo(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isNotExactlyInstanceOf(Class<?> type) {
    try {
      optionalAssert.isNotExactlyInstanceOf(type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isNotIn(Iterable<?> values) {
    try {
      optionalAssert.isNotIn(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isNotIn(Object[] values) {
    try {
      optionalAssert.isNotIn(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isNotInstanceOf(Class<?> type) {
    try {
      optionalAssert.isNotInstanceOf(type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isNotInstanceOfAny(Class<?>[] types) {
    try {
      optionalAssert.isNotInstanceOfAny(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isNotNull() {
    try {
      optionalAssert.isNotNull();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isNotOfAnyClassIn(Class<?>[] types) {
    try {
      optionalAssert.isNotOfAnyClassIn(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isNotPresent() {
    try {
      optionalAssert.isNotPresent();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isNotSameAs(Object other) {
    try {
      optionalAssert.isNotSameAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isNull() {
    try {
      optionalAssert.isNull();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isOfAnyClassIn(Class[] types) {
    try {
      optionalAssert.isOfAnyClassIn(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isPresent() {
    try {
      optionalAssert.isPresent();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> isSameAs(Object expected) {
    try {
      optionalAssert.isSameAs(expected);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public <U> AbstractOptionalAssert<?, U> map(Function<? super VALUE, ? extends U> mapper) {
    return optionalAssert.map(mapper);
  }

  public SoftOptionalAssert<VALUE> matches(Predicate<? super Optional<VALUE>> predicate) {
    try {
      optionalAssert.matches(predicate);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> matches(Predicate<? super Optional<VALUE>> predicate,
      String predicateDescription) {
    try {
      optionalAssert.matches(predicate,predicateDescription);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> overridingErrorMessage(String newErrorMessage, Object[] args) {
    optionalAssert.overridingErrorMessage(newErrorMessage,args);
    return this;
  }

  public SoftOptionalAssert<VALUE> overridingErrorMessage(Supplier<String> supplier) {
    optionalAssert.overridingErrorMessage(supplier);
    return this;
  }

  public SoftOptionalAssert<VALUE> satisfies(Consumer<? super Optional<VALUE>>[] requirements) {
    try {
      optionalAssert.satisfies(requirements);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> satisfies(Condition condition) {
    try {
      optionalAssert.satisfies(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> satisfies(
      ThrowingConsumer<? super Optional<VALUE>>[] assertions) {
    try {
      optionalAssert.satisfies(assertions);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> satisfiesAnyOf(Consumer<? super Optional<VALUE>>[] assertions) {
    try {
      optionalAssert.satisfiesAnyOf(assertions);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> satisfiesAnyOf(
      ThrowingConsumer<? super Optional<VALUE>>[] assertions) {
    try {
      optionalAssert.satisfiesAnyOf(assertions);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> usingComparator(
      Comparator<? super Optional<VALUE>> customComparator) {
    optionalAssert.usingComparator(customComparator);
    return this;
  }

  public SoftOptionalAssert<VALUE> usingComparator(Comparator customComparator,
      String customComparatorDescription) {
    optionalAssert.usingComparator(customComparator,customComparatorDescription);
    return this;
  }

  public SoftOptionalAssert<VALUE> usingDefaultComparator() {
    optionalAssert.usingDefaultComparator();
    return this;
  }

  public SoftOptionalAssert<VALUE> usingDefaultValueComparator() {
    try {
      optionalAssert.usingDefaultValueComparator();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> usingEquals(
      BiPredicate<? super Optional<VALUE>, ? super Optional<VALUE>> predicate) {
    optionalAssert.usingEquals(predicate);
    return this;
  }

  public SoftOptionalAssert<VALUE> usingEquals(
      BiPredicate<? super Optional<VALUE>, ? super Optional<VALUE>> predicate,
      String customEqualsDescription) {
    optionalAssert.usingEquals(predicate,customEqualsDescription);
    return this;
  }

  public SoftOptionalAssert<VALUE> usingRecursiveAssertion() {
    try {
      optionalAssert.usingRecursiveAssertion();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftOptionalAssert<VALUE> usingRecursiveAssertion(
      RecursiveAssertionConfiguration recursiveAssertionConfiguration) {
    try {
      optionalAssert.usingRecursiveAssertion(recursiveAssertionConfiguration);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public RecursiveComparisonAssert<?> usingRecursiveComparison() {
    return optionalAssert.usingRecursiveComparison();
  }

  public RecursiveComparisonAssert<?> usingRecursiveComparison(
      RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    return optionalAssert.usingRecursiveComparison(recursiveComparisonConfiguration);
  }

  public SoftOptionalAssert<VALUE> usingValueComparator(
      Comparator<? super VALUE> customComparator) {
    optionalAssert.usingValueComparator(customComparator);
    return this;
  }

  public SoftOptionalAssert<VALUE> withFailMessage(String newErrorMessage, Object[] args) {
    optionalAssert.withFailMessage(newErrorMessage,args);
    return this;
  }

  public SoftOptionalAssert<VALUE> withFailMessage(Supplier<String> supplier) {
    optionalAssert.withFailMessage(supplier);
    return this;
  }

  public SoftOptionalAssert<VALUE> withRepresentation(Representation representation) {
    optionalAssert.withRepresentation(representation);
    return this;
  }

  public SoftOptionalAssert<VALUE> withThreadDumpOnError() {
    optionalAssert.withThreadDumpOnError();
    return this;
  }
}
