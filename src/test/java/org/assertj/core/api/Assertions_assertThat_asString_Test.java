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
package org.assertj.core.api;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

/**
 * Tests for Assert.asString() methods
 */
public class Assertions_assertThat_asString_Test {

  @Test
  public void should_pass_string_asserts_on_string_objects_with_asString() {
	Object stringAsObject = "hello world";
	assertThat(stringAsObject).asString().contains("hello");
  }

  @Test
  public void should_fail_string_asserts_on_non_string_objects_even_with_asString() {
    Object nonString = new Object();

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(nonString).asString().contains("hello"))
                                                   .withMessageContaining(format("an instance of:%n  <java.lang.String>%nbut was instance of:%n  <java.lang.Object>"));
  }

}
