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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveAnnotations.shouldHaveAnnotations;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link ShouldHaveAnnotations#shouldHaveAnnotations(Class, java.util.Collection, java.util.Collection)}}</code>
 *
 * @author William Delanoue
 */
class ShouldHaveAnnotations_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldHaveAnnotations(ShouldHaveAnnotations_create_Test.class,
                                                        Lists.list(Override.class,
                                                                   Deprecated.class),
                                                        Lists.list(SuppressWarnings.class));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(String.format(
                                          "[Test] %n"
                                          + "Expecting%n"
                                          + "  org.assertj.core.error.ShouldHaveAnnotations_create_Test%n"
                                          + "to have annotations:%n"
                                          + "  [java.lang.Override, java.lang.Deprecated]%n"
                                          + "but the following annotations were not found:%n"
                                          + "  [java.lang.SuppressWarnings]"));
  }
}
