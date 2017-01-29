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
 * Tests for <code>{@link Strings#concat(Object...)}</code>.
 * 
 * @author Alex Ruiz
 */
public class Strings_concat_Test {

  @Test
  public void should_return_null_if_array_is_null() {
    assertThat(Strings.concat((Object[]) null)).isNull();
  }

  @Test
  public void should_concatenate_given_Strings() {
    assertThat(Strings.concat("One", "Two", "Three")).isEqualTo("OneTwoThree");
  }
}
