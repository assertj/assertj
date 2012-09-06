/*
 * Created on Oct 20, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api.abstract_;

import static junit.framework.Assert.*;
import static org.fest.assertions.test.ErrorMessages.descriptionIsNull;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.TestData.someTextDescription;

import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.api.ConcreteAssert;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link AbstractAssert#as(String)}</code>.
 * 
 * @author Alex Ruiz
 */
public class AbstractAssert_as_with_text_description_Test {

  @Rule
  public ExpectedException thrown = none();

  private ConcreteAssert assertions;
  private String description;

  @Before
  public void setUp() {
    assertions = new ConcreteAssert(6L);
    description = someTextDescription();
  }

  @Test
  public void should_set_description() {
    assertions.as(description);
    assertEquals(description, assertions.descriptionText());
  }

  @Test
  public void should_return_this() {
    ConcreteAssert descriptable = assertions.as(description);
    assertSame(assertions, descriptable);
  }

  @Test
  public void should_throw_error_if_description_is_null() {
    thrown.expectNullPointerException(descriptionIsNull());
    assertions.as((String) null);
  }
}
