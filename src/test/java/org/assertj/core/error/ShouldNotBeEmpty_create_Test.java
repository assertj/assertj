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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import java.io.File;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldNotBeEmpty#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
@DisplayName("ShouldNotBeEmpty create")
class ShouldNotBeEmpty_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory errorMessageFactory = shouldNotBeEmpty();
    // WHEN
    String message = errorMessageFactory.create(new TestDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting actual not to be empty"));
  }

  @Test
  void should_create_specific_error_message_for_File() {
    // GIVEN
    File file = new File("/test.txt");
    ErrorMessageFactory errorMessageFactory = shouldNotBeEmpty(file);
    // WHEN
    String message = errorMessageFactory.create(new TestDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting file <%s> not to be empty", file.getAbsolutePath()));
  }
}
