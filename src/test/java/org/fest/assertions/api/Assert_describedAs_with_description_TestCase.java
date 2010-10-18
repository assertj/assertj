/*
 * Created on Sep 17, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.api;

import static junit.framework.Assert.*;
import static org.fest.assertions.api.AssertInternals.descriptionOf;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.FailureMessages.descriptionIsNull;

import org.fest.assertions.core.*;
import org.fest.assertions.core.Assert;
import org.fest.assertions.description.*;
import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Test case for implementations of <code>{@link Assert#describedAs(Description)}</code>
 *
 * @author Alex Ruiz
 */
public abstract class Assert_describedAs_with_description_TestCase {

  @Rule public final ExpectedException thrown = none();

  private Assert<?> assertions;
  private Description d;

  @Before public final void setUp() {
    assertions = createAssertToTest();
    d = new TextDescription("who's the more foolish: the fool, or the fool who follows him?");
  }

  abstract Assert<?> createAssertToTest();

  @Test public final void should_set_description() {
    assertions.describedAs(d);
    assertEquals(d.value(), descriptionOf(assertions));
  }

  @Test public final void should_return_this() {
    Descriptable descriptable = assertions.describedAs(d);
    assertSame(assertions, descriptable);
  }

  @Test public final void should_throw_error_if_description_is_null() {
    thrown.expectNullPointerException(descriptionIsNull());
    assertions.describedAs((Description) null);
  }
}
