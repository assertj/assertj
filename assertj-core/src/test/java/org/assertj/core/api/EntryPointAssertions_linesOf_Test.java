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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.BDDAssertions.then;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import org.assertj.core.util.ResourceUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions linesOf method")
class EntryPointAssertions_linesOf_Test extends EntryPointAssertionsBaseTest {

  @ParameterizedTest
  @MethodSource("fileLinesOfWithCharsetFunctions")
  void should_read_file_lines_with_charset(BiFunction<File, Charset, List<String>> linesOfWithCharsetFunction) {
    // GIVEN
    File sampleFile = ResourceUtil.getResource("utf8.txt").toFile();
    // WHEN
    List<String> lines = linesOfWithCharsetFunction.apply(sampleFile, UTF_8);
    // THEN
    then(lines).containsExactly("A text file encoded in UTF-8, with diacritics:", "é à");
  }

  private static Stream<BiFunction<File, Charset, List<String>>> fileLinesOfWithCharsetFunctions() {
    return Stream.of(Assertions::linesOf, BDDAssertions::linesOf, withAssertions::linesOf);
  }

  @ParameterizedTest
  @MethodSource("fileLinesOfWithCharsetAsStringFunctions")
  void should_read_file_lines_with_charset_as_string(BiFunction<File, String, List<String>> linesOfWithCharsetFunction) {
    // GIVEN
    File sampleFile = ResourceUtil.getResource("utf8.txt").toFile();
    // WHEN
    List<String> lines = linesOfWithCharsetFunction.apply(sampleFile, "UTF8");
    // THEN
    then(lines).containsExactly("A text file encoded in UTF-8, with diacritics:", "é à");
  }

  private static Stream<BiFunction<File, String, List<String>>> fileLinesOfWithCharsetAsStringFunctions() {
    return Stream.of(Assertions::linesOf, BDDAssertions::linesOf, withAssertions::linesOf);
  }

  @ParameterizedTest
  @MethodSource("fileLinesOfWithDefaultCharsetFunctions")
  void should_read_file_lines_with_default_charset(Function<File, List<String>> linesOfWithDefaultCharsetFunction) {
    // GIVEN
    File sampleFile = ResourceUtil.getResource("ascii.txt").toFile();
    // WHEN
    List<String> lines = linesOfWithDefaultCharsetFunction.apply(sampleFile);
    // THEN
    then(lines).containsExactly("abc");
  }

  private static Stream<Function<File, List<String>>> fileLinesOfWithDefaultCharsetFunctions() {
    return Stream.of(Assertions::linesOf, BDDAssertions::linesOf, withAssertions::linesOf);
  }

  @ParameterizedTest
  @MethodSource("pathLinesOfWithCharsetFunctions")
  void should_read_path_lines_with_charset(BiFunction<Path, Charset, List<String>> linesOfWithCharsetFunction) {
    // GIVEN
    Path sampleFile = ResourceUtil.getResource("utf8.txt");
    // WHEN
    List<String> lines = linesOfWithCharsetFunction.apply(sampleFile, UTF_8);
    // THEN
    then(lines).containsExactly("A text file encoded in UTF-8, with diacritics:", "é à");
  }

  private static Stream<BiFunction<Path, Charset, List<String>>> pathLinesOfWithCharsetFunctions() {
    return Stream.of(Assertions::linesOf, BDDAssertions::linesOf, withAssertions::linesOf);
  }

  @ParameterizedTest
  @MethodSource("pathLinesOfWithCharsetAsStringFunctions")
  void should_read_path_lines_with_charset_as_string(BiFunction<Path, String, List<String>> linesOfWithCharsetFunction) {
    // GIVEN
    Path sampleFile = ResourceUtil.getResource("utf8.txt");
    // WHEN
    List<String> lines = linesOfWithCharsetFunction.apply(sampleFile, "UTF8");
    // THEN
    then(lines).containsExactly("A text file encoded in UTF-8, with diacritics:", "é à");
  }

  private static Stream<BiFunction<Path, String, List<String>>> pathLinesOfWithCharsetAsStringFunctions() {
    return Stream.of(Assertions::linesOf, BDDAssertions::linesOf, withAssertions::linesOf);
  }

  @ParameterizedTest
  @MethodSource("pathLinesOfWithDefaultCharsetFunctions")
  void should_read_path_lines_with_default_charset(Function<Path, List<String>> linesOfWithDefaultCharsetFunction) {
    // GIVEN
    Path sampleFile = ResourceUtil.getResource("ascii.txt");
    // WHEN
    List<String> lines = linesOfWithDefaultCharsetFunction.apply(sampleFile);
    // THEN
    then(lines).containsExactly("abc");
  }

  private static Stream<Function<Path, List<String>>> pathLinesOfWithDefaultCharsetFunctions() {
    return Stream.of(Assertions::linesOf, BDDAssertions::linesOf, withAssertions::linesOf);
  }

  @ParameterizedTest
  @MethodSource("urlLinesOfWithCharsetFunctions")
  void should_read_url_lines_with_charset(BiFunction<URL, Charset, List<String>> linesOfWithCharsetFunction) {
    // GIVEN
    URL sampleUrl = ClassLoader.getSystemResource("utf8.txt");
    // WHEN
    List<String> lines = linesOfWithCharsetFunction.apply(sampleUrl, UTF_8);
    // THEN
    then(lines).containsExactly("A text file encoded in UTF-8, with diacritics:", "é à");
  }

  private static Stream<BiFunction<URL, Charset, List<String>>> urlLinesOfWithCharsetFunctions() {
    return Stream.of(Assertions::linesOf, BDDAssertions::linesOf, withAssertions::linesOf);
  }

  @ParameterizedTest
  @MethodSource("urlLinesOfWithCharsetAsStringFunctions")
  void should_read_url_lines_with_charset_as_string(BiFunction<URL, String, List<String>> linesOfWithCharsetFunction) {
    // GIVEN
    URL sampleUrl = ClassLoader.getSystemResource("utf8.txt");
    // WHEN
    List<String> lines = linesOfWithCharsetFunction.apply(sampleUrl, "UTF8");
    // THEN
    then(lines).containsExactly("A text file encoded in UTF-8, with diacritics:", "é à");
  }

  private static Stream<BiFunction<URL, String, List<String>>> urlLinesOfWithCharsetAsStringFunctions() {
    return Stream.of(Assertions::linesOf, BDDAssertions::linesOf, withAssertions::linesOf);
  }

  @ParameterizedTest
  @MethodSource("urlLinesOfWithDefaultCharsetFunctions")
  void should_read_URL_lines_with_default_charset(Function<URL, List<String>> linesOfWithDefaultCharsetFunction) {
    // GIVEN
    URL sampleUrl = ClassLoader.getSystemResource("ascii.txt");
    // WHEN
    List<String> lines = linesOfWithDefaultCharsetFunction.apply(sampleUrl);
    // THEN
    then(lines).containsExactly("abc");
  }

  private static Stream<Function<URL, List<String>>> urlLinesOfWithDefaultCharsetFunctions() {
    return Stream.of(Assertions::linesOf, BDDAssertions::linesOf, withAssertions::linesOf);
  }

}
