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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.iterable;

import static java.util.Comparator.comparing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.GroupAssertTestHelper.comparatorForElementFieldsWithNamesOf;
import static org.assertj.core.api.GroupAssertTestHelper.comparatorForElementFieldsWithTypeOf;
import static org.assertj.core.api.GroupAssertTestHelper.comparatorsByTypeOf;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_STRING;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_TIMESTAMP;
import static org.assertj.core.test.AlwaysEqualComparator.alwaysEqual;

import java.sql.Timestamp;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.test.AlwaysEqualComparator;
import org.assertj.core.test.CartoonCharacter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link AbstractIterableAssert#flatExtracting(Extractor)}</code>
 *
 * @author Mateusz Haligowski
 */
public class IterableAssert_flatExtracting_with_SortedSet_Test {

  private CartoonCharacter bart;
  private CartoonCharacter lisa;
  private CartoonCharacter maggie;
  private CartoonCharacter homer;
  private CartoonCharacter pebbles;
  private CartoonCharacter fred;

  private static final ThrowingExtractor<CartoonCharacter, List<CartoonCharacter>, Exception> childrenThrowingExtractor = CartoonCharacter::getChildren;

  private static final Extractor<CartoonCharacter, List<CartoonCharacter>> children = new Extractor<CartoonCharacter, List<CartoonCharacter>>() {
    @Override
    public List<CartoonCharacter> extract(CartoonCharacter input) {
      return input.getChildren();
    }
  };

  private final ThrowingExtractor<CartoonCharacter, List<CartoonCharacter>, Exception> throwingExtractor = new ThrowingExtractor<CartoonCharacter, List<CartoonCharacter>, Exception>() {
    @Override
    public List<CartoonCharacter> extractThrows(CartoonCharacter cartoonCharacter) throws Exception {
      if (cartoonCharacter.getChildren().isEmpty()) throw new Exception("no children");
      return cartoonCharacter.getChildren();
    }
  };

  @BeforeEach
  public void setUp() {
    bart = new CartoonCharacter("Bart Simpson");
    lisa = new CartoonCharacter("Lisa Simpson");
    maggie = new CartoonCharacter("Maggie Simpson");

    homer = new CartoonCharacter("Homer Simpson");
    homer.addChildren(bart, lisa, maggie);

    pebbles = new CartoonCharacter("Pebbles Flintstone");
    fred = new CartoonCharacter("Fred Flintstone");
    fred.addChildren(pebbles);
  }

  @Test
  public void should_allow_assertions_on_joined_lists_when_extracting_children() {
    assertThat(newSortedSet(homer, fred)).flatExtracting(children)
                                         .containsOnly(bart, lisa, maggie, pebbles);
  }

  @Test
  public void should_allow_assertions_on_empty_result_lists() {
    assertThat(newSortedSet(bart, lisa, maggie)).flatExtracting(children)
                                                .isEmpty();
  }

  @Test
  public void should_throw_null_pointer_exception_when_extracting_from_null() {
    assertThatNullPointerException().isThrownBy(() -> assertThat(newSortedSet(homer, null)).flatExtracting(children));
  }

