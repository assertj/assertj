package org.assertj.core.api.soft;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.AssertionError;
import java.lang.Class;
import java.lang.Iterable;
import java.lang.Object;
import java.lang.String;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.assertj.core.annotation.Beta;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.AssertionErrorCollector;
import org.assertj.core.api.Condition;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.RecursiveComparisonAssert;
import org.assertj.core.api.ThrowingConsumer;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.description.Description;
import org.assertj.core.presentation.Representation;

@Beta
public final class SoftObjectAssert<ACTUAL> implements SoftAssert {
  private final AssertionErrorCollector errorCollector;

  private final ObjectAssert<ACTUAL> objectAssert;

  public SoftObjectAssert(ACTUAL actual, AssertionErrorCollector errorCollector) {
    this.errorCollector = errorCollector;
    this.objectAssert = assertThat(actual);
  }

  public ACTUAL actual() {
    return objectAssert.actual();
  }

  public SoftObjectAssert<ACTUAL> as(String description, Object[] args) {
    objectAssert.as(description,args);
    return this;
  }

  public SoftObjectAssert<ACTUAL> as(Supplier<String> descriptionSupplier) {
    objectAssert.as(descriptionSupplier);
    return this;
  }

  public <SOFT_ASSERT extends SoftAssert> SOFT_ASSERT asInstanceOf(
      DefaultSoftAssertFactory<?, SOFT_ASSERT> softAssertFactory) {
    return softAssertFactory.createSoftAssert(actual(), errorCollector);
  }

  public SoftObjectAssert<ACTUAL> as(Description description) {
    objectAssert.as(description);
    return this;
  }

  public AbstractCharSequenceAssert asString() {
    return objectAssert.asString();
  }

  public SoftObjectAssert<ACTUAL> describedAs(String description, Object[] args) {
    objectAssert.describedAs(description,args);
    return this;
  }

  public SoftObjectAssert<ACTUAL> describedAs(Supplier<String> descriptionSupplier) {
    objectAssert.describedAs(descriptionSupplier);
    return this;
  }

  public SoftObjectAssert<ACTUAL> describedAs(Description description) {
    objectAssert.describedAs(description);
    return this;
  }

  public SoftObjectAssert<ACTUAL> descriptionText() {
    objectAssert.descriptionText();
    return this;
  }

