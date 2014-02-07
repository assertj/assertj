/*
 * Created on Oct 20, 2010
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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Tests for <code>{@link Assertions#assertThat(Float)}</code>.
 * 
 * @author Alex Ruiz
 */
public class Assertions_assertThat_with_Float_Test {

  @Test
  public void should_create_Assert() {
    Float zero = 0f;
    FloatAssert assertions = Assertions.assertThat(zero);
    assertNotNull(assertions);
  }

  @Test
  public void should_pass_actual() {
    Float eight = 8f;
    FloatAssert assertions = Assertions.assertThat(eight);
    assertSame(eight, assertions.actual);
  }

  @Test
  public void isCloseTo_within_offset_should_pass() {
    assertThat(8.1f).isCloseTo(8.2f, within(0.2f));
    assertThat(8f).isCloseTo(new Float(8.2f), within(0.5f));
    // you can use offset if you prefer
    assertThat(8.1f).isCloseTo(8.2f, offset(0.2f));
    // if difference is exactly equals to 0.1, it's ok
    assertThat(8.1f).isCloseTo(8.2f, within(0.1f));
  }

  @Test
  public void isEqualTo_with_offset_should_pass() {
    assertThat(8.1f).isEqualTo(8.2f, offset(0.2f));
    assertThat(8.1f).isEqualTo(new Float(8.2), offset(0.2f));
    // within is an alias of offset
    assertThat(8.1f).isEqualTo(8.2f, within(0.1f));
    // if difference is exactly equals to the offset (0.1), it's ok
    assertThat(8.1f).isEqualTo(new Float(8.2), offset(0.1f));
  }
}
