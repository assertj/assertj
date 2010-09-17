/*
 * Created on Aug 6, 2010
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
package org.fest.assertions.error;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.fest.assertions.description.Description;
import org.fest.assertions.internal.TestDescription;
import org.junit.*;

/**
 * Tests for <code>{@link WhenNotEqualErrorFactory#newAssertionError(Description)}</code>.
 *
 * @author Alex Ruiz
 */
public class WhenNotEqualErrorFactory_newAssertionError_Test {

  private Description description;
  private Formatter formatter;
  private WhenNotEqualErrorFactory factory;

  @Before public void setUp() {
    description = new TestDescription("Jedi");
    formatter = mock(Formatter.class);
    factory = new WhenNotEqualErrorFactory("Luke", "Yoda");
    factory.updateFormatter(formatter);
  }

  @Test public void should_create_ComparisonFailure_if_JUnit4_is_in_classpath() {
    when(formatter.format(description)).thenReturn("[Jedi]");
    AssertionError error = factory.newAssertionError(description);
    verify(error);
  }

  @Test public void should_trim_formatted_description() {
    when(formatter.format(description)).thenReturn("[Jedi] ");
    AssertionError error = factory.newAssertionError(description);
    verify(error);
  }

  private void verify(AssertionError error) {
    assertEquals(ComparisonFailure.class, error.getClass());
    assertEquals("[Jedi] expected:<'[Yoda]'> but was:<'[Luke]'>", error.getMessage());
  }
}
