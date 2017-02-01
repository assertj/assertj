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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.util;

import org.assertj.core.api.exception.RuntimeIOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.junit.rules.ExpectedException.none;

/*
 * Tests for {@link URLs#linesOf(File, Charset)} and {@link URLs#linesOf(File, String)}.
 *
 * @author Turbo87
 * @author dorzey
 */
public class URLs_linesOf_Test {

  private static final URL SAMPLE_RESOURCE_URL = ClassLoader.getSystemResource("utf8.txt");

  private static final List<String> EXPECTED_CONTENT = newArrayList("A text file encoded in UTF-8, with diacritics:", "é à");

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_throw_exception_if_url_not_found() throws MalformedURLException {
    File missingFile = new File("missing.txt");
    assertThat(missingFile).doesNotExist();

    thrown.expect(RuntimeIOException.class);
    URLs.linesOf(missingFile.toURI().toURL(), Charset.defaultCharset());
  }

  @Test
  public void should_pass_if_resource_file_is_split_into_lines() {
    assertThat(URLs.linesOf(SAMPLE_RESOURCE_URL, StandardCharsets.UTF_8)).isEqualTo(EXPECTED_CONTENT);
  }

  @Test
  public void should_pass_if_resource_file_is_split_into_lines_using_charset() {
    assertThat(URLs.linesOf(SAMPLE_RESOURCE_URL, "UTF-8")).isEqualTo(EXPECTED_CONTENT);
  }
}
