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
package org.assertj.core.presentation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mariusz Smykula
 */
final class NumberGrouping {

  private static final String UNDERSCORE_SEPARATOR = "_";
  private static Pattern hexGroupPattern = Pattern.compile("([0-9|A-Z]{4})");
  private static Pattern binaryGroupPattern = Pattern.compile("([0-1]{8})");

  static String toHexLiteral(String value) {
    return value.length() > 4 ? toHexLiteral(hexGroupPattern, value) : value;
  }

  static String toBinaryLiteral(String value) {
    return toHexLiteral(binaryGroupPattern, value);
  }

  private static String toHexLiteral(Pattern pattern, String value) {

    Matcher matcher = pattern.matcher(value);
    StringBuilder literalBuilder = new StringBuilder();
    while (matcher.find()) {
      String byteGroup = matcher.group(1);
      if (notEmpty(literalBuilder)) {
        literalBuilder.append(UNDERSCORE_SEPARATOR);
      }
      literalBuilder.append(byteGroup);
    }

    return literalBuilder.toString();
  }

  private static boolean notEmpty(StringBuilder sb) {
    return sb.length() != 0;
  }

  private NumberGrouping() {
  }

}