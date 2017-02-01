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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldNotContainCharSequence.shouldNotContain;

import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Test;


/**
 * Tests for <code>{@link ShouldNotContainCharSequence#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class ShouldNotContainString_create_Test {

  @Test
  public void should_create_error_message() {
    ErrorMessageFactory factory = shouldNotContain("Yoda", "od");
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format("[Test] %nExpecting:%n <\"Yoda\">%nnot to contain:%n <\"od\"> "));
  }

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
    ErrorMessageFactory factory = shouldNotContain("Yoda", "od", new ComparatorBasedComparisonStrategy(
        CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
        "[Test] %nExpecting:%n <\"Yoda\">%nnot to contain:%n <\"od\"> when comparing values using 'CaseInsensitiveStringComparator'"
    ));
  }
}
