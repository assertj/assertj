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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDSoftAssertions.thenSoftly;
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecursiveComparisonConfiguration_typeMessages_Test {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  void setUp() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  @Test
  void should_register_custom_message_for_type() {
    // GIVEN
    String message = "field message";
    Class<String> type = String.class;
    // WHEN
    recursiveComparisonConfiguration.registerErrorMessageForType(message, type);
    // THEN
    thenSoftly(softly -> {
      softly.then(recursiveComparisonConfiguration.hasCustomMessageForType(type)).isTrue();
      softly.then(recursiveComparisonConfiguration.getMessageForType(type)).isEqualTo(message);
    });
  }

  @Test
  void should_register_custom_message_with_arguments_for_type() {
    // GIVEN
    String message = "field message with arguments %s and %s";
    List<Object> args = list("one", "two");
    Class<String> type = String.class;
    // WHEN
    recursiveComparisonConfiguration.registerErrorMessageForType(message, args, type);
    // THEN
    thenSoftly(softly -> {
      softly.then(recursiveComparisonConfiguration.hasCustomMessageForType(type)).isTrue();
      softly.then(recursiveComparisonConfiguration.getMessageForType(type)).isEqualTo(format(message, args.toArray()));
    });
  }

  @Test
  void should_throw_NPE_if_arguments_list_is_null() {
    // GIVEN
    String message = "field message";
    Class<String> type = String.class;
    List<Object> args = null;
    // WHEN
    Throwable throwable = catchThrowable(
        () -> recursiveComparisonConfiguration.registerErrorMessageForType(message, args, type));
    // THEN
    then(throwable).isInstanceOf(NullPointerException.class).hasMessage("Arguments list must not be null");
  }
}
