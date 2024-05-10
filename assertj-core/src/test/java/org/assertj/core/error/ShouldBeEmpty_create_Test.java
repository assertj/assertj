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
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Lists.list;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.assertj.core.description.Description;
import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.Representation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldBeEmpty#create(Description, Representation)}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
@DisplayName("ShouldBeEmpty create")
class ShouldBeEmpty_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory underTest = shouldBeEmpty(list("Luke", "Yoda"));
    // WHEN
    String message = underTest.create(new TestDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting empty but was: [\"Luke\", \"Yoda\"]"));
  }

  @Test
  void should_create_specific_error_message_for_File() {
    // GIVEN
    File file = new File("/test.txt");
    ErrorMessageFactory underTest = shouldBeEmpty(file);
    // WHEN
    String message = underTest.create(new TestDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo("[Test] %nExpecting file %s to be empty", file.getAbsolutePath());
  }

  @Test
  void should_create_specific_error_message_for_Path() {
    // GIVEN
    Path path = Paths.get("/test.txt");
    ErrorMessageFactory underTest = shouldBeEmpty(path);
    // WHEN
    String message = underTest.create(new TestDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo("[Test] %nExpecting path %s to be empty", path);
  }

}
