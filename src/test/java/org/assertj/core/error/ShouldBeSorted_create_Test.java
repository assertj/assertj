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
import static org.assertj.core.error.ShouldBeSorted.shouldBeSorted;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.assertj.core.test.ExpectedException;

/**
 * Tests for <code>{@link ShouldBeSorted#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Joel Costigliola
 */
public class ShouldBeSorted_create_Test {

  @Rule
  public ExpectedException thrown = none();
  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldBeSorted(1, array("b", "c", "a"));
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TestDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(String.format(
        "[Test] %ngroup is not sorted because element 1:%n <\"c\">%nis not less or equal than element 2:%n <\"a\">%ngroup was:%n <[\"b\", \"c\", \"a\"]>"
    ));
  }

  @Test
  public void should_fail_if_object_parameter_is_not_an_array() {
    thrown.expectIllegalArgumentException();
    shouldBeSorted(1, "not an array");
  }
}
