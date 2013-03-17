/*
 * Created on Apr 27, 2012
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
 * Copyright @2010-2012 the original author or authors.
 */
package org.assertj.core.error;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.util.Lists.newArrayList;

import org.assertj.core.description.*;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.error.ShouldHaveSameSizeAs;
import org.junit.*;

/**
 * Tests for <code>{@link ShouldHaveSameSizeAs#create(Description)}</code>.
 * 
 * @author Nicolas François
 */
public class ShouldHaveSameSizeAs_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldHaveSameSizeAs(newArrayList("Luke", "Yoda"), 2, 8);
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"));
    assertEquals(
        "[Test] actual and expected should have same size but actual size is:<2> while expected is:<8>, actual was:<['Luke', 'Yoda']>",
        message);
  }
}
