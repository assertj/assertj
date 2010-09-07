/*
 * Created on Jul 26, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.assertions.test.ExpectedExceptions.expectErrorWhenDescriptionIsNull;
import static org.junit.Assert.*;
import static org.junit.rules.ExpectedException.none;

import org.fest.assertions.description.*;
import org.junit.*;
import org.junit.rules.ExpectedException;

/**
 * Tests for <code>{@link DescriptionValidator#notNull(org.fest.assertions.description.Description)}</code> and
 * <code>{@link DescriptionValidator#notNull(String)}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class DescriptionValidator_notNull_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_throw_error_if_description_is_null() {
    expectErrorWhenDescriptionIsNull(thrown);
    Description d = null;
    DescriptionValidator.notNull(d);
  }

  @Test
  public void should_throw_error_if_text_description_is_null() {
    expectErrorWhenDescriptionIsNull(thrown);
    String d = null;
    DescriptionValidator.notNull(d);
  }

  @Test
  public void should_return_description_with_given_text() {
    Description d = DescriptionValidator.notNull("Yoda");
    assertEquals("Yoda", d.value());
  }

  @Test
  public void should_return_same_description() {
    Description e = new TextDescription("Yoda");
    Description d = DescriptionValidator.notNull(e);
    assertSame(e, d);
  }
}
