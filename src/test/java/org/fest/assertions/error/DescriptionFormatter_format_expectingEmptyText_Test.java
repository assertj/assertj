/*
 * Created on Jan 11, 2011
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
package org.fest.assertions.error;

import static junit.framework.Assert.assertEquals;
import static org.fest.util.Lists.newArrayList;

import java.util.List;

import org.fest.assertions.description.Description;
import org.fest.assertions.internal.TestDescription;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.*;
import org.junit.runners.Parameterized.Parameters;

/**
 * Tests for <code>{@link DescriptionFormatter#format(Description)}</code>.
 * 
 * @author Alex Ruiz
 */
@RunWith(Parameterized.class)
public class DescriptionFormatter_format_expectingEmptyText_Test {

  private final Description description;

  @Parameters
  public static List<Object[]> parameters() {
    return newArrayList(new Object[][] { { null }, { new TestDescription(null) }, { new TestDescription("") } });
  }

  public DescriptionFormatter_format_expectingEmptyText_Test(Description description) {
    this.description = description;
  }

  private static DescriptionFormatter formatter;

  @BeforeClass
  public static void setUpOnce() {
    formatter = DescriptionFormatter.instance();
  }

  @Test
  public void should_return_empty_String() {
    assertEquals("", formatter.format(description));
  }
}