  public SoftObjectAssert<ACTUAL> doesNotHave(Condition<? super ACTUAL> condition) {
    try {
      objectAssert.doesNotHave(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> doesNotHaveSameClassAs(Object other) {
    try {
      objectAssert.doesNotHaveSameClassAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> doesNotHaveSameHashCodeAs(Object other) {
    try {
      objectAssert.doesNotHaveSameHashCodeAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> doesNotHaveToString(String otherToString) {
    try {
      objectAssert.doesNotHaveToString(otherToString);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> doesNotHaveToString(String expectedStringTemplate,
      Object[] args) {
    try {
      objectAssert.doesNotHaveToString(expectedStringTemplate,args);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> doesNotMatch(Predicate<? super ACTUAL> predicate) {
    try {
      objectAssert.doesNotMatch(predicate);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> doesNotMatch(Predicate<? super ACTUAL> predicate,
      String predicateDescription) {
    try {
      objectAssert.doesNotMatch(predicate,predicateDescription);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public <T> SoftObjectAssert<ACTUAL> doesNotReturn(T expected, Function<ACTUAL, T> from) {
    try {
      objectAssert.doesNotReturn(expected,from);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public boolean equals(Object obj) {
    return objectAssert.equals(obj);
  }

  public AbstractObjectAssert<?, ?> extracting(String propertyOrField) {
    return objectAssert.extracting(propertyOrField);
  }

  public <ASSERT extends AbstractAssert<?, ?>> ASSERT extracting(String propertyOrField,
      InstanceOfAssertFactory<?, ASSERT> assertFactory) {
    return objectAssert.extracting(propertyOrField,assertFactory);
  }

  public AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> extracting(
      String[] propertiesOrFields) {
    return objectAssert.extracting(propertiesOrFields);
  }

  public <T> AbstractObjectAssert<?, T> extracting(Function<? super ACTUAL, T> extractor) {
    return objectAssert.extracting(extractor);
  }

  public <T, ASSERT extends AbstractAssert<?, ?>> ASSERT extracting(
      Function<? super ACTUAL, T> extractor, InstanceOfAssertFactory<?, ASSERT> assertFactory) {
    return objectAssert.extracting(extractor,assertFactory);
  }

  public AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> extracting(
      Function<? super ACTUAL, ?>[] extractors) {
    return objectAssert.extracting(extractors);
  }

  public WritableAssertionInfo getWritableAssertionInfo() {
    return objectAssert.getWritableAssertionInfo();
  }

  public SoftObjectAssert<ACTUAL> has(Condition<? super ACTUAL> condition) {
    try {
      objectAssert.has(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> hasAllNullFieldsOrProperties() {
    try {
      objectAssert.hasAllNullFieldsOrProperties();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> hasAllNullFieldsOrPropertiesExcept(
      String[] propertiesOrFieldsToIgnore) {
    try {
      objectAssert.hasAllNullFieldsOrPropertiesExcept(propertiesOrFieldsToIgnore);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> hasFieldOrProperty(String name) {
    try {
      objectAssert.hasFieldOrProperty(name);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> hasFieldOrPropertyWithValue(String name, Object value) {
    try {
      objectAssert.hasFieldOrPropertyWithValue(name,value);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public int hashCode() {
    return objectAssert.hashCode();
  }

  public SoftObjectAssert<ACTUAL> hasNoNullFieldsOrProperties() {
    try {
      objectAssert.hasNoNullFieldsOrProperties();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> hasNoNullFieldsOrPropertiesExcept(
      String[] propertiesOrFieldsToIgnore) {
    try {
      objectAssert.hasNoNullFieldsOrPropertiesExcept(propertiesOrFieldsToIgnore);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> hasOnlyFields(String[] expectedFieldNames) {
    try {
      objectAssert.hasOnlyFields(expectedFieldNames);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> hasSameClassAs(Object other) {
    try {
      objectAssert.hasSameClassAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> hasSameHashCodeAs(Object other) {
    try {
      objectAssert.hasSameHashCodeAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> hasToString(String expectedToString) {
    try {
      objectAssert.hasToString(expectedToString);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> hasToString(String expectedStringTemplate, Object[] args) {
    try {
      objectAssert.hasToString(expectedStringTemplate,args);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> is(Condition condition) {
    try {
      objectAssert.is(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> isEqualTo(Object expected) {
    try {
      objectAssert.isEqualTo(expected);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> isExactlyInstanceOf(Class type) {
    try {
      objectAssert.isExactlyInstanceOf(type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> isIn(Iterable values) {
    try {
      objectAssert.isIn(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> isIn(Object[] values) {
    try {
      objectAssert.isIn(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> isInstanceOf(Class<?> type) {
    try {
      objectAssert.isInstanceOf(type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> isInstanceOfAny(Class[] types) {
    try {
      objectAssert.isInstanceOfAny(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> isInstanceOfSatisfying(Class type, Consumer requirements) {
    try {
      objectAssert.isInstanceOfSatisfying(type,requirements);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> isNot(Condition condition) {
    try {
      objectAssert.isNot(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> isNotEqualTo(Object other) {
    try {
      objectAssert.isNotEqualTo(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> isNotExactlyInstanceOf(Class<?> type) {
    try {
      objectAssert.isNotExactlyInstanceOf(type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> isNotIn(Iterable<?> values) {
    try {
      objectAssert.isNotIn(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> isNotIn(Object[] values) {
    try {
      objectAssert.isNotIn(values);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> isNotInstanceOf(Class<?> type) {
    try {
      objectAssert.isNotInstanceOf(type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> isNotInstanceOfAny(Class<?>[] types) {
    try {
      objectAssert.isNotInstanceOfAny(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> isNotNull() {
    try {
      objectAssert.isNotNull();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> isNotOfAnyClassIn(Class<?>[] types) {
    try {
      objectAssert.isNotOfAnyClassIn(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> isNotSameAs(Object other) {
    try {
      objectAssert.isNotSameAs(other);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> isNull() {
    try {
      objectAssert.isNull();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> isOfAnyClassIn(Class[] types) {
    try {
      objectAssert.isOfAnyClassIn(types);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> isSameAs(Object expected) {
    try {
      objectAssert.isSameAs(expected);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> matches(Predicate<? super ACTUAL> predicate) {
    try {
      objectAssert.matches(predicate);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> matches(Predicate<? super ACTUAL> predicate,
      String predicateDescription) {
    try {
      objectAssert.matches(predicate,predicateDescription);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> overridingErrorMessage(String newErrorMessage, Object[] args) {
    objectAssert.overridingErrorMessage(newErrorMessage,args);
    return this;
  }

  public SoftObjectAssert<ACTUAL> overridingErrorMessage(Supplier<String> supplier) {
    objectAssert.overridingErrorMessage(supplier);
    return this;
  }

  public <T> SoftObjectAssert<ACTUAL> returns(T expected, Function<ACTUAL, T> from) {
    try {
      objectAssert.returns(expected,from);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public <T> SoftObjectAssert<ACTUAL> returns(T expected, Function<ACTUAL, T> from,
      String description) {
    try {
      objectAssert.returns(expected,from,description);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> satisfies(Consumer<? super ACTUAL>[] requirements) {
    try {
      objectAssert.satisfies(requirements);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> satisfies(Condition condition) {
    try {
      objectAssert.satisfies(condition);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> satisfies(ThrowingConsumer<? super ACTUAL>[] assertions) {
    try {
      objectAssert.satisfies(assertions);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> satisfiesAnyOf(Consumer<? super ACTUAL>[] assertions) {
    try {
      objectAssert.satisfiesAnyOf(assertions);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> satisfiesAnyOf(ThrowingConsumer<? super ACTUAL>[] assertions) {
    try {
      objectAssert.satisfiesAnyOf(assertions);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> usingComparator(Comparator<? super ACTUAL> customComparator) {
    objectAssert.usingComparator(customComparator);
    return this;
  }

  public SoftObjectAssert<ACTUAL> usingComparator(Comparator customComparator,
      String customComparatorDescription) {
    objectAssert.usingComparator(customComparator,customComparatorDescription);
    return this;
  }

  public <T> SoftObjectAssert<ACTUAL> usingComparatorForType(Comparator<? super T> comparator,
      Class<T> type) {
    try {
      objectAssert.usingComparatorForType(comparator,type);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> usingDefaultComparator() {
    objectAssert.usingDefaultComparator();
    return this;
  }

  public SoftObjectAssert<ACTUAL> usingEquals(
      BiPredicate<? super ACTUAL, ? super ACTUAL> predicate) {
    objectAssert.usingEquals(predicate);
    return this;
  }

  public SoftObjectAssert<ACTUAL> usingEquals(BiPredicate<? super ACTUAL, ? super ACTUAL> predicate,
      String customEqualsDescription) {
    objectAssert.usingEquals(predicate,customEqualsDescription);
    return this;
  }

  public SoftObjectAssert<ACTUAL> usingRecursiveAssertion() {
    try {
      objectAssert.usingRecursiveAssertion();
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public SoftObjectAssert<ACTUAL> usingRecursiveAssertion(
      RecursiveAssertionConfiguration recursiveAssertionConfiguration) {
    try {
      objectAssert.usingRecursiveAssertion(recursiveAssertionConfiguration);
      errorCollector.succeeded();
    } catch (AssertionError assertionError) {
      errorCollector.collectAssertionError(assertionError);
    }
    return this;
  }

  public RecursiveComparisonAssert<?> usingRecursiveComparison() {
    return objectAssert.usingRecursiveComparison();
  }

  public RecursiveComparisonAssert<?> usingRecursiveComparison(
      RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    return objectAssert.usingRecursiveComparison(recursiveComparisonConfiguration);
  }

  public SoftObjectAssert<ACTUAL> withFailMessage(String newErrorMessage, Object[] args) {
    objectAssert.withFailMessage(newErrorMessage,args);
    return this;
  }

  public SoftObjectAssert<ACTUAL> withFailMessage(Supplier<String> supplier) {
    objectAssert.withFailMessage(supplier);
    return this;
  }

  public SoftObjectAssert<ACTUAL> withRepresentation(Representation representation) {
    objectAssert.withRepresentation(representation);
    return this;
  }

  public SoftObjectAssert<ACTUAL> withThreadDumpOnError() {
    objectAssert.withThreadDumpOnError();
    return this;
  }
}
