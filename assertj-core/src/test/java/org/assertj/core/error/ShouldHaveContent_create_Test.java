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
import static java.nio.charset.Charset.defaultCharset;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.error.ShouldHaveContent.shouldHaveContent;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.assertj.core.description.TextDescription;
import org.assertj.core.util.Lists;
import org.assertj.core.util.diff.Delta;
import org.junit.jupiter.api.Test;

class ShouldHaveContent_create_Test {

  private static final String DIFF = "diff !";

  @Test
  void should_create_error_message() {
    // GIVEN
    final FakeFile file = new FakeFile("xyz");
    Delta<String> delta = mock(Delta.class);
    when(delta.toString()).thenReturn(DIFF);
    List<Delta<String>> diffs = Lists.list(delta);
    ErrorMessageFactory factory = shouldHaveContent(file, defaultCharset(), diffs);
    // WHEN
    String message = factory.create(new TextDescription("Test"), CONFIGURATION_PROVIDER.representation());
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "File:%n"
                                   + "  xyz%n"
                                   + "read with charset %s does not have the expected content:%n%n"
                                   + DIFF,
                                   defaultCharset().name()));
  }
}