  @Test
  public void should_rethrow_throwing_extractor_checked_exception_as_a_runtime_exception() {
    SortedSet<CartoonCharacter> childCharacters = newSortedSet(bart, lisa, maggie);
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> assertThat(childCharacters).flatExtracting(cartoonCharacter -> {
      if (cartoonCharacter.getChildren().isEmpty()) throw new Exception("no children");
      return cartoonCharacter.getChildren();
    })).withMessage("java.lang.Exception: no children");
  }

  @Test
  public void should_let_throwing_extractor_runtime_exception_bubble_up() {
    SortedSet<CartoonCharacter> childCharacters = newSortedSet(bart, lisa, maggie);
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> assertThat(childCharacters).flatExtracting(cartoonCharacter -> {
      if (cartoonCharacter.getChildren().isEmpty()) throw new RuntimeException("no children");
      return cartoonCharacter.getChildren();
    })).withMessage("no children");
  }

  @Test
  public void should_allow_assertions_on_joined_lists_when_extracting_children_with_throwing_extractor() {
    SortedSet<CartoonCharacter> cartoonCharacters = newSortedSet(homer, fred);
    assertThat(cartoonCharacters).flatExtracting(cartoonCharacter -> {
      if (cartoonCharacter.getChildren().isEmpty()) throw new Exception("no children");
      return cartoonCharacter.getChildren();
    }).containsOnly(bart, lisa, maggie, pebbles);
  }

  @Test
  public void should_allow_assertions_on_joined_lists_when_extracting_children_with_anonymous_class_throwing_extractor() {
    SortedSet<CartoonCharacter> cartoonCharacters = newSortedSet(homer, fred);
    assertThat(cartoonCharacters).flatExtracting(new ThrowingExtractor<CartoonCharacter, List<CartoonCharacter>, Exception>() {
      @Override
      public List<CartoonCharacter> extractThrows(CartoonCharacter cartoonCharacter) throws Exception {
        if (cartoonCharacter.getChildren().isEmpty()) throw new Exception("no children");
        return cartoonCharacter.getChildren();
      }
    }).containsOnly(bart, lisa, maggie, pebbles);
  }

  @Test
  public void should_keep_existing_description_if_set_when_extracting_using_extractor() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(newSortedSet(homer)).as("expected description")
                                                                                                    .flatExtracting(children)
                                                                                                    .isEmpty())
                                                   .withMessageContaining("[expected description]");
  }

  @Test
  public void should_keep_existing_description_if_set_when_extracting_using_single_field_name() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(newSortedSet(homer)).as("expected description")
                                                                                                    .flatExtracting("children")
                                                                                                    .isEmpty())
                                                   .withMessageContaining("[expected description]");
  }

  @Test
  public void should_keep_existing_description_if_set_when_extracting_using_multiple_field_names() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(newSortedSet(homer)).as("expected description")
                                                                                                    .flatExtracting("children",
                                                                                                                    "name")
                                                                                                    .isEmpty())
                                                   .withMessageContaining("[expected description]");
  }

  public void should_keep_existing_description_if_set_when_extracting_using_multiple_extractors_varargs() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(newSortedSet(homer)).as("expected description")
                                                                                                    .flatExtracting(children,
                                                                                                                    children)
                                                                                                    .isEmpty())
                                                   .withMessageContaining("[expected description]");
  }

  @Test
  public void should_keep_existing_description_if_set_when_extracting_using_multiple_throwing_extractors_varargs() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(newSortedSet(homer)).as("expected description")
                                                                                                    .flatExtracting(throwingExtractor,
                                                                                                                    throwingExtractor)
                                                                                                    .isEmpty())
                                                   .withMessageContaining("[expected description]");
  }

  @Test
  public void flatExtracting_should_keep_assertion_state() {
    // GIVEN
    AlwaysEqualComparator<CartoonCharacter> cartoonCharacterAlwaysEqualComparator = alwaysEqual();
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    // @format:off
    AbstractListAssert<?, ?, ?, ?> assertion
            = assertThat(newSortedSet(homer, fred)).as("test description")
                                                   .withFailMessage("error message")
                                                   .withRepresentation(UNICODE_REPRESENTATION)
                                                   .usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING, "foo")
                                                   .usingComparatorForElementFieldsWithType(ALWAY_EQUALS_TIMESTAMP, Timestamp.class)
                                                   .flatExtracting(children)
                                                   .usingComparatorForType(cartoonCharacterAlwaysEqualComparator, CartoonCharacter.class)
                                                   .contains(bart, lisa, new CartoonCharacter("Unknown"));
    // @format:on
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).get(CartoonCharacter.class)).isSameAs(cartoonCharacterAlwaysEqualComparator);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).get(Timestamp.class)).isSameAs(ALWAY_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAY_EQUALS_STRING);
  }

  @Test
  public void flatExtracting_with_ThrowingExtractor_should_keep_assertion_state() {
    // GIVEN
    AlwaysEqualComparator<CartoonCharacter> cartoonCharacterAlwaysEqualComparator = alwaysEqual();
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    // @format:off
    AbstractListAssert<?, ?, ?, ?> assertion
           = assertThat(newSortedSet(homer, fred)).as("test description")
                                                  .withFailMessage("error message")
                                                  .withRepresentation(UNICODE_REPRESENTATION)
                                                  .usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING, "foo")
                                                  .usingComparatorForElementFieldsWithType(ALWAY_EQUALS_TIMESTAMP, Timestamp.class)
                                                  .flatExtracting(childrenThrowingExtractor)
                                                  .usingComparatorForType(cartoonCharacterAlwaysEqualComparator, CartoonCharacter.class)
                                                  .contains(bart, lisa, new CartoonCharacter("Unknown"));
    // @format:on
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).get(CartoonCharacter.class)).isSameAs(cartoonCharacterAlwaysEqualComparator);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).get(Timestamp.class)).isSameAs(ALWAY_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAY_EQUALS_STRING);
  }

  private static SortedSet<CartoonCharacter> newSortedSet(CartoonCharacter... cartoonCharacters) {
    TreeSet<CartoonCharacter> cartoonCharacterSortedSet = new TreeSet<>(comparing(CartoonCharacter::getName));
    for (CartoonCharacter cartoonCharacter : cartoonCharacters) {
      cartoonCharacterSortedSet.add(cartoonCharacter);
    }
    return cartoonCharacterSortedSet;
  }

}
