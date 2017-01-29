/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeSubstring.shouldBeSubstring;

import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Test;

public class ShouldBeSubstringOf_create_Test {

  @Test
  public void should_create_error_message() {
    ErrorMessageFactory factory = shouldBeSubstring("bcd", "abcdef", StandardComparisonStrategy.instance());
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  <\"bcd\">%n" +
                                         "to be a substring of:%n" +
                                         "  <\"abcdef\">%n"));
  }

  @Test
  public void should_create_error_message_with_comparison_strategy() {
    ErrorMessageFactory factory = shouldBeSubstring("bcd", "abcdef",
                                                    new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         "  <\"bcd\">%n" +
                                         "to be a substring of:%n" +
                                         "  <\"abcdef\">%n" +
                                         "when comparing values using 'CaseInsensitiveStringComparator'"));
  }

}
