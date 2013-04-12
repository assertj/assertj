/*
 * Created on Dec 27, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.error;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.error.ShouldBeInstanceOfAny.shouldBeInstanceOfAny;

import java.io.File;
import java.util.regex.Pattern;

import org.assertj.core.description.Description;
import org.assertj.core.internal.TestDescription;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link ShouldBeInstanceOfAny#create(Description)}</code>.
 * 
 * @author Alex Ruiz
 */
public class ShouldBeInstanceOfAny_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    Class<?>[] types = { File.class, Pattern.class };
    factory = shouldBeInstanceOfAny("Yoda", types);
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TestDescription("Test"));
    assertEquals(
        "[Test] \nExpecting:\n <'Yoda'>\nto be an instance of any of:\n <[java.io.File, java.util.regex.Pattern]>\nbut was instance of:\n <java.lang.String>",
        message);
  }
}
