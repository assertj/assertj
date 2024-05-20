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

import java.net.URI;
import org.assertj.core.api.AbstractUriAssert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author Alexander Bischof
 */
class Assertions_assertThat_with_URI_Test {

  private static URI uri;

  @BeforeAll
  static void beforeClass() {
    uri = URI.create("http://www.helloworld.org:8080/pages");
  }

  @Test
  void should_create_Assert() {
    AbstractUriAssert<?> assertions = Assertions.assertThat(uri);
    assertThat(assertions).isNotNull();
  }

}
