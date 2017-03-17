/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

import org.assertj.core.test.CartoonCharacter;
import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.api.AbstractIterableAssert#flatExtracting(org.assertj.core.api.iterable.Extractor)}</code>
 *
 * @author Alexander Bischof
 */
public class IterableAssert_flatExtracting_with_String_parameter_Test {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private CartoonCharacter bart;
  private CartoonCharacter lisa;
  private CartoonCharacter maggie;
  private CartoonCharacter homer;
  private CartoonCharacter pebbles;
  private CartoonCharacter fred;

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
    assertThat(newArrayList(homer, fred)).flatExtracting("children").containsOnly(bart, lisa, maggie, pebbles);
  }

  @Test
  public void should_allow_assertions_on_joined_lists_when_extracting_children_array() {
    assertThat(newArrayList(homer, fred)).flatExtracting("childrenArray").containsOnly(bart, lisa, maggie, pebbles);
  }

  @Test
  public void should_allow_assertions_on_empty_result_lists() {
    assertThat(newArrayList(bart, lisa, maggie)).flatExtracting("children").isEmpty();
  }

  @Test
  public void should_throw_exception_when_extracting_from_null() {
    thrown.expectIllegalArgumentException();
    assertThat(newArrayList(homer, null)).flatExtracting("children");
  }

  @Test
  public void should_throw_exception_when_extracted_value_is_not_an_array_or_an_iterable() {
    thrown.expectIllegalArgumentException("Flat extracting expects extracted values to be Iterables or arrays but was a String");
    assertThat(newArrayList(homer, fred)).flatExtracting("name");
  }
}
