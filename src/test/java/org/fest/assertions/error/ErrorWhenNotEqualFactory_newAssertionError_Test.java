/*
 * Created on Aug 6, 2010
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
package org.fest.assertions.error;

import static junit.framework.Assert.assertEquals;
import static org.fest.util.Collections.list;

import java.util.List;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.*;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests for <code>{@link ErrorWhenNotEqualFactory#newAssertionError(String)}</code>.
 *
 * @author Alex Ruiz
 */
@RunWith(Parameterized.class)
public class ErrorWhenNotEqualFactory_newAssertionError_Test {

  private final String description;

  @Parameters
  public static List<Object[]> parameters() {
    return list(new Object[][] {
        { "[Jedi]" },
        { "[Jedi] " }
    });
  }

  public ErrorWhenNotEqualFactory_newAssertionError_Test(String description) {
    this.description = description;
  }

  private ErrorWhenNotEqualFactory errorFactory;

  @Before
  public void setUp() {
    errorFactory = new ErrorWhenNotEqualFactory("Yoda", "Luke");
  }

  @Test
  public void should_create_ComparisonFailure_if_JUnit4_is_in_classpath() {
    AssertionError error = errorFactory.newAssertionError(description);
    assertEquals(ComparisonFailure.class, error.getClass());
    assertEquals("[Jedi] expected:<'[Yoda]'> but was:<'[Luke]'>", error.getMessage());
  }
}
