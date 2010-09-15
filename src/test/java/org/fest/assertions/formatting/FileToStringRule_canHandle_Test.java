/*
 * Created on Sep 10, 2010
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
package org.fest.assertions.formatting;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.io.File;

import org.junit.*;

/**
 * Tests for <code>{@link FileToStringRule#canHandle(Object)}</code>.
 *
 * @author Alex Ruiz
 */
public class FileToStringRule_canHandle_Test {

  private static FileToStringRule rule;

  @BeforeClass public static void setUpOnce() {
    rule = new FileToStringRule();
  }

  @Test public void should_return_true_if_object_is_File() {
    assertTrue(rule.canHandle(mock(File.class)));
  }

  @Test public void should_return_false_if_object_is_not_File() {
    assertFalse(rule.canHandle("Luke"));
  }
}
