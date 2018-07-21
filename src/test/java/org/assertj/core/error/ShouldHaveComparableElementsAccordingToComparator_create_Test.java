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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeSorted.shouldHaveComparableElementsAccordingToGivenComparator;
import static org.assertj.core.util.Arrays.array;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link ShouldBeSorted#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 * 
 * @author Joel Costigliola
 */
public class ShouldHaveComparableElementsAccordingToComparator_create_Test {

  private ErrorMessageFactory factory;

  @BeforeEach
  public void setUp() {
	factory = shouldHaveComparableElementsAccordingToGivenComparator(array("b", "c", "a"),
	                                                                 new CaseInsensitiveStringComparator());
  }

  @Test
  public void should_create_error_message() {
	String message = factory.create(new TestDescription("Test"), new StandardRepresentation());
	assertThat(message).isEqualTo(String.format("[Test] %nsome elements are not mutually comparable according to CaseInsensitiveStringComparator comparator in group:%n"
	                                  + "<[\"b\", \"c\", \"a\"]>"));

  }
}
