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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.util.introspection;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Utility class for conversion between different case formats
 */
public class CaseFormatUtils {

  private static final String WORD_SEPARATOR_REGEX = "[ _-]";

  private CaseFormatUtils() {}

  /**
   * Converts an input string into camelCase.
   * <p> 
   * The input string may use any of the well known case styles: Pascal, Snake, Kebab or even Camel.
   * Already camelCased strings will be returned as is. 
   * Mix and match is also an option; the algorithm will try its best to give an acceptable answer.
   * Mixed case will be preserved, i.e {@code assertThat(toCamelCase("miXedCAse")).isEqualTo("miXedCAse")}
   * 
   * @param s the string to be converted
   * @return the input string converted to camelCase
   */
  public static String toCamelCase(String s) {
    List<String> words = extractWords(requireNonNull(s));
    return IntStream.range(0, words.size())
                    .mapToObj(i -> adjustWordCase(words.get(i), i > 0))
                    .collect(joining());
  }

  private static List<String> extractWords(String s) {
    String[] chunks = s.split(WORD_SEPARATOR_REGEX);
    return Arrays.stream(chunks)
                 .map(String::trim)
                 .filter(w -> !w.isEmpty())
                 .collect(toList());
  }

  private static String adjustWordCase(String s, boolean firstLetterUpperCased) {
    String firstLetter = s.substring(0, 1);
    String trailingLetters = s.substring(1);
    return (firstLetterUpperCased ? firstLetter.toUpperCase() : firstLetter.toLowerCase()) +
           (isAllCaps(s) ? trailingLetters.toLowerCase() : trailingLetters);
  }

  private static boolean isAllCaps(String s) {
    return s.toUpperCase().equals(s);
  }
}
