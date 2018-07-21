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
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Strings#quote(Object)}</code>.
 * 
 * @author Alex Ruiz
 */
public class Strings_quoteObject_Test {

  @Test
  public void should_not_quote_Object_that_is_not_String() {
    assertThat(Strings.quote(9)).isEqualTo(9);
  }

  @Test
  public void should_quote_Object_that_is_String() {
    Object o = "Hello";
    assertThat(Strings.quote(o)).isEqualTo("'Hello'");
  }
}
