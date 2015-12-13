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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.util;

public class GroupFormatUtil {

  static final String ELEMENT_SEPARATOR = ",";
  public static final String ELEMENT_SEPARATOR_WITH_NEWLINE = ELEMENT_SEPARATOR + org.assertj.core.util.Compatibility.System.lineSeparator();
  public static final String DEFAULT_END = "]";
  public static final String DEFAULT_START = "[";
  // 4 spaces indentation : 2 space indentation after new line + '<' + '['
  static final String INDENTATION_AFTER_NEWLINE = "    ";
  // used when formatting iterable to a single line
  static final String INDENTATION_FOR_SINGLE_LINE = " ";
  @VisibleForTesting
  static int maxLengthForSingleLineDescription = 80;

  public static void setMaxLengthForSingleLineDescription(int maxLengthForSingleLineDescription) {
    if (maxLengthForSingleLineDescription <= 0)
      throw new IllegalArgumentException("maxLengthForSingleLineDescription must be > 0 but was "
                                         + maxLengthForSingleLineDescription);
    IterableUtil.maxLengthForSingleLineDescription = maxLengthForSingleLineDescription;
  }

}
