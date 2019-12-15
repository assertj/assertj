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
package org.assertj.core.api.path;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.TempFileUtil.createTempPathWithContent;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.nio.charset.Charset;
import java.nio.file.Path;

import org.assertj.core.api.PathAssert;
import org.assertj.core.api.PathAssertBaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PathAssert hasSameTextualContentAs")
public class PathAssert_hasSameTextualContentAs_Test extends PathAssertBaseTest {

  private static Path expected;

  @BeforeAll
  public static void beforeOnce() {
    expected = mock(Path.class);
  }

  @Override
  protected PathAssert invoke_api_method() {
    return assertions.hasSameTextualContentAs(expected);
  }

  @Override
  protected void verify_internal_effects() {
    verify(paths).assertHasSameContentAs(getInfo(assertions), getActual(assertions), defaultCharset, expected, defaultCharset);
  }

  @Test
  public void should_use_charset_specified_by_usingCharset_to_read_actual_file_content() throws Exception {
    // GIVEN
    Charset turkishCharset = Charset.forName("windows-1254");
    Path actual = createTempPathWithContent("Gerçek", turkishCharset);
    Path expected = createTempPathWithContent("Gerçek", defaultCharset);
    // WHEN/THEN
    then(actual).usingCharset(turkishCharset)
                .hasSameTextualContentAs(expected);
  }

  @Test
  public void should_allow_charset_to_be_specified_for_reading_expected_file_content() throws Exception {
    // GIVEN
    Charset turkishCharset = Charset.forName("windows-1254");
    Path actual = createTempPathWithContent("Gerçek", defaultCharset);
    Path expected = createTempPathWithContent("Gerçek", turkishCharset);
    // WHEN/THEN
    then(actual).hasSameTextualContentAs(expected, turkishCharset);
  }
}
