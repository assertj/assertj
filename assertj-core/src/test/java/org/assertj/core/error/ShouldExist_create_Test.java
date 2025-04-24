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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldExist.shouldExistNoFollowLinks;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.mockito.Mockito.mock;

import java.nio.file.Path;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Yvonne Wang
 */
class ShouldExist_create_Test {

  private TestDescription description;

  @BeforeEach
  public void setUp() {
    description = new TestDescription("Test");
  }

  @Test
  void should_create_error_message_for_File() {
    // WHEN
    String actualMessage = shouldExist(new FakeFile("xyz")).create(description, STANDARD_REPRESENTATION);
    // THEN
    then(actualMessage).isEqualTo("[Test] %nExpecting file:%n  xyz%nto exist.".formatted());
  }

  @Test
  void should_create_error_message_for_Path_following_symbolic_links() {
    // GIVEN
    final Path actual = mock(Path.class);
    // WHEN
    String actualMessage = shouldExist(actual).create(description, STANDARD_REPRESENTATION);
    // THEN
    then(actualMessage).isEqualTo("[Test] %nExpecting path:%n  %s%nto exist (symbolic links were followed).".formatted(actual));
  }

  @Test
  void should_create_error_message_for_Path_not_following_symbolic_links() {
    // GIVEN
    final Path actual = mock(Path.class);
    // WHEN
    String actualMessage = shouldExistNoFollowLinks(actual).create(description, STANDARD_REPRESENTATION);
    // THEN
    then(actualMessage).isEqualTo("[Test] %nExpecting path:%n  %s%nto exist (symbolic links were not followed).".formatted(actual));
  }
}
