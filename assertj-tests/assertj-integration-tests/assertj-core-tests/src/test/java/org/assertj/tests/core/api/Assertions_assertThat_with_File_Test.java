/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import org.assertj.core.api.AbstractFileAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Yvonne Wang
 */
class Assertions_assertThat_with_File_Test {

  private static File actual;

  @BeforeAll
  static void setUpOnce() {
    actual = new File("xyz");
  }

  @Test
  void should_create_Assert() {
    AbstractFileAssert<?> assertions = Assertions.assertThat(actual);
    assertThat(assertions).isNotNull();
  }

}
