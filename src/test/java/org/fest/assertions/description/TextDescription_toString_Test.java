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
package org.fest.assertions.description;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for <code>{@link TextDescription#toString()}</code>.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class TextDescription_toString_Test {

  @Test
  public void should_return_value() {
    TextDescription description = new TextDescription("Flash");
    assertEquals(description.value, description.toString());
  }
}
