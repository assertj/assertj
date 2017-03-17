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
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.Lists.newArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.assertj.core.test.ExpectedException;

/**
 * Tests for <code>{@link Strings#join(String...)}</code>.
 * 
 * @author Alex Ruiz
 */
public class Strings_join_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_throw_error_if_delimiter_is_null() {
    thrown.expectIllegalArgumentException();
    Strings.join(null, "Uno", "Dos").with(null);
  }

  @Test
  public void should_return_empty_String_if_array_to_join_is_null() {
    assertThat( Strings.join((String[]) null).with("|")).isEmpty();
  }

  @Test
  public void should_join_using_delimiter() {
    assertThat(Strings.join("Luke", "Leia", "Han").with("|")).isEqualTo("Luke|Leia|Han");
  }

  @Test
  public void should_join_using_delimiter_and_escape() {
    assertThat(Strings.join("Luke", "Leia", "Han").with("|", "'")).isEqualTo("'Luke'|'Leia'|'Han'");
  }

  @Test
  public void should_join_using_iterable_delimiter_and_escape() {
    assertThat(Strings.join(newArrayList("Luke", "Leia", "Han")).with("|", "'")).isEqualTo("'Luke'|'Leia'|'Han'");
  }
}
