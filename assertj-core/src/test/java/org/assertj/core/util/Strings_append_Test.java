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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Strings#append(String)}</code>.
 * 
 * @author Alex Ruiz
 */
class Strings_append_Test {

  @Test
  void should_append_String() {
    assertThat(Strings.append("c").to("ab")).isEqualTo("abc");
  }

  @Test
  void should_not_append_String_if_already_present() {
    assertThat(Strings.append("c").to("abc")).isEqualTo("abc");
  }
}
