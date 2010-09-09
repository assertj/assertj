/*
 * Created on Sep 8, 2010
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
import static org.fest.assertions.util.ToString.toStringOf;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.fest.assertions.description.Description;
import org.fest.assertions.internal.TestDescription;
import org.fest.assertions.util.ToString;
import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Tests for <code>{@link Formatter#formatMessage(String, Description, Object...)}</code>.
 *
 * @author Alex Ruiz
 */
@PrepareForTest(ToString.class)
@RunWith(PowerMockRunner.class)
public class Formatter_formatMessage_Test {

  @Before
  public void setUp() {
    mockStatic(ToString.class);
  }

  @Test
  public void should_format_message() {
    when(toStringOf("World")).thenReturn("World!");
    String s = Formatter.formatMessage("%sHello %s", new TestDescription("Testing"), "World");
    assertEquals("[Testing] Hello World!", s);
  }
}
