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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.error;

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.error.ShouldContainOnly.shouldContainOnly;
import static org.fest.util.Collections.*;

import org.fest.assertions.description.*;
import org.junit.*;

/**
 * Tests for <code>{@link ShouldContainOnly#create(Description)}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class ShouldContainOnly_create_Test {

  private ErrorMessageFactory factory;

  @Before public void setUp() {
    factory = shouldContainOnly(list("Yoda", "Han"), list("Luke", "Yoda"), set("Luke"), set("Han"));
  }

  @Test public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] expected:<['Yoda', 'Han']> to contain only:<['Luke', 'Yoda']>; not found:<['Luke']> and not expected:<['Han']>", message);
  }
}
