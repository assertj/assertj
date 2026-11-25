/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.error.AnyElementShouldMatch.anyElementShouldMatch;
import static org.assertj.core.error.ElementsShouldMatch.elementsShouldMatch;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfy;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfyAny;
import static org.assertj.core.error.ElementsShouldSatisfy.elementsShouldSatisfyExactly;
import static org.assertj.core.error.NoElementsShouldMatch.noElementsShouldMatch;
import static org.assertj.core.error.NoElementsShouldSatisfy.noElementsShouldSatisfy;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldSatisfy.shouldSatisfyExactlyInAnyOrder;
import static org.assertj.core.error.ShouldSatisfyOnlyOnce.shouldSatisfyOnlyOnce;
import static org.assertj.core.error.ZippedElementsShouldSatisfy.zippedElementsShouldSatisfy;
import static org.assertj.core.internal.Arrays.assertIsArray;
import static org.assertj.core.internal.CommonValidations.checkPredicateIsNotNull;
import static org.assertj.core.internal.CommonValidations.checkSizes;
import static org.assertj.core.internal.CommonValidations.hasSameSizeAsCheck;
import static org.assertj.core.util.IterableUtil.isNullOrEmpty;
import static org.assertj.core.util.IterableUtil.sizeOf;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Streams.stream;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.error.AssertionErrorCreator;
import org.assertj.core.error.UnsatisfiedRequirement;
import org.assertj.core.error.ZippedElementsShouldSatisfy.ZipSatisfyError;
import org.assertj.core.presentation.PredicateDescription;

/// Element assertions shared by iterable, object array, atomic reference array and predicate assertions: verify that
/// elements match a predicate or satisfy requirements given as consumers.
///
/// Arrays are converted to lists before calling these methods.
///
/// @author Alex Ruiz
/// @author Yvonne Wang
/// @author Maciej Jaskowski
/// @author Nicolas François
/// @author Joel Costigliola
/// @author Florent Biville
public final class GroupElementsAssertions {

  private static final AssertionErrorCreator assertionErrorCreator = AssertionErrorCreator.instance();

  private GroupElementsAssertions() {}

  /**
   * Verifies that every element satisfies the given requirements.
   *
   * @param <E> the element type
   * @param info assertion information
   * @param actual the actual iterable
   * @param requirements the element requirements
   */
  public static <E> void assertAllSatisfy(AssertionInfo info, Iterable<? extends E> actual, Consumer<? super E> requirements) {
    assertNotNull(info, actual);
    assertNotEmpty(info, actual);
    requireNonNull(requirements, "The Consumer<T> expressing the assertions requirements must not be null");

    List<UnsatisfiedRequirement> unsatisfiedRequirements = stream(actual).map(element -> failsRequirements(requirements, element))
                                                                         .flatMap(Optional::stream)
                                                                         .collect(toList());
    if (!unsatisfiedRequirements.isEmpty())
      throw assertionErrorCreator.assertionError(info, elementsShouldSatisfy(actual, unsatisfiedRequirements, info));
  }

  /**
   * Verifies that each element satisfies its corresponding requirements.
   *
   * @param <E> the element type
   * @param info assertion information
   * @param actual the actual iterable
   * @param allRequirements the requirements in element order
   */
  public static <E> void assertSatisfiesExactly(AssertionInfo info, Iterable<? extends E> actual,
                                                Consumer<? super E>[] allRequirements) {
    assertNotNull(info, actual);
    assertHasSameSizeAs(info, actual, allRequirements); // TODO
    List<E> actualAsList = newArrayList(actual);
    Map<Integer, UnsatisfiedRequirement> unsatisfiedRequirements = new HashMap<>();
    for (int i = 0; i < allRequirements.length; i++) {
      final int index = i;
      E elementAtIndex = actualAsList.get(index);
      Consumer<? super E> requirementsAtIndex = allRequirements[index];
      failsRequirements(requirementsAtIndex, elementAtIndex).ifPresent(req -> unsatisfiedRequirements.put(index, req));
    }
    if (!unsatisfiedRequirements.isEmpty())
      throw assertionErrorCreator.assertionError(info, elementsShouldSatisfyExactly(actual, unsatisfiedRequirements, info));

  }

