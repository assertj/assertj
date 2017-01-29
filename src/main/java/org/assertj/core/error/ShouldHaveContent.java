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
package org.assertj.core.error;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;

import org.assertj.core.util.diff.Delta;


/**
 * Creates an error message indicating that an assertion that verifies that a file/path has a given text content failed.
 * 
 * @author Olivier Michallat
 */
public class ShouldHaveContent extends AbstractShouldHaveTextContent {

  /**
   * Creates a new <code>{@link ShouldHaveContent}</code>.
   * @param actual the actual file in the failed assertion.
   * @param charset the charset that was used to read the file.
   * @param diffs the differences between {@code actual} and the expected text that was provided in the assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveContent(File actual, Charset charset, List<Delta<String>> diffs) {
    return new ShouldHaveContent(actual, charset, diffsAsString(diffs));
  }
  
  /**
   * Creates a new <code>{@link ShouldHaveContent}</code>.
   * @param actual the actual path in the failed assertion.
   * @param charset the charset that was used to read the the path.
   * @param diffs the differences between {@code actual} and the expected text that was provided in the assertion.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveContent(Path actual, Charset charset, List<Delta<String>> diffs) {
    return new ShouldHaveContent(actual, charset, diffsAsString(diffs));
  }

  private ShouldHaveContent(File actual, Charset charset, String diffs) {
    super("%nFile:%n  <%s>%nread with charset <%s> does not have the expected content:%n", actual, charset);
    this.diffs = diffs;
  }
  
  private ShouldHaveContent(Path actual, Charset charset, String diffs) {
    super("%nPath:%n  <%s>%nread with charset <%s> does not have the expected content:%n", actual, charset);
    this.diffs = diffs;
  }
}
