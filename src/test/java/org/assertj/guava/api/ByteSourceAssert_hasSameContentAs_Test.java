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
 * Copyright 2012-2013 the original author or authors.
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
 * Tests for <code>{@link ByteSource#contentEquals(ByteSource)}</code>.
 *
 * @author Andrew Gaul
 */
public class ByteSourceAssert_hasSameContentAs_Test extends BaseTest {

  @Test
  public void should_pass_if_size_of_actual_is_equal_to_expected_size() throws IOException {
    ByteSource actual = ByteSource.wrap(new byte[1]);
    ByteSource other = ByteSource.wrap(new byte[1]);
    assertThat(actual).hasSameContentAs(other);
  }

  @Test
  public void should_fail_if_actual_is_null() throws IOException {
    ByteSource actual = null;
    ByteSource other = ByteSource.wrap(new byte[1]);
    expectException(AssertionError.class, actualIsNull());
    assertThat(actual).hasSameContentAs(other);
  }

  @Test
  public void should_fail_if_content_of_actual_is_not_equal_to_expected_content() throws IOException {
    ByteSource actual = ByteSource.wrap(new byte[1]);
    ByteSource other = ByteSource.wrap(new byte[] {(byte) 1});
    try {
      assertThat(actual).hasSameContentAs(other);
    } catch (AssertionError e) {
      assertThat(e).hasMessage("expected:<ByteSource.wrap(0[1])> but was:<ByteSource.wrap(0[0])>");
      return;
    }
    fail("Assertion error expected");
  }
}
