/*
 * Created on Jan 29, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.File;

import org.fest.assertions.internal.Files;
import org.junit.*;

/**
 * Tests for <code>{@link FileAssert#exists()}</code>.
 * 
 * @author Yvonne Wang
 */
public class FileAssert_exists_Test {

  private Files files;
  private FileAssert assertions;

  @Before
  public void setUp() {
    files = mock(Files.class);
    assertions = new FileAssert(new File("abc"));
    assertions.files = files;
  }

  @Test
  public void should_verify_that_actual_exists() {
    assertions.exists();
    verify(files).assertExists(assertions.info, assertions.actual);
  }

  @Test
  public void should_return_this() {
    FileAssert returned = assertions.exists();
    assertSame(assertions, returned);
  }
}
