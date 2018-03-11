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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.test.CartoonCharacter;
import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link AbstractIterableAssert#flatExtracting(Extractor)}</code>
 * 
 * @author Mateusz Haligowski
 */
public class IterableAssert_flatExtracting_Test {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private CartoonCharacter bart;
  private CartoonCharacter lisa;
  private CartoonCharacter maggie;
  private CartoonCharacter homer;
  private CartoonCharacter pebbles;
  private CartoonCharacter fred;

  private final Extractor<CartoonCharacter, List<CartoonCharacter>> children = new Extractor<CartoonCharacter, List<CartoonCharacter>>() {
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

  @Before
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
    assertThat(newArrayList(homer, fred)).flatExtracting(children).containsOnly(bart, lisa, maggie, pebbles);
  }

  @Test
  public void should_allow_assertions_on_empty_result_lists() {
    assertThat(newArrayList(bart, lisa, maggie)).flatExtracting(children).isEmpty();
  }

  @Test
  public void should_throw_null_pointer_exception_when_extracting_from_null() {
    thrown.expectNullPointerException();
    assertThat(newArrayList(homer, null)).flatExtracting(children);
  }

  @Test
  public void should_rethrow_throwing_extractor_checked_exception_as_a_runtime_exception() {
    List<CartoonCharacter> childCharacters = newArrayList(bart, lisa, maggie);
    thrown.expect(RuntimeException.class, "java.lang.Exception: no children");
    assertThat(childCharacters).flatExtracting(cartoonCharacter -> {
      if (cartoonCharacter.getChildren().isEmpty()) throw new Exception("no children");
      return cartoonCharacter.getChildren();
    });
  }

  @Test
  public void should_let_throwing_extractor_runtime_exception_bubble_up() {
    List<CartoonCharacter> childCharacters = newArrayList(bart, lisa, maggie);
    thrown.expect(RuntimeException.class, "no children");
    assertThat(childCharacters).flatExtracting(cartoonCharacter -> {
      if (cartoonCharacter.getChildren().isEmpty()) throw new RuntimeException("no children");
      return cartoonCharacter.getChildren();
    });
  }

  @Test
  public void should_allow_assertions_on_joined_lists_when_extracting_children_with_throwing_extractor() {
    List<CartoonCharacter> cartoonCharacters = newArrayList(homer, fred);
    assertThat(cartoonCharacters).flatExtracting(cartoonCharacter -> {
      if (cartoonCharacter.getChildren().isEmpty()) throw new Exception("no children");
      return cartoonCharacter.getChildren();
    }).containsOnly(bart, lisa, maggie, pebbles);
  }

  @Test
  public void should_allow_assertions_on_joined_lists_when_extracting_children_with_anonymous_class_throwing_extractor() {
    List<CartoonCharacter> cartoonCharacters = newArrayList(homer, fred);
    assertThat(cartoonCharacters).flatExtracting(new ThrowingExtractor<CartoonCharacter, List<CartoonCharacter>, Exception>() {
      @Override
      public List<CartoonCharacter> extractThrows(CartoonCharacter cartoonCharacter) throws Exception {
        if (cartoonCharacter.getChildren().isEmpty()) throw new Exception("no children");
        return cartoonCharacter.getChildren();
      }
    }).containsOnly(bart, lisa, maggie, pebbles);
  }

  @Test
  public void should_keep_existing_description_if_set_when_extracting_using_multiple_extractors_varargs() {
    thrown.expectAssertionErrorWithMessageContaining("[expected description]");

    assertThat(newArrayList(homer)).as("expected description").flatExtracting(children, children).isEmpty();
  }

  @Test
  public void should_keep_existing_description_if_set_when_extracting_using_multiple_throwing_extractors_varargs() {
    thrown.expectAssertionErrorWithMessageContaining("[expected description]");

    assertThat(newArrayList(homer)).as("expected description").flatExtracting(throwingExtractor, throwingExtractor)
                                   .isEmpty();
  }

  @Test
  public void should_keep_existing_description_if_set_when_extracting_using_extractor() {
    thrown.expectAssertionErrorWithMessageContaining("[expected description]");

    assertThat(newArrayList(homer)).as("expected description").flatExtracting(children).isEmpty();
  }

  @Test
  public void should_keep_existing_description_if_set_when_extracting_using_single_field_name() {
    thrown.expectAssertionErrorWithMessageContaining("[expected description]");

    assertThat(newArrayList(homer)).as("expected description").flatExtracting("children").isEmpty();
  }

  @Test
  public void should_keep_existing_description_if_set_when_extracting_using_multiple_field_names() {
    thrown.expectAssertionErrorWithMessageContaining("[expected description]");

    assertThat(newArrayList(homer)).as("expected description").flatExtracting("children", "name").isEmpty();
  }

}
