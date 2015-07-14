/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.guava.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Test;

import com.google.common.io.ByteSource;

/**
 * Tests for <code>{@link ByteSource#isEmpty()}</code>.
 *
 * @author Andrew Gaul
 */
public class ByteSourceAssert_isEmpty_Test extends BaseTest {

  @Test
  public void should_pass_if_actual_is_empty() throws IOException {
    ByteSource actual = ByteSource.empty();
    assertThat(actual).isEmpty();
  }

  @Test
  public void should_fail_if_actual_is_null() throws IOException {
    ByteSource actual = null;
    expectException(AssertionError.class, actualIsNull());
    assertThat(actual).isEmpty();
  }

  @Test
  public void should_fail_if_actual_is_not_empty() throws IOException {
    ByteSource actual = ByteSource.wrap(new byte[1]);
    try {
      assertThat(actual).isEmpty();
    } catch (AssertionError e) {
      assertThat(e).hasMessage("\nExpecting empty but was:<ByteSource.wrap(00)>");
      return;
    }
    fail("Assertion error expected");
  }

}
