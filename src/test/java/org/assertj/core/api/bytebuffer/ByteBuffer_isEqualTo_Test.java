/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.bytebuffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.error.buffer.bytebuffer.ContentsShouldBeEqualTo.contentsShouldBeEqualTo;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;

import com.google.common.base.Charsets;

/**
 * Tests for :
 * <code>{@link org.assertj.core.api.AbstractByteBufferAssert#isEqualTo(byte[])}</code>.
 * <code>{@link org.assertj.core.api.AbstractByteBufferAssert#isEqualTo(String)}</code>.
 * <code>{@link org.assertj.core.api.AbstractByteBufferAssert#isEqualTo(ByteBuffer)}</code>.
 * <code>{@link org.assertj.core.api.AbstractByteBufferAssert#isEqualTo(String, Charset)}</code>.
 *
 * @author Jean de Leeuw
 */
public class ByteBuffer_isEqualTo_Test {

  @Test
  public void should_pass_when_buffer_equals_expected_string_with_default_charset() {
    String testString = "test";
    ByteBuffer buffer = ByteBuffer.wrap(testString.getBytes());
    assertThat(buffer).isEqualTo(testString);
  }

  @Test
  public void should_fail_when_buffer_does_not_equals_expected_string_with_default_charset() {
    String actual = "test";
    String expected = "differentString";

    ByteBuffer buffer = ByteBuffer.wrap(actual.getBytes());
    assertThatThrownBy(() -> assertThat(buffer).isEqualTo(expected))
      .isInstanceOf(AssertionError.class)
      .hasMessage(contentsShouldBeEqualTo(expected, buffer, Charset.defaultCharset()).create());
  }

  @Test
  public void should_pass_when_buffer_equals_expected_string_with_specified_charset() {
    String testString = "test";
    Charset specified = Charsets.UTF_8;

    ByteBuffer buffer = specified.encode(testString);
    assertThat(buffer).isEqualTo(testString, specified);
  }

  @Test
  public void should_fail_when_buffer_does_not_equals_expected_string_with_specified_charset() {
    String actual = "test";
    String expected = "differentString";
    Charset specified = Charsets.UTF_8;

    ByteBuffer buffer = specified.encode(actual);
    assertThatThrownBy(() -> assertThat(buffer).isEqualTo(expected, specified))
      .isInstanceOf(AssertionError.class)
      .hasMessage(contentsShouldBeEqualTo(expected, buffer, specified).create());
  }

  @Test
  public void should_fail_when_buffer_does_not_equals_expected_string_with_different_charsets() {
    String content = "test";
    Charset actualCharset = Charsets.UTF_8;
    Charset expectedCharset = Charsets.UTF_16;

    ByteBuffer buffer = actualCharset.encode(content);
    assertThatThrownBy(() -> assertThat(buffer).isEqualTo(content, expectedCharset))
      .isInstanceOf(AssertionError.class)
      .hasMessage(contentsShouldBeEqualTo(content, buffer, expectedCharset).create());
  }

  @Test
  public void should_pass_when_buffer_equals_expected_byte_array() {
    String testString = "test";

    ByteBuffer buffer = ByteBuffer.wrap(testString.getBytes());
    assertThat(buffer).isEqualTo(testString.getBytes());
  }

  @Test
  public void should_fail_when_buffer_does_not_equals_expected_byte_array() {
    String actual = "test";
    String expected = "differentString";

    ByteBuffer buffer = ByteBuffer.wrap(actual.getBytes());
    assertThatThrownBy(() -> assertThat(buffer).isEqualTo(expected.getBytes()))
      .isInstanceOf(AssertionError.class);
  }

  @Test
  public void should_pass_when_buffer_equals_expected_byte_buffer() {
    String testString = "test";
    ByteBuffer actual = ByteBuffer.wrap(testString.getBytes());
    ByteBuffer expected = ByteBuffer.wrap(testString.getBytes());

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void should_fail_when_buffer_does_not_equals_expected_byte_buffer() {
    ByteBuffer actual = ByteBuffer.wrap("test".getBytes());
    ByteBuffer expected = ByteBuffer.wrap("differentString".getBytes());

    assertThatThrownBy(() -> assertThat(actual).isEqualTo(expected))
      .isInstanceOf(AssertionError.class);
  }
}
