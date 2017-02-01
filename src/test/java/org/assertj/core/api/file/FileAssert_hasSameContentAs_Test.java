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
package org.assertj.core.api.file;

import static java.nio.charset.Charset.defaultCharset;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.assertj.core.api.FileAssert;
import org.assertj.core.api.FileAssertBaseTest;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for <code>{@link FileAssert#hasSameContentAs(java.io.File)}</code>.
 * 
 * @author Yvonne Wang
 */
public class FileAssert_hasSameContentAs_Test extends FileAssertBaseTest {

  private static File expected;

  @BeforeClass
  public static void beforeOnce() {
    expected = new File("xyz");
  }

  @Override
  protected FileAssert invoke_api_method() {
    return assertions.hasSameContentAs(expected);
  }

  @Override
  protected void verify_internal_effects() {
    verify(files).assertSameContentAs(getInfo(assertions), getActual(assertions), defaultCharset(), expected, defaultCharset());
  }

  @Test
  public void should_use_charset_specified_by_usingCharset_to_read_actual_file_content() throws Exception {
    Charset turkishCharset = Charset.forName("windows-1254");
    File actual = createDeleteOnExitTempFileWithContent("Gerçek", turkishCharset);
    File expected = createDeleteOnExitTempFileWithContent("Gerçek", defaultCharset());

    assertThat(actual).usingCharset(turkishCharset).hasSameContentAs(expected);
  }

  @Test
  public void should_allow_charset_to_be_specified_for_reading_expected_file_content() throws Exception {
    Charset turkishCharset = Charset.forName("windows-1254");
    File actual = createDeleteOnExitTempFileWithContent("Gerçek", defaultCharset());
    File expected = createDeleteOnExitTempFileWithContent("Gerçek", turkishCharset);

    assertThat(actual).hasSameContentAs(expected, turkishCharset);
  }

  private File createDeleteOnExitTempFileWithContent(String content, Charset charset) throws IOException {
    Path tempFile = Files.createTempFile("test", "test");
    tempFile.toFile().deleteOnExit();
    Files.write(tempFile, asList(content), charset);
    return tempFile.toFile();
  }
}
