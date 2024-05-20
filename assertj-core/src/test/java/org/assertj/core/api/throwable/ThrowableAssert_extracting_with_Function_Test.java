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
package org.assertj.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ThrowableAssert_extracting_with_Function_Test {

  @Test
  void should_allow_type_specific_extractor() {
    // GIVEN
    Exception cause = new Exception("boom!");
    ClassNotFoundException exception = new ClassNotFoundException("message", cause);
    // WHEN/THEN
    assertThat(exception).extracting(ClassNotFoundException::getException)
                         .isSameAs(cause);
  }

}
