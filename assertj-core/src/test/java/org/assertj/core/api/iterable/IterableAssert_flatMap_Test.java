/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import java.util.function.Function;
import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.testkit.CartoonCharacter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link AbstractIterableAssert#flatMap(Function)}</code>
 *
 * @author Mateusz Haligowski
 */
class IterableAssert_flatMap_Test {

  private CartoonCharacter bart;
  private CartoonCharacter lisa;
  private CartoonCharacter maggie;
  private CartoonCharacter homer;
  private CartoonCharacter pebbles;
  private CartoonCharacter fred;

  private static final ThrowingExtractor<CartoonCharacter, List<CartoonCharacter>, Exception> childrenThrowingExtractor = CartoonCharacter::getChildren;

  private static final Function<CartoonCharacter, List<CartoonCharacter>> children = CartoonCharacter::getChildren;

  @BeforeEach
  void setUp() {
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
  void should_allow_assertions_on_joined_lists_when_extracting_children() {
    assertThat(list(homer, fred)).flatMap(children)
                                 .containsOnly(bart, lisa, maggie, pebbles);
  }

  @Test
  void should_allow_assertions_on_empty_result_lists() {
    assertThat(list(bart, lisa, maggie)).flatMap(children)
                                        .isEmpty();
  }

  @Test
  void should_throw_assertion_error_if_actual_is_null() {
    // GIVEN
    List<CartoonCharacter> simpsons = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(simpsons).flatMap(children));
    // THEN
    then(assertionError).hasMessage("Expecting actual not to be null");
  }

  @Test
  void should_bubble_up_null_pointer_exception_from_lambda_extractor() {
    // GIVEN
    List<CartoonCharacter> cartoonCharacters = list(homer, null);
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> assertThat(cartoonCharacters).flatMap(children));
  }

  @Test
  void should_rethrow_throwing_extractor_checked_exception_as_a_runtime_exception() {
    // GIVEN
    List<CartoonCharacter> childCharacters = list(bart, lisa, maggie);
    // WHEN
    RuntimeException runtimeException = catchThrowableOfType(() -> assertThat(childCharacters).flatMap(cartoonCharacter -> {
      if (cartoonCharacter.getChildren().isEmpty()) throw new Exception("no children");
      return cartoonCharacter.getChildren();
    }), RuntimeException.class);
    // THEN
    then(runtimeException).hasMessage("java.lang.Exception: no children");
  }

  @Test
  void should_let_throwing_extractor_runtime_exception_bubble_up() {
    // GIVEN
    List<CartoonCharacter> childCharacters = list(bart, lisa, maggie);
    // WHEN
    RuntimeException runtimeException = catchThrowableOfType(() -> assertThat(childCharacters).flatMap(cartoonCharacter -> {
      if (cartoonCharacter.getChildren().isEmpty()) throw new RuntimeException("no children");
      return cartoonCharacter.getChildren();
    }), RuntimeException.class);
    // THEN
    then(runtimeException).hasMessage("no children");
  }

  @Test
  void should_allow_assertions_on_joined_lists_when_extracting_children_with_throwing_extractor() {
    // GIVEN
    List<CartoonCharacter> cartoonCharacters = list(homer, fred);
    // WHEN/THEN
    assertThat(cartoonCharacters).flatMap(cartoonCharacter -> {
      if (cartoonCharacter.getChildren().isEmpty()) throw new Exception("no children");
      return cartoonCharacter.getChildren();
    }).containsOnly(bart, lisa, maggie, pebbles);
  }

  @Test
  void should_allow_assertions_on_joined_lists_when_extracting_children_with_anonymous_class_throwing_extractor() {
    // GIVEN
    List<CartoonCharacter> cartoonCharacters = list(homer, fred);
    // WHEN/THEN
    assertThat(cartoonCharacters).flatMap(new ThrowingExtractor<CartoonCharacter, List<CartoonCharacter>, Exception>() {
      @Override
      public List<CartoonCharacter> extractThrows(CartoonCharacter cartoonCharacter) throws Exception {
        if (cartoonCharacter.getChildren().isEmpty()) throw new Exception("no children");
        return cartoonCharacter.getChildren();
      }
    }).containsOnly(bart, lisa, maggie, pebbles);
  }

  @Test
  void should_keep_existing_description_if_set_when_extracting_using_function() {
    // GIVEN
    List<CartoonCharacter> cartoonCharacters = list(homer);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(cartoonCharacters).as("expected description")
                                                                                            .flatMap(children)
                                                                                            .isEmpty());
    // THEN
    then(assertionError).hasMessageContaining("[expected description]");
  }

  @Test
  void should_keep_existing_description_if_set_when_extracting_using_multiple_function_varargs() {
    // GIVEN
    List<CartoonCharacter> cartoonCharacters = list(homer);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(cartoonCharacters).as("expected description")
                                                                                            .flatMap(children, children)
                                                                                            .isEmpty());
    // THEN
    then(assertionError).hasMessageContaining("[expected description]");
  }

  @Test
  void should_keep_existing_description_if_set_when_extracting_using_multiple_throwing_extractors_varargs() {
    // GIVEN
    List<CartoonCharacter> cartoonCharacters = list(homer);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(cartoonCharacters).as("expected description")
                                                                                            .flatMap(childrenThrowingExtractor,
                                                                                                     childrenThrowingExtractor)
                                                                                            .isEmpty());
    // THEN
    then(assertionError).hasMessageContaining("[expected description]");
  }

}
