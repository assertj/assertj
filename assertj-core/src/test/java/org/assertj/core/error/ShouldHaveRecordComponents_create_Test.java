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
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveRecordComponents.shouldHaveRecordComponents;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Sets.set;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

class ShouldHaveRecordComponents_create_Test {

  @Test
  void should_create_error_message() {
    // WHEN
    String message = shouldHaveRecordComponents(Object.class, set("component1", "component2"),
                                                set("component2")).create(new TestDescription("TEST"),
                                                                          STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[TEST] %n" +
                                   "Expecting%n" +
                                   "  java.lang.Object%n" +
                                   "to have the following record components:%n" +
                                   "  [\"component1\", \"component2\"]%n" +
                                   "but it doesn't have:%n" +
                                   "  [\"component2\"]"));
  }

}
