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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.objectarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.GroupAssertTestHelper.comparatorForElementFieldsWithNamesOf;
import static org.assertj.core.api.GroupAssertTestHelper.comparatorForElementFieldsWithTypeOf;
import static org.assertj.core.api.GroupAssertTestHelper.comparatorsByTypeOf;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAYS_EQUALS_STRING;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAYS_EQUALS_TIMESTAMP;
import static org.assertj.core.test.AlwaysEqualComparator.alwaysEqual;
import static org.assertj.core.util.Arrays.array;

import java.sql.Timestamp;

import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.test.AlwaysEqualComparator;
import org.assertj.core.test.CartoonCharacter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObjectArrayAssert_flatExtracting_with_String_parameter_Test {
  private CartoonCharacter bart;
  private CartoonCharacter lisa;
  private CartoonCharacter maggie;
  private CartoonCharacter homer;
  private CartoonCharacter pebbles;
  private CartoonCharacter fred;

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
    assertThat(array(homer, fred)).flatExtracting("children")
                                  .containsOnly(bart, lisa, maggie, pebbles);
  }

  @Test
  void should_allow_assertions_on_joined_lists_when_extracting_children_array() {
    assertThat(array(homer, fred)).flatExtracting("childrenArray")
                                  .containsOnly(bart, lisa, maggie, pebbles);
  }

  @Test
  void should_allow_assertions_on_empty_result_lists() {
    assertThat(array(bart, lisa, maggie)).flatExtracting("children")
                                         .isEmpty();
  }

  @Test
  void should_throw_illegal_argument_exception_when_extracting_from_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(array(homer, null)).flatExtracting("children"));
  }

  @Test
  void should_throw_exception_when_extracted_value_is_not_an_array_or_an_iterable() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(array(homer, fred)).flatExtracting("name"))
                                        .withMessage("Flat extracting expects extracted values to be Iterables or arrays but was a String");
  }

  @Test
  void flatExtracting_should_keep_assertion_state() {
    // GIVEN
    AlwaysEqualComparator<CartoonCharacter> cartoonCharacterAlwaysEqualComparator = alwaysEqual();
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    // @format:off
    AbstractListAssert<?, ?, ?, ?> assertion
             = assertThat(array(homer, fred)).as("test description")
                                             .withFailMessage("error message")
                                             .withRepresentation(UNICODE_REPRESENTATION)
                                             .usingComparatorForElementFieldsWithNames(ALWAYS_EQUALS_STRING, "foo")
                                             .usingComparatorForElementFieldsWithType(ALWAYS_EQUALS_TIMESTAMP, Timestamp.class)
                                             .usingComparatorForType(cartoonCharacterAlwaysEqualComparator, CartoonCharacter.class)
                                             .flatExtracting("children")
                                             .contains(bart, lisa, new CartoonCharacter("Unknown"));
  // @format:on
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).getComparatorForType(CartoonCharacter.class)).isSameAs(cartoonCharacterAlwaysEqualComparator);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).getComparatorForType(Timestamp.class)).isSameAs(ALWAYS_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAYS_EQUALS_STRING);
  }
}
