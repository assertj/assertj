/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Comparator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.assertj.core.description.Description;
import org.assertj.core.presentation.Representation;

/**
 * @param <ACTUAL> 
 *
 * @author Stefano Cordio
 * @since 3.19.0
 */
public class ComparatorBasedObjectAssert<ACTUAL> extends ObjectAssert<ACTUAL>
    implements ComparisonBasedAssert<ComparatorBasedObjectAssert<ACTUAL>, ACTUAL> {

  public ComparatorBasedObjectAssert(ACTUAL actual, AbstractAssert<?, ?> sourceAssert,
                                     Comparator<? super ACTUAL> customComparator, String customComparatorDescription) {
    super(actual);
    withAssertionState(sourceAssert).usingComparator(customComparator, customComparatorDescription);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isEqualByComparingTo(ACTUAL other) {
    objects.assertEqualByComparison(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isNotEqualByComparingTo(ACTUAL other) {
    objects.assertNotEqualByComparison(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isLessThan(ACTUAL other) {
    objects.assertLessThan(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isLessThanOrEqualTo(ACTUAL other) {
    objects.assertLessThanOrEqualTo(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isGreaterThan(ACTUAL other) {
    objects.assertGreaterThan(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isGreaterThanOrEqualTo(ACTUAL other) {
    objects.assertGreaterThanOrEqualTo(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isBetween(ACTUAL startInclusive, ACTUAL endInclusive) {
    objects.assertIsBetween(info, actual, startInclusive, endInclusive, true, true);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isStrictlyBetween(ACTUAL startExclusive, ACTUAL endExclusive) {
    objects.assertIsBetween(info, actual, startExclusive, endExclusive, false, false);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> as(Description description) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.as(description);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> as(String description, Object... args) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.as(description, args);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> hasNoNullFieldsOrProperties() {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.hasNoNullFieldsOrProperties();
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> hasAllNullFieldsOrProperties() {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.hasAllNullFieldsOrProperties();
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> hasNoNullFieldsOrPropertiesExcept(String... propertiesOrFieldsToIgnore) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.hasNoNullFieldsOrPropertiesExcept(propertiesOrFieldsToIgnore);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> hasAllNullFieldsOrPropertiesExcept(String... propertiesOrFieldsToIgnore) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.hasAllNullFieldsOrPropertiesExcept(propertiesOrFieldsToIgnore);
  }

  /** {@inheritDoc} */
  @Override
  public <T> ComparatorBasedObjectAssert<ACTUAL> usingComparatorForFields(Comparator<T> comparator,
                                                                          String... propertiesOrFields) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.usingComparatorForFields(comparator, propertiesOrFields);
  }

  /** {@inheritDoc} */
  @Override
  public <T> ComparatorBasedObjectAssert<ACTUAL> usingComparatorForType(Comparator<? super T> comparator, Class<T> type) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.usingComparatorForType(comparator, type);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> hasFieldOrProperty(String name) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.hasFieldOrProperty(name);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> hasFieldOrPropertyWithValue(String name, Object value) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.hasFieldOrPropertyWithValue(name, value);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> hasOnlyFields(String... expectedFieldNames) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.hasOnlyFields(expectedFieldNames);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> describedAs(Description description) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.describedAs(description);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isEqualTo(Object expected) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.isEqualTo(expected);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isNotEqualTo(Object other) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.isNotEqualTo(other);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isNotNull() {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.isNotNull();
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isSameAs(Object expected) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.isSameAs(expected);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isNotSameAs(Object other) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.isNotSameAs(other);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isIn(Object... values) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.isIn(values);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isNotIn(Object... values) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.isNotIn(values);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isIn(Iterable<?> values) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.isIn(values);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isNotIn(Iterable<?> values) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.isNotIn(values);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> is(Condition<? super ACTUAL> condition) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.is(condition);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isNot(Condition<? super ACTUAL> condition) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.isNot(condition);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> has(Condition<? super ACTUAL> condition) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.has(condition);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> doesNotHave(Condition<? super ACTUAL> condition) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.doesNotHave(condition);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> satisfies(Condition<? super ACTUAL> condition) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.satisfies(condition);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isInstanceOf(Class<?> type) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.isInstanceOf(type);
  }

  /** {@inheritDoc} */
  @Override
  public <T> ComparatorBasedObjectAssert<ACTUAL> isInstanceOfSatisfying(Class<T> type, Consumer<T> requirements) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.isInstanceOfSatisfying(type, requirements);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isInstanceOfAny(Class<?>... types) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.isInstanceOfAny(types);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isNotInstanceOf(Class<?> type) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.isNotInstanceOf(type);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isNotInstanceOfAny(Class<?>... types) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.isNotInstanceOfAny(types);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> hasSameClassAs(Object other) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.hasSameClassAs(other);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> hasToString(String expectedToString) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.hasToString(expectedToString);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> doesNotHaveToString(String otherToString) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.doesNotHaveToString(otherToString);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> doesNotHaveSameClassAs(Object other) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.doesNotHaveSameClassAs(other);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isExactlyInstanceOf(Class<?> type) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.isExactlyInstanceOf(type);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isNotExactlyInstanceOf(Class<?> type) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.isNotExactlyInstanceOf(type);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isOfAnyClassIn(Class<?>... types) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.isOfAnyClassIn(types);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> isNotOfAnyClassIn(Class<?>... types) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.isNotOfAnyClassIn(types);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> overridingErrorMessage(String newErrorMessage, Object... args) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.overridingErrorMessage(newErrorMessage, args);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> overridingErrorMessage(Supplier<String> supplier) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.overridingErrorMessage(supplier);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> withFailMessage(String newErrorMessage, Object... args) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.withFailMessage(newErrorMessage, args);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> withFailMessage(Supplier<String> supplier) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.withFailMessage(supplier);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> usingDefaultComparator() {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.usingDefaultComparator();
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> withThreadDumpOnError() {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.withThreadDumpOnError();
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> withRepresentation(Representation representation) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.withRepresentation(representation);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> matches(Predicate<? super ACTUAL> predicate) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.matches(predicate);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> matches(Predicate<? super ACTUAL> predicate, String predicateDescription) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.matches(predicate, predicateDescription);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> satisfies(Consumer<ACTUAL> requirements) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.satisfies(requirements);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> satisfiesAnyOf(Consumer<ACTUAL> assertions1, Consumer<ACTUAL> assertions2) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.satisfiesAnyOf(assertions1, assertions2);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> satisfiesAnyOf(Consumer<ACTUAL> assertions1, Consumer<ACTUAL> assertions2,
                                                            Consumer<ACTUAL> assertions3) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.satisfiesAnyOf(assertions1, assertions2, assertions3);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> satisfiesAnyOf(Consumer<ACTUAL> assertions1, Consumer<ACTUAL> assertions2,
                                                            Consumer<ACTUAL> assertions3, Consumer<ACTUAL> assertions4) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.satisfiesAnyOf(assertions1, assertions2, assertions3, assertions4);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> hasSameHashCodeAs(Object other) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.hasSameHashCodeAs(other);
  }

  /** {@inheritDoc} */
  @Override
  public ComparatorBasedObjectAssert<ACTUAL> doesNotHaveSameHashCodeAs(Object other) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.doesNotHaveSameHashCodeAs(other);
  }

  /** {@inheritDoc} */
  @Override
  public <T> ComparatorBasedObjectAssert<ACTUAL> returns(T expected, Function<ACTUAL, T> from) {
    return (ComparatorBasedObjectAssert<ACTUAL>) super.returns(expected, from);
  }

}
