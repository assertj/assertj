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
import static org.assertj.core.error.ShouldNotExist.FILE_SHOULD_NOT_EXIST;
import static org.assertj.core.error.ShouldNotExist.PATH_SHOULD_NOT_EXIST;
import static org.assertj.core.error.ShouldNotExist.shouldNotExist;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.mockito.Mockito.mock;

import java.nio.file.Path;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link ShouldNotExist#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 *
 * @author Yvonne Wang
 */
class ShouldNotExist_create_Test {

  private static final TestDescription TEST_DESCRIPTION = new TestDescription("Test");

  @Test
  void should_create_error_message_for_File_object() {
    // GIVEN
    final FakeFile file = new FakeFile("xyz");
    // WHEN
    String actualMessage = shouldNotExist(file).create(TEST_DESCRIPTION, STANDARD_REPRESENTATION);
    // THEN
    then(actualMessage).isEqualTo(format("[Test] " + FILE_SHOULD_NOT_EXIST, file));
  }

  @Test
  void should_create_error_message_for_Path_object() {
    // GIVEN
    final Path path = mock(Path.class);
    // WHEN
    String actualMessage = shouldNotExist(path).create(TEST_DESCRIPTION, STANDARD_REPRESENTATION);
    // THEN
    then(actualMessage).isEqualTo(format("[Test] " + PATH_SHOULD_NOT_EXIST, path));
  }
}
