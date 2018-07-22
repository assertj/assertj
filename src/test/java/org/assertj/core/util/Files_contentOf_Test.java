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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

import java.io.File;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Files#contentOf(File, Charset)} and {@link Files#contentOf(File, String)}.
 * 
 * @author Olivier Michallat
 */
public class Files_contentOf_Test {
  private final File sampleFile = new File("src/test/resources/utf8.txt");
  private final String expectedContent = "A text file encoded in UTF-8, with diacritics:\né à";

  @Test
  public void should_throw_exception_if_charset_is_null() {
    Charset charset = null;
    assertThatNullPointerException().isThrownBy(() -> Files.contentOf(new File("test"), charset));
  }

  @Test
  public void should_throw_exception_if_charset_name_does_not_exist() {
    assertThatIllegalArgumentException().isThrownBy(() -> Files.contentOf(new File("test"), "Klingon"));
  }

  @Test
  public void should_throw_exception_if_file_not_found() {
    File missingFile = new File("missing.txt");
    assertThat(missingFile.exists()).isFalse();

    assertThatExceptionOfType(UncheckedIOException.class).isThrownBy(() -> Files.contentOf(missingFile,
                                                                                           Charset.defaultCharset()));
  }

  @Test
  public void should_load_file_using_charset() {
    assertThat(Files.contentOf(sampleFile, StandardCharsets.UTF_8)).isEqualTo(expectedContent);
  }

  @Test
  public void should_load_file_using_charset_name() {
    assertThat(Files.contentOf(sampleFile, "UTF-8")).isEqualTo(expectedContent);
  }
}