  /**
   * Verifies that elements satisfy the requirements in any order.
   *
   * @param <E> the element type
   * @param info assertion information
   * @param actual the actual iterable
   * @param consumers the element requirements
   */
  public static <E> void assertSatisfiesExactlyInAnyOrder(AssertionInfo info, Iterable<? extends E> actual,
                                                          Consumer<? super E>[] consumers) {
    assertNotNull(info, actual);
    requireNonNull(consumers, "The Consumer<? super E>... expressing the assertions must not be null");
    for (Consumer<? super E> consumer : consumers)
      requireNonNull(consumer, "Elements in the Consumer<? super E>... expressing the assertions must not be null");

    checkSizes(actual, sizeOf(actual), consumers.length, info);
    Deque<ElementsSatisfyingConsumer<E>> satisfiedElementsPerConsumer = satisfiedElementsPerConsumer(actual, consumers);
    // fail fast check
    boolean someRequirementsAreNotMet = satisfiedElementsPerConsumer.stream().anyMatch(e -> e.getElements().isEmpty());
    if (someRequirementsAreNotMet) throw assertionErrorCreator.assertionError(info, shouldSatisfyExactlyInAnyOrder(actual));

    if (!areAllConsumersSatisfied(satisfiedElementsPerConsumer))
      throw assertionErrorCreator.assertionError(info, shouldSatisfyExactlyInAnyOrder(actual));
  }

  /**
   * Verifies that exactly one element satisfies the given requirements.
   *
   * @param <E> the element type
   * @param info assertion information
   * @param actual the actual iterable
   * @param requirements the element requirements
   */
  public static <E> void assertSatisfiesOnlyOnce(AssertionInfo info, Iterable<? extends E> actual,
                                                 Consumer<? super E> requirements) {
    assertNotNull(info, actual);
    requireNonNull(requirements, "The Consumer<? super E> expressing the requirements must not be null");
    List<E> satisfiedElements = new ArrayList<>();
    List<UnsatisfiedRequirement> unsatisfiedRequirements = new ArrayList<>();
    actual.forEach(element -> failsRequirements(requirements, element).ifPresentOrElse(unsatisfiedRequirements::add,
                                                                                       () -> satisfiedElements.add(element)));
    if (satisfiedElements.size() != 1) {
      throw assertionErrorCreator.assertionError(info, shouldSatisfyOnlyOnce(actual, satisfiedElements, unsatisfiedRequirements,
                                                                             info));
    }
  }

  /**
   * Verifies requirements against pairs of zipped elements.
   *
   * @param <ACTUAL_ELEMENT> the actual element type
   * @param <OTHER_ELEMENT> the other element type
   * @param info assertion information
   * @param actual the actual iterable
   * @param other the iterable to zip with
   * @param zipRequirements the pair requirements
   */
  public static <ACTUAL_ELEMENT, OTHER_ELEMENT> void assertZipSatisfy(AssertionInfo info,
                                                                      Iterable<? extends ACTUAL_ELEMENT> actual,
                                                                      Iterable<OTHER_ELEMENT> other,
                                                                      BiConsumer<? super ACTUAL_ELEMENT, OTHER_ELEMENT> zipRequirements) {
    assertNotNull(info, actual);
    requireNonNull(zipRequirements, "The BiConsumer expressing the assertions requirements must not be null");
    requireNonNull(other, "The iterable to zip actual with must not be null");
    assertHasSameSizeAs(info, actual, other);
    Iterator<OTHER_ELEMENT> otherIterator = other.iterator();

    List<ZipSatisfyError> errors = stream(actual).map(actualElement -> failsZipRequirements(actualElement, otherIterator.next(),
                                                                                            zipRequirements))
                                                 .flatMap(Optional::stream)
                                                 .collect(toList());
    if (!errors.isEmpty())
      throw assertionErrorCreator.assertionError(info, zippedElementsShouldSatisfy(info, actual, other, errors));
  }

