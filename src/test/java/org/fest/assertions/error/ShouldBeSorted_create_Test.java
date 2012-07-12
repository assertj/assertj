/*
 * Created on Sep 17, 2010
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
package org.fest.assertions.error;

import static junit.framework.Assert.assertEquals;

import static org.fest.assertions.error.ShouldBeSorted.shouldBeSorted;
import static org.fest.util.Arrays.array;

import static org.junit.rules.ExpectedException.none;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.fest.assertions.description.Description;
import org.fest.assertions.internal.TestDescription;

/**
 * Tests for <code>{@link ShouldBeSorted#create(Description)}</code>.
 * 
 * @author Joel Costigliola
 */
public class ShouldBeSorted_create_Test {

  @Rule
  public ExpectedException thrown = none();

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldBeSorted(1, array("b", "c", "a"));
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TestDescription("Test"));
    assertEquals("[Test] group is not sorted because element 1:<'c'> is not less or equal than element 2:<'a'>.\n"
        + "group was:\n" + "<['b', 'c', 'a']>", message);
  }

  @Test
  public void should_fail_if_object_parameter_is_not_an_array() {
    thrown.expect(IllegalArgumentException.class);
    shouldBeSorted(1, "not an array");
  }
}
