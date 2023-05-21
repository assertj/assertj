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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldStartWithPath.PATH_SHOULD_START_WITH;
import static org.assertj.core.error.ShouldStartWithPath.shouldStartWith;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.mockito.Mockito.mock;

import java.nio.file.Path;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

final class ShouldStartWithPath_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    final Path actual = mock(Path.class);
    final Path other = mock(Path.class);
    // WHEN
    String actualMessage = shouldStartWith(actual, other).create(new TestDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(actualMessage).isEqualTo(format("[Test] " + PATH_SHOULD_START_WITH, actual, other));
  }
}