  /**
   * Verifies that at least one element satisfies the requirements.
   *
   * @param <E> the element type
   * @param info assertion information
   * @param actual the actual iterable
   * @param requirements the element requirements
   */
  public static <E> void assertAnySatisfy(AssertionInfo info, Iterable<? extends E> actual, Consumer<? super E> requirements) {
    assertNotNull(info, actual);
    requireNonNull(requirements, "The Consumer<T> expressing the assertions requirements must not be null");

    List<UnsatisfiedRequirement> unsatisfiedRequirements = new ArrayList<>();
    for (E element : actual) {
      Optional<UnsatisfiedRequirement> result = failsRequirements(requirements, element);
      if (result.isEmpty()) return; // element satisfied the requirements
      unsatisfiedRequirements.add(result.get());
    }

    throw assertionErrorCreator.assertionError(info, elementsShouldSatisfyAny(actual, unsatisfiedRequirements, info));
  }

  /**
   * Verifies that every element matches the predicate.
   *
   * @param <E> the element type
   * @param info assertion information
   * @param actual the actual iterable
   * @param predicate the predicate to evaluate
   * @param predicateDescription the predicate description
   */
  public static <E> void assertAllMatch(AssertionInfo info, Iterable<? extends E> actual, Predicate<? super E> predicate,
                                        PredicateDescription predicateDescription) {
    assertNotNull(info, actual);
    checkPredicateIsNotNull(predicate);
    assertNotEmpty(info, actual);
    List<? extends E> nonMatches = stream(actual).filter(predicate.negate()).collect(toList());

    if (!nonMatches.isEmpty()) {
      throw assertionErrorCreator.assertionError(info, elementsShouldMatch(actual,
                                                                           nonMatches.size() == 1 ? nonMatches.get(0)
                                                                               : nonMatches,
                                                                           predicateDescription));
    }
  }

  /**
   * Verifies that no element satisfies the restrictions.
   *
   * @param <E> the element type
   * @param info assertion information
   * @param actual the actual iterable
   * @param restrictions the restrictions to evaluate
   */
  public static <E> void assertNoneSatisfy(AssertionInfo info, Iterable<? extends E> actual, Consumer<? super E> restrictions) {
    assertNotNull(info, actual);
    requireNonNull(restrictions, "The Consumer<T> expressing the restrictions must not be null");
    List<E> erroneousElements = stream(actual).map(element -> failsRestrictions(element, restrictions))
                                              .flatMap(Optional::stream)
                                              .collect(toList());
    if (!erroneousElements.isEmpty())
      throw assertionErrorCreator.assertionError(info, noElementsShouldSatisfy(actual, erroneousElements));
  }

  /**
   * Verifies that at least one element matches the predicate.
   *
   * @param <E> the element type
   * @param info assertion information
   * @param actual the actual iterable
   * @param predicate the predicate to evaluate
   * @param predicateDescription the predicate description
   */
  public static <E> void assertAnyMatch(AssertionInfo info, Iterable<? extends E> actual, Predicate<? super E> predicate,
                                        PredicateDescription predicateDescription) {
    assertNotNull(info, actual);
    checkPredicateIsNotNull(predicate);
    if (stream(actual).noneMatch(predicate)) {
      throw assertionErrorCreator.assertionError(info, anyElementShouldMatch(actual, predicateDescription));
    }
  }

  /**
   * Verifies that no element matches the predicate.
   *
   * @param <E> the element type
   * @param info assertion information
   * @param actual the actual iterable
   * @param predicate the predicate to evaluate
   * @param predicateDescription the predicate description
   */
  public static <E> void assertNoneMatch(AssertionInfo info, Iterable<? extends E> actual, Predicate<? super E> predicate,
                                         PredicateDescription predicateDescription) {
    assertNotNull(info, actual);
    checkPredicateIsNotNull(predicate);
    stream(actual).filter(predicate)
                  .findFirst()
                  .ifPresent(e -> {
                    throw assertionErrorCreator.assertionError(info, noElementsShouldMatch(actual, e, predicateDescription));
                  });
  }

  /**
   * Creates a predicate that matches when the given assertions pass.
   *
   * @param <T> the tested value type
   * @param assertions the assertions to evaluate
   * @return the predicate
   */
  public static <T> Predicate<T> byPassingAssertions(Consumer<? super T> assertions) {
    return objectToTest -> {
      try {
        assertions.accept(objectToTest);
        return true;
      } catch (AssertionError e) {
        return false;
      }
    };
  }

