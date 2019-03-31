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
import static org.assertj.core.error.buffer.bytebuffer.ContentsShouldContain.contentsShouldContain;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;

import com.google.common.base.Charsets;

/**
 * Tests for :
 * <code>{@link org.assertj.core.api.AbstractByteBufferAssert#contains(byte[])}</code>.
 * <code>{@link org.assertj.core.api.AbstractByteBufferAssert#contains(String)}</code>.
 * <code>{@link org.assertj.core.api.AbstractByteBufferAssert#contains(ByteBuffer)}</code>.
 * <code>{@link org.assertj.core.api.AbstractByteBufferAssert#contains(String, Charset)}</code>.
 *
 * @author Jean de Leeuw
 */
public class ByteBuffer_contains_Test {

  @Test
  public void should_pass_when_buffer_contains_expected_string_with_default_charset() {
    ByteBuffer buffer = ByteBuffer.wrap("test".getBytes());
    assertThat(buffer).contains("es");
  }

  @Test
  public void should_fail_when_buffer_does_not_contain_expected_string_with_default_charset() {
    String actual = "test";
    String expected = "xy";

    ByteBuffer buffer = ByteBuffer.wrap(actual.getBytes());
    assertThatThrownBy(() -> assertThat(buffer).contains(expected))
      .isInstanceOf(AssertionError.class)
      .hasMessage(contentsShouldContain(expected, buffer, Charset.defaultCharset()).create());
  }

  @Test
  public void should_pass_when_buffer_contains_expected_string_with_specified_charset() {
    Charset specified = Charsets.UTF_8;

    ByteBuffer buffer = specified.encode("test");
    assertThat(buffer).contains("es", specified);
  }

  @Test
  public void should_fail_when_buffer_does_not_contain_expected_string_with_specified_charset() {
    String actual = "test";
    String expected = "xy";
    Charset specified = Charsets.UTF_8;

    ByteBuffer buffer = specified.encode(actual);
    assertThatThrownBy(() -> assertThat(buffer).contains(expected, specified))
      .isInstanceOf(AssertionError.class)
      .hasMessage(contentsShouldContain(expected, buffer, specified).create());
  }

  @Test
  public void should_fail_when_buffer_does_not_contains_expected_string_with_different_charsets() {
    String fullContent = "test";
    String containsContent = "es";
    Charset actualCharset = Charsets.UTF_8;
    Charset expectedCharset = Charsets.UTF_16;

    ByteBuffer buffer = actualCharset.encode(fullContent);
    assertThatThrownBy(() -> assertThat(buffer).contains(containsContent, expectedCharset))
      .isInstanceOf(AssertionError.class)
      .hasMessage(contentsShouldContain(containsContent, buffer, expectedCharset).create());
  }

  @Test
  public void should_pass_when_buffer_contains_expected_byte_array() {
    ByteBuffer buffer = ByteBuffer.wrap("test".getBytes());
    assertThat(buffer).contains("es".getBytes());
  }

  @Test
  public void should_fail_when_buffer_does_not_contain_expected_byte_array() {
    String actual = "test";
    String expected = "xy";

    ByteBuffer buffer = ByteBuffer.wrap(actual.getBytes());
    assertThatThrownBy(() -> assertThat(buffer).contains(expected.getBytes()))
      .isInstanceOf(AssertionError.class);
  }

  @Test
  public void should_pass_when_buffer_contains_expected_byte_buffer() {
    ByteBuffer actual = ByteBuffer.wrap("test".getBytes());
    ByteBuffer expected = ByteBuffer.wrap("es".getBytes());

    assertThat(actual).contains(expected);
  }

  @Test
  public void should_fail_when_buffer_does_not_contain_expected_byte_buffer() {
    ByteBuffer actual = ByteBuffer.wrap("test".getBytes());
    ByteBuffer expected = ByteBuffer.wrap("xy".getBytes());

    assertThatThrownBy(() -> assertThat(actual).contains(expected))
      .isInstanceOf(AssertionError.class);
  }
}
