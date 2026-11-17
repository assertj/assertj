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
package org.assertj.tests.core.error;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.description.EmptyTextDescription.emptyDescription;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.presentation.Representation;
import org.junit.jupiter.api.Test;

/// Tests for the default methods [ErrorMessageFactory#create(Description)] and [ErrorMessageFactory#create()].
class ErrorMessageFactory_create_Test {

  private Description passedDescription;
  private Representation passedRepresentation;

  private final ErrorMessageFactory factory = (description, representation) -> {
    passedDescription = description;
    passedRepresentation = representation;
    return "message";
  };

  @Test
  void should_create_error_message_with_given_description_and_configured_representation() {
    // GIVEN
    Description description = new TextDescription("Test");
    // WHEN
    String message = factory.create(description);
    // THEN
    then(message).isEqualTo("message");
    then(passedDescription).isSameAs(description);
    then(passedRepresentation).isSameAs(CONFIGURATION_PROVIDER.representation());
  }

  @Test
  void should_create_error_message_with_empty_description_and_configured_representation() {
    // WHEN
    String message = factory.create();
    // THEN
    then(message).isEqualTo("message");
    then(passedDescription).isEqualTo(emptyDescription());
    then(passedRepresentation).isSameAs(CONFIGURATION_PROVIDER.representation());
  }

}