  // private section

  private static void assertNotNull(AssertionInfo info, Iterable<?> actual) {
    Objects.instance().assertNotNull(info, actual);
  }

  private static void assertNotEmpty(AssertionInfo info, Iterable<?> actual) {
    assertNotNull(info, actual);
    if (isNullOrEmpty(actual)) throw assertionErrorCreator.assertionError(info, shouldNotBeEmpty());
  }

  private static void assertHasSameSizeAs(AssertionInfo info, Iterable<?> actual, Object other) {
    assertNotNull(info, actual);
    assertIsArray(info, other);
    hasSameSizeAsCheck(info, actual, other, sizeOf(actual));
  }

  private static void assertHasSameSizeAs(AssertionInfo info, Iterable<?> actual, Iterable<?> other) {
    assertNotNull(info, actual);
    hasSameSizeAsCheck(info, actual, other, sizeOf(actual));
  }

  @SafeVarargs
  private static <E> Deque<ElementsSatisfyingConsumer<E>> satisfiedElementsPerConsumer(Iterable<? extends E> actual,
                                                                                       Consumer<? super E>... consumers) {
    return stream(consumers).map(consumer -> new ElementsSatisfyingConsumer<E>(actual, consumer))
                            .collect(toCollection(ArrayDeque::new));
  }

  private static <E> boolean areAllConsumersSatisfied(Queue<ElementsSatisfyingConsumer<E>> satisfiedElementsPerConsumer) {
    // recursively test whether we can find any specific matching permutation that can meet the requirements
    if (satisfiedElementsPerConsumer.isEmpty()) return true; // all consumers have been satisfied

    // pop the head (i.e., elements satisfying the current consumer), process the tail (i.e., remaining consumers)...
    ElementsSatisfyingConsumer<E> head = satisfiedElementsPerConsumer.remove();
    List<E> elementsSatisfyingCurrentConsumer = head.getElements();
    if (elementsSatisfyingCurrentConsumer.isEmpty()) return false;   // no element satisfies current consumer
    // if we remove an element satisfying the current consumer from all remaining consumers, will other elements still satisfy
    // the remaining consumers?
    return elementsSatisfyingCurrentConsumer.stream()
                                            .map(element -> removeElement(satisfiedElementsPerConsumer, element))
                                            .anyMatch(GroupElementsAssertions::areAllConsumersSatisfied);
  }

  private static <E> Queue<ElementsSatisfyingConsumer<E>> removeElement(Queue<ElementsSatisfyingConsumer<E>> satisfiedElementsPerConsumer,
                                                                        E element) {
    // new Queue of ElementsSatisfyingConsumer without the given element, original ElementsSatisfyingConsumer are not modified.
    return satisfiedElementsPerConsumer.stream()
                                       .map(elementsSatisfyingConsumer -> elementsSatisfyingConsumer.withoutElement(element))
                                       .collect(toCollection(ArrayDeque::new));
  }

  private static <E> Optional<UnsatisfiedRequirement> failsRequirements(Consumer<? super E> requirements, E element) {
    try {
      requirements.accept(element);
    } catch (AssertionError ex) {
      return Optional.of(new UnsatisfiedRequirement(element, ex));
    }
    return Optional.empty();
  }

  private static <ACTUAL_ELEMENT, OTHER_ELEMENT> Optional<ZipSatisfyError> failsZipRequirements(ACTUAL_ELEMENT actualElement,
                                                                                                OTHER_ELEMENT otherElement,
                                                                                                BiConsumer<ACTUAL_ELEMENT, OTHER_ELEMENT> zipRequirements) {
    try {
      zipRequirements.accept(actualElement, otherElement);
      return Optional.empty();
    } catch (AssertionError ex) {
      return Optional.of(new ZipSatisfyError(actualElement, otherElement, ex.getMessage()));
    }
  }

  private static <E> Optional<E> failsRestrictions(E element, Consumer<? super E> restrictions) {
    try {
      restrictions.accept(element);
    } catch (AssertionError e) {
      // element is supposed not to meet the given restrictions
      return Optional.empty();
    }
    // element meets the given restrictions!
    return Optional.of(element);
  }

}
