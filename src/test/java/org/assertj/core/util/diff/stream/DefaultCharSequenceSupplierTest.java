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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.util.diff.stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.delete;
import static java.nio.file.Files.write;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.util.Files.newTemporaryFile;
import static org.assertj.core.util.diff.stream.DefaultCharSequenceSupplier.EMPTY_BUFFER;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SoftAssertionsExtension.class)
class DefaultCharSequenceSupplierTest {
  static final String STRING = "123";

  private File file;
  private SeekableByteChannel channel;
  private DefaultCharSequenceSupplier supplier;
  private FileInputStream fileInputStream;

  @BeforeEach
  void setUp() throws IOException {
    file = newTemporaryFile();
    write(file.toPath(), STRING.getBytes(UTF_8));
    fileInputStream = new FileInputStream(file);
    channel = fileInputStream.getChannel();
    supplier = new DefaultCharSequenceSupplier(UTF_8, channel);
  }

  @AfterEach
  void tearDown() throws IOException {
    channel.close();
    fileInputStream.close();
    delete(file.toPath());
  }

  @Test
  public void apply(SoftAssertions softly) throws IOException {
    softly.assertThat(toString(supplier.get(0L, 1L))).isEqualTo(STRING.substring(0, 1));
    softly.assertThat(toString(supplier.get(0L, 2L))).isEqualTo(STRING.substring(0, 2));
    softly.assertThat(toString(supplier.get(0L, 3L))).isEqualTo(STRING);
    softly.assertThat(toString(supplier.get(1L, 2L))).isEqualTo(STRING.substring(1, 2));
    softly.assertThat(toString(supplier.get(1L, 3L))).isEqualTo(STRING.substring(1, 3));
    softly.assertThat(toString(supplier.get(2L, 3L))).isEqualTo(STRING.substring(2, 3));
  }

  @Test
  @Timeout(value = 10, unit = SECONDS) // protect against infinite loop
  public void apply_with_empty_interval(SoftAssertions softly) throws IOException {
    softly.assertThat((CharSequence) supplier.get(0L, 0L)).isSameAs(EMPTY_BUFFER);
    softly.assertThat((CharSequence) supplier.get(2L, 2L)).isSameAs(EMPTY_BUFFER);
  }

  private static String toString(CharSequence charSequence) {
    return (charSequence == null) ? null : charSequence.chars().mapToObj(i -> Character.toString((char) i)).collect(joining());
  }
}
