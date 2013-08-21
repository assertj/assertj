/*
 * Created on Jul 20, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.description;

import static junit.framework.Assert.assertEquals;

import org.assertj.core.description.TextDescription;
import org.junit.Test;

/**
 * Tests for <code>{@link TextDescription#value()}</code>.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class TextDescription_value_Test {

  @Test
  public void should_return_value() {
    TextDescription description = new TextDescription("Robin");
    assertEquals(description.value, description.value());
  }

  @Test
  public void should_return_formatted_value() {
    TextDescription description = new TextDescription("Robin {}", "Hood");
    assertEquals("Robin Hood", description.value());
  }
}
