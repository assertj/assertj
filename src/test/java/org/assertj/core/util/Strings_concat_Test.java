/*
 * Created on Sep 22, 2006
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
 * Copyright @2006-2011 the original author or authors.
 */
package org.assertj.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for <code>{@link Strings#concat(Object...)}</code>.
 * 
 * @author Alex Ruiz
 */
public class Strings_concat_Test {

  @Test
  public void should_return_null_if_array_is_null() {
    assertNull(Strings.concat((Object[]) null));
  }

  @Test
  public void should_concatenate_given_Strings() {
    assertEquals("OneTwoThree", Strings.concat("One", "Two", "Three"));
  }
}
