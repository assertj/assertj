/*
 * Created on Jan 15, 2011
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
package org.fest.assertions.core;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

import org.fest.assertions.description.Description;
import org.junit.*;

/**
 * Tests for <code>{@link WritableAssertionInfo#descriptionText()}</code>.
 * 
 * @author Yvonne Wang
 */
public class WritableAssertionInfo_descriptionText_Test {

  private WritableAssertionInfo info;

  @Before
  public void setUp() {
    info = new WritableAssertionInfo();
  }

  @Test
  public void should_return_null_if_description_is_null() {
    assertNull(info.descriptionText());
  }

  @Test
  public void should_return_text_of_description() {
    Description description = mock(Description.class);
    info.description(description);
    when(description.value()).thenReturn("Yoda");
    assertEquals("Yoda", info.descriptionText());
  }
}
