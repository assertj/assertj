/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.fest.assertions.api;

import static org.fest.assertions.api.GUAVA.assertThat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.google.common.base.Optional;

/**
 * Tests for <code>{@link GUAVA#assertThat(Optional)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Assertions_assertThat_with_Optional_Test {

  @Test
  public void should_create_Assert() {
    Optional<String> actual = Optional.of("value");
    assertNotNull(assertThat(actual));
    assertEquals(OptionalAssert.class, assertThat(actual).getClass());
  }

  @Test
  public void should_pass_actual() {
    Optional<String> actual = Optional.of("value");
    assertSame(actual, assertThat(actual).actual);
  }
}
