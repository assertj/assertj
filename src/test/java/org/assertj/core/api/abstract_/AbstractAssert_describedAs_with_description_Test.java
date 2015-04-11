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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api.abstract_;

import static org.assertj.core.test.ErrorMessages.descriptionIsNull;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.test.TestData.someDescription;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.ConcreteAssert;
import org.assertj.core.description.Description;
import org.assertj.core.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link AbstractAssert#describedAs(Description)}</code>
 * 
 * @author Alex Ruiz
 */
public class AbstractAssert_describedAs_with_description_Test {

  @Rule
  public ExpectedException thrown = none();

  private ConcreteAssert assertions;
  private Description d;

  @Before
  public void setUp() {
    assertions = new ConcreteAssert(6L);
    d = someDescription();
  }

  @Test
  public void should_set_description() {
    assertions.describedAs(d);
    assertEquals(d.value(), assertions.descriptionText());
  }

  @Test
  public void should_return_this() {
    ConcreteAssert descriptable = assertions.describedAs(d);
    assertSame(assertions, descriptable);
  }

  @Test
  public void should_throw_error_if_description_is_null() {
    thrown.expectNullPointerException(descriptionIsNull());
    assertions.describedAs((Description) null);
  }
}
