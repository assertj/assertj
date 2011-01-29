/*
 * Created on Jan 28, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.File;

import org.fest.assertions.internal.Files;
import org.junit.*;

/**
 * Tests for <code>{@link FileAssert#hasContentEqualTo(File)}</code>.
 *
 * @author Yvonne Wang
 */
public class FileAssert_hasContentEqualTo_Test {

  private static File expected;

  @BeforeClass public static void setUpOnce() {
    expected = new File("xyz");
  }

  private Files files;
  private FileAssert assertions;

  @Before public void setUp() {
    files = mock(Files.class);
    assertions = new FileAssert(new File("abc"));
    assertions.files = files;
  }

  @Test public void should_verify_that_content_in_actual_is_equal_to_expected() {
    assertions.hasContentEqualTo(expected);
    verify(files).assertEqualContent(assertions.info, assertions.actual, expected);
  }

  @Test public void should_return_this() {
    FileAssert returned = assertions.hasContentEqualTo(expected);
    assertSame(assertions, returned);
  }
}
