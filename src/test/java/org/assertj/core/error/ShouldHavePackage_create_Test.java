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

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHavePackage.shouldHavePackage;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import java.util.Collection;

import org.assertj.core.description.Description;
import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.Representation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldHavePackage#create(Description, Representation)}</code>.
 *
 * @author Stefano Cordio
 */
@DisplayName("ShouldHavePackage create")
class ShouldHavePackage_create_Test {

  @Test
  void should_create_error_message_with_String_if_actual_has_package() {
    // WHEN
    String message = shouldHavePackage(Object.class, "java.util").create(new TestDescription("TEST"),
                                                                         STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[TEST] %n" +
                                   "Expecting%n" +
                                   "  java.lang.Object%n" +
                                   "to have package:%n" +
                                   "  \"java.util\"%n" +
                                   "but had:%n" +
                                   "  \"java.lang\""));
  }

  @Test
  void should_create_error_message_with_String_if_actual_has_no_package() {
    // WHEN
    String message = shouldHavePackage(Object[].class, "java.util").create(new TestDescription("TEST"),
                                                                           STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[TEST] %n" +
                                   "Expecting%n" +
                                   "  java.lang.Object[]%n" +
                                   "to have package:%n" +
                                   "  \"java.util\"%n" +
                                   "but had none."));
  }

  @Test
  void should_create_error_message_with_Package_if_actual_has_package() {
    // WHEN
    String message = shouldHavePackage(Object.class, Collection.class.getPackage()).create(new TestDescription("TEST"),
                                                                                           STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[TEST] %n" +
                                   "Expecting%n" +
                                   "  java.lang.Object%n" +
                                   "to have package:%n" +
                                   "  \"java.util\"%n" +
                                   "but had:%n" +
                                   "  \"java.lang\""));
  }

  @Test
  void should_create_error_message_with_Package_if_actual_has_no_package() {
    // WHEN
    String message = shouldHavePackage(Object[].class, Collection.class.getPackage()).create(new TestDescription("TEST"),
                                                                                             STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[TEST] %n" +
                                   "Expecting%n" +
                                   "  java.lang.Object[]%n" +
                                   "to have package:%n" +
                                   "  \"java.util\"%n" +
                                   "but had none."));
  }

}
