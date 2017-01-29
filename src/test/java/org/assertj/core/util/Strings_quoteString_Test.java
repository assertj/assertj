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

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;

/**
 * Tests for <code>{@link Strings#quote(String)}</code>.
 * 
 * @author Alex Ruiz
 */
public class Strings_quoteString_Test {

  @Test
  public void should_quote_String() {
    assertThat(Strings.quote("foo")).isEqualTo("'foo'");
  }

  @Test
  public void should_quote_empty_String() {
    assertThat(Strings.quote("")).isEqualTo("''");
  }

  @Test
  public void should_return_null_if_String_is_null() {
    assertThat(Strings.quote(null)).isNull();
  }
}
