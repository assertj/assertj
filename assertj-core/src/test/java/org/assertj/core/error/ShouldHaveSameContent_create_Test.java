/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSameContent.shouldHaveSameContent;

import java.io.ByteArrayInputStream;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link ShouldHaveSameContent#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 *
 * @author Yvonne Wang
 * @author Matthieu Baechler
 */
class ShouldHaveSameContent_create_Test {

  @Test
  void should_create_error_message_file_even_if_content_contains_format_specifier() {
    // GIVEN
    ErrorMessageFactory factory = shouldHaveSameContent(new FakeFile("abc"), new FakeFile("xyz"), emptyList());
    // WHEN
    String expectedErrorMessage = format("[Test] %nFile:%n  abc%nand file:%n  xyz%ndo not have same content:%n%n");
    // THEN
    then(factory.create(new TextDescription("Test"), new StandardRepresentation())).isEqualTo(expectedErrorMessage);
  }

  @Test
  void should_create_error_message_inputstream_even_if_content_contains_format_specifier() {
    // GIVEN
    ErrorMessageFactory factory = shouldHaveSameContent(new ByteArrayInputStream(new byte[] { 'a' }),
                                                        new ByteArrayInputStream(new byte[] { 'b' }),
                                                        emptyList());
    // WHEN
    String expectedErrorMessage = format("[Test] %nInputStreams do not have same content:%n%n");
    // THEN
    then(factory.create(new TextDescription("Test"), new StandardRepresentation())).isEqualTo(expectedErrorMessage);
  }

}
