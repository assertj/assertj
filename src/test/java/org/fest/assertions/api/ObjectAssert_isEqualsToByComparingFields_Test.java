/*
 * 
 * Created on May 27, 2012
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
 * Copyright @2012 the original author or authors.
 */
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.fest.assertions.internal.Objects;
import org.fest.assertions.test.Jedi;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link ObjectAssert#isEqualsToByComparingFields(Object)}</code>.
 * 
 * @author Nicolas François
 */
public class ObjectAssert_isEqualsToByComparingFields_Test {

  private Objects objects;
  private ObjectAssert<Jedi> assertions;

  @Before
  public void setUp() {
    objects = mock(Objects.class);
    assertions = new ObjectAssert<Jedi>(new Jedi("Yoda", "Green"));
    assertions.objects = objects;
  }

  @Test
  public void should_verify_that_actual_is_instance_of_type() {
    Jedi other = new Jedi("Yoda", "Blue");
    assertions.isEqualsToByComparingFields(other);
    verify(objects).assertIsLenientEqualsToByIgnoringFields(assertions.info, assertions.actual, other);
  }

  @Test
  public void should_return_this() {
    Jedi other = new Jedi("Yoda", "Blue");
    ObjectAssert<Jedi> returned = assertions.isEqualsToByComparingFields(other);
    assertSame(assertions, returned);
  }
}
