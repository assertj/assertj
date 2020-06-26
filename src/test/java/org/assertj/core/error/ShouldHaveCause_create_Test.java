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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveCause.shouldHaveCause;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ShouldHaveCause create")
public class ShouldHaveCause_create_Test {

  @Test
  void should_create_error_message_for_actual_cause() {
    // GIVEN
    Throwable actual = new RuntimeException();
    // WHEN
    String message = shouldHaveCause(actual).create();
    // THEN
    then(message).isEqualTo("expecting java.lang.RuntimeException to have a cause but it did not");
  }
}
