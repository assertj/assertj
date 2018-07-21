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
package org.assertj.core.api.atomic.referencearray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.util.Arrays.array;

import java.util.concurrent.atomic.AtomicReferenceArray;

import org.assertj.core.test.CartoonCharacter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AtomicReferenceArrayAssert_flatExtracting_with_String_parameter_Test {
  private CartoonCharacter bart;
  private CartoonCharacter lisa;
  private CartoonCharacter maggie;
  private CartoonCharacter homer;
  private CartoonCharacter pebbles;
  private CartoonCharacter fred;

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
    AtomicReferenceArray<CartoonCharacter> cartoonCharacters = new AtomicReferenceArray<>(array(homer, fred));
    assertThat(cartoonCharacters).flatExtracting("children")
                                 .containsOnly(bart, lisa, maggie, pebbles);
  }

  @Test
  public void should_allow_assertions_on_joined_lists_when_extracting_children_array() {
    AtomicReferenceArray<CartoonCharacter> cartoonCharacters = new AtomicReferenceArray<>(array(homer, fred));
    assertThat(cartoonCharacters).flatExtracting("childrenArray")
                                 .containsOnly(bart, lisa, maggie, pebbles);
  }

  @Test
  public void should_allow_assertions_on_empty_result_lists() {
    AtomicReferenceArray<CartoonCharacter> childCharacters = new AtomicReferenceArray<>(array(bart, lisa, maggie));
    assertThat(childCharacters).flatExtracting("children")
                               .isEmpty();
  }

  @Test
  public void should_throw_illegal_argument_exception_when_extracting_from_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(new AtomicReferenceArray<>(array(homer, null))).flatExtracting("children"));
  }

  @Test
  public void should_throw_exception_when_extracted_value_is_not_an_array_or_an_iterable() {
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(new CartoonCharacter[] { homer, fred }).flatExtracting("name")).withMessage("Flat extracting expects extracted values to be Iterables or arrays but was a String");
  }
}
