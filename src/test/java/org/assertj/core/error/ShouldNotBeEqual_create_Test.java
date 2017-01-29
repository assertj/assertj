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
import static org.assertj.core.error.ShouldNotBeEqual.shouldNotBeEqual;

import org.assertj.core.internal.*;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for <code>{@link ShouldNotBeEqual#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ShouldNotBeEqual_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldNotBeEqual("Yoda", "Luke");
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TestDescription("Jedi"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format("[Jedi] %nExpecting:%n <\"Yoda\">%nnot to be equal to:%n <\"Luke\">%n"));
  }

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
    factory = shouldNotBeEqual("Yoda", "Luke", new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TestDescription("Jedi"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
        "[Jedi] %nExpecting:%n <\"Yoda\">%nnot to be equal to:%n <\"Luke\">%nwhen comparing values using 'CaseInsensitiveStringComparator'"
    ));
  }
}
