/*
 * Created on Jan 28, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.assertj.core.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.Reader;
import java.io.StringReader;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for <code>{@link org.assertj.core.api.Assertions#assertThat(java.io.Reader)}</code>.
 *
 * @author Matthieu Baechler
 * @author Bartosz Bierkowski
 */
public class Assertions_assertThat_with_Reader_Test {

  private static Reader actual;

  @BeforeClass
  public static void setUpOnce() {
    actual = new StringReader("");
  }

  @Test
  public void should_create_Assert() {
    AbstractReaderAssert<?, ? extends Reader> assertions = Assertions.assertThat(actual);
    assertNotNull(assertions);
  }

  @Test
  public void should_pass_actual() {
    AbstractReaderAssert<?, ? extends Reader> assertions = Assertions.assertThat(actual);
    assertSame(actual, assertions.actual);
  }
}
