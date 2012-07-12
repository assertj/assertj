/*
 * Created on Oct 21, 2010
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
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.*;

import org.fest.assertions.internal.Objects;
import org.junit.*;

/**
 * Tests for <code>{@link ByteAssert#isSameAs(Byte)}</code>.
 * 
 * @author Alex Ruiz
 */
public class ByteAssert_isSameAs_Test {

  private Objects objects;
  private ByteAssert assertions;

  @Before
  public void setUp() {
    objects = mock(Objects.class);
    assertions = new ByteAssert((byte) 6);
    assertions.objects = objects;
  }

  @Test
  public void should_verify_that_actual_value_is_same_as_expected_value() {
    Byte expected = (byte) 8;
    assertions.isSameAs(expected);
    verify(objects).assertSame(assertions.info, assertions.actual, expected);
  }

  @Test
  public void should_return_this() {
    ByteAssert returned = assertions.isSameAs((byte) 8);
    assertSame(assertions, returned);
  }
}