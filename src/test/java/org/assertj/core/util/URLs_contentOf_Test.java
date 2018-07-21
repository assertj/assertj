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


import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/*
 * Tests for {@link URLs#contentOf(File, Charset)} and {@link URLs#contentOf(File, String)}.
 *
 * @author Turbo87
 * @author dorzey
 */
public class URLs_contentOf_Test {

  private final URL sampleResourceURL = ClassLoader.getSystemResource("utf8.txt");
  private final String expectedContent = "A text file encoded in UTF-8, with diacritics:\né à";

  @Test
  public void should_throw_exception_if_url_not_found() throws MalformedURLException {
    File missingFile = new File("missing.txt");
    assertThat(missingFile.exists()).isFalse();

    assertThatExceptionOfType(UncheckedIOException.class).isThrownBy(() -> URLs.contentOf(missingFile.toURI().toURL(),
                                                                                          Charset.defaultCharset()));
  }

  @Test
  public void should_load_resource_from_url_using_charset() {
    assertThat(URLs.contentOf(sampleResourceURL, StandardCharsets.UTF_8)).isEqualTo(expectedContent);
  }

  @Test
  public void should_load_resource_from_url_using_charset_name() {
    assertThat(URLs.contentOf(sampleResourceURL, "UTF-8")).isEqualTo(expectedContent);
  }
}
