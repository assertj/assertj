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
package org.assertj.core.error.uri;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.uri.ShouldHaveFragment.shouldHaveFragment;

import java.net.URI;
import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

class ShouldHaveFragment_create_Test {

  @Test
  void should_create_error_message_for_has_fragment() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news#print");
    // WHEN
    String error = shouldHaveFragment(uri, "foo").create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting fragment of%n" +
                                 "  <http://assertj.org/news#print>%n" +
                                 "to be:%n" +
                                 "  <\"foo\">%n" +
                                 "but was:%n" +
                                 "  <\"print\">"));
  }

  @Test
  void should_create_error_message_for_has_no_fragment() {
    // GIVEN
    URI uri = URI.create("http://assertj.org/news#print");
    // WHEN
    String error = shouldHaveFragment(uri, null).create(new TestDescription("TEST"));
    // THEN
    then(error).isEqualTo(format("[TEST] %n" +
                                 "Expecting URI:%n" +
                                 "  <http://assertj.org/news#print>%n" +
                                 "not to have a fragment but had:%n" +
                                 "  <\"print\">"));
  }
}
