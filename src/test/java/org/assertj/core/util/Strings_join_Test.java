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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.rules.ExpectedException.none;
import static org.assertj.core.util.Lists.newArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests for <code>{@link Strings#join(String...)}</code>.
 * 
 * @author Alex Ruiz
 */
public class Strings_join_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_throw_error_if_delimeter_is_null() {
    thrown.expect(IllegalArgumentException.class);
    Strings.join(null, "Uno", "Dos").with(null);
  }

  @Test
  public void should_return_empty_String_if_array_to_join_is_null() {
    assertThat( Strings.join((String[]) null).with("|")).isEmpty();
  }

  @Test
  public void should_join_using_delimeter() {
    assertThat(Strings.join("Luke", "Leia", "Han").with("|")).isEqualTo("Luke|Leia|Han");
  }

  @Test
  public void should_join_using_delimeter_and_escape() {
    assertThat(Strings.join("Luke", "Leia", "Han").with("|", "'")).isEqualTo("'Luke'|'Leia'|'Han'");
  }

  @Test
  public void should_join_using_iterable_delimeter_and_escape() {
    assertThat(Strings.join(newArrayList("Luke", "Leia", "Han")).with("|", "'")).isEqualTo("'Luke'|'Leia'|'Han'");
  }
}
