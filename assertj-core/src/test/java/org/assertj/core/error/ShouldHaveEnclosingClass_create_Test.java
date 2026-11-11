/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveEnclosingClass.shouldHaveEnclosingClass;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import java.util.Map;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

class ShouldHaveEnclosingClass_create_Test {

  @Test
  void should_create_error_message_if_actual_has_enclosing_class() {
    // WHEN
    String message = shouldHaveEnclosingClass(Map.Entry.class, Integer.class).create(new TestDescription("TEST"),
                                                                                     STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[TEST] %n" +
                                   "Expecting%n" +
                                   "  java.util.Map.Entry%n" +
                                   "to have enclosing class:%n" +
                                   "  java.lang.Integer%n" +
                                   "but had:%n" +
                                   "  java.util.Map"));
  }

  @Test
  void should_create_error_message_if_actual_has_no_enclosing_class() {
    // WHEN
    String message = shouldHaveEnclosingClass(String.class, Integer.class).create(new TestDescription("TEST"),
                                                                                  STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[TEST] %n" +
                                   "Expecting%n" +
                                   "  java.lang.String%n" +
                                   "to have enclosing class:%n" +
                                   "  java.lang.Integer%n" +
                                   "but had none."));
  }

}
