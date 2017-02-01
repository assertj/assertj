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
import static org.assertj.core.error.ShouldBeSubsetOf.shouldBeSubsetOf;
import static org.assertj.core.util.Lists.newArrayList;

import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link ShouldBeSubsetOf#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 * 
 * @author Maciej Jaskowski
 */
public class ShouldBeSubsetOf_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
	factory = shouldBeSubsetOf(newArrayList("Yoda", "Luke"), newArrayList("Han", "Luke"), newArrayList("Yoda"));
  }

  @Test
  public void should_create_error_message() {
	String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
	assertThat(message).isEqualTo(String.format("[Test] %nExpecting :%n <[\"Yoda\", \"Luke\"]>%nto be subset of%n <[\"Han\", \"Luke\"]>%nbut found these extra elements:%n <[\"Yoda\"]>"));
  }

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
	ErrorMessageFactory factory = shouldBeSubsetOf(newArrayList("Yoda", "Luke"),
	                                               newArrayList("Han", "Luke"),
	                                               newArrayList("Yoda"),
	                                               new ComparatorBasedComparisonStrategy(
	                                                                                     CaseInsensitiveStringComparator.instance));
	String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
	assertThat(message).isEqualTo(String.format("[Test] %nExpecting when comparing values using 'CaseInsensitiveStringComparator':%n <[\"Yoda\", \"Luke\"]>%nto be subset of%n <[\"Han\", \"Luke\"]>%nbut found these extra elements:%n <[\"Yoda\"]>"));
  }
}
