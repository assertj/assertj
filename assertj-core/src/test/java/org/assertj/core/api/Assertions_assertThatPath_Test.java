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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThatPath;
import static org.assertj.core.api.BDDAssertions.then;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Assertions_assertThatPath_Test {

  private Path actual;

  @BeforeEach
  void before() {
    actual = Paths.get(".");
  }

  @Test
  void should_create_Assert() {
    // GIVEN
    AbstractPathAssert<?> assertions = assertThatPath(actual);
    // WHEN/THEN
    then(assertions).isNotNull();
  }

  @Test
  void should_pass_actual() {
    // GIVEN
    AbstractPathAssert<?> assertions = assertThatPath(actual);
    // WHEN/THEN
    then(actual).isSameAs(assertions.actual);
  }
}
