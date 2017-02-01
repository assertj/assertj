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
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContainIgnoringCase;
import static org.assertj.core.util.Arrays.array;
import static org.mockito.internal.util.collections.Sets.newSet;

import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Test;

/**
 * Tests for <code>{@link ShouldContainCharSequence#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class ShouldContainString_create_Test {

  private ErrorMessageFactory factory;

  @Test
  public void should_create_error_message() {
    factory = shouldContain("Yoda", "Luke");
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format("[Test] %nExpecting:%n <\"Yoda\">%nto contain:%n <\"Luke\"> "));
  }

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
    factory = shouldContain("Yoda", "Luke",
                            new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
        "[Test] %nExpecting:%n <\"Yoda\">%nto contain:%n <\"Luke\"> when comparing values using 'CaseInsensitiveStringComparator'"
    ));
  }

  @Test
  public void should_create_error_message_when_ignoring_case() {
    factory = shouldContainIgnoringCase("Yoda", "Luke");
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
        "[Test] %nExpecting:%n <\"Yoda\">%nto contain:%n <\"Luke\">%n (ignoring case)"
    ));
  }

  @Test
  public void should_create_error_message_with_several_string_values() {
    factory = shouldContain("Yoda, Luke", array("Luke", "Vador", "Solo"), newSet("Vador", "Solo"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
        "[Test] %nExpecting:%n <\"Yoda, Luke\">%nto contain:%n <[\"Luke\", \"Vador\", \"Solo\"]>%nbut could not find:%n <[\"Vador\", \"Solo\"]>%n "
    ));
  }

}
