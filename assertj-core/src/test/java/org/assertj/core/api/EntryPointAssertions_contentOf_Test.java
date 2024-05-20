/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.BDDAssertions.then;

import java.io.File;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("EntryPoint assertions contentOf method")
class EntryPointAssertions_contentOf_Test extends EntryPointAssertionsBaseTest {

  @ParameterizedTest
  @MethodSource("fileContentOfWithCharsetFunctions")
  void should_read_file_content_with_charset(BiFunction<File, Charset, String> contentOfWithCharsetFunction) {
    // GIVEN
    File sampleFile = new File("src/test/resources/utf8.txt");
    // WHEN
    String content = contentOfWithCharsetFunction.apply(sampleFile, UTF_8);
    // THEN
    then(content).isEqualTo("A text file encoded in UTF-8, with diacritics:\né à");
  }

  private static Stream<BiFunction<File, Charset, String>> fileContentOfWithCharsetFunctions() {
    return Stream.of(Assertions::contentOf, BDDAssertions::contentOf, withAssertions::contentOf);
  }

  @ParameterizedTest
  @MethodSource("fileContentOfWithCharsetAsStringFunctions")
  void should_read_file_content_with_charset_as_string(BiFunction<File, String, String> contentOfWithCharsetFunction) {
    // GIVEN
    File sampleFile = new File("src/test/resources/utf8.txt");
    // WHEN
    String content = contentOfWithCharsetFunction.apply(sampleFile, "UTF8");
    // THEN
    then(content).isEqualTo("A text file encoded in UTF-8, with diacritics:\né à");
  }

  private static Stream<BiFunction<File, String, String>> fileContentOfWithCharsetAsStringFunctions() {
    return Stream.of(Assertions::contentOf, BDDAssertions::contentOf, withAssertions::contentOf);
  }

  @ParameterizedTest
  @MethodSource("fileContentOfWithDefaultCharsetFunctions")
  void should_read_file_content_with_default_charset(Function<File, String> contentOfWithDefaultCharsetFunction) {
    // GIVEN
    File sampleFile = new File("src/test/resources/ascii.txt");
    // WHEN
    String content = contentOfWithDefaultCharsetFunction.apply(sampleFile);
    // THEN
    then(content).isEqualTo("abc");
  }

  private static Stream<Function<File, String>> fileContentOfWithDefaultCharsetFunctions() {
    return Stream.of(Assertions::contentOf, BDDAssertions::contentOf, withAssertions::contentOf);
  }

  @ParameterizedTest
  @MethodSource("urlContentOfWithCharsetFunctions")
  void should_read_url_content_with_charset(BiFunction<URL, Charset, String> contentOfWithCharsetFunction) {
    // GIVEN
    URL sampleUrl = ClassLoader.getSystemResource("utf8.txt");
    // WHEN
    String content = contentOfWithCharsetFunction.apply(sampleUrl, UTF_8);
    // THEN
    then(content).isEqualTo("A text file encoded in UTF-8, with diacritics:\né à");
  }

  private static Stream<BiFunction<URL, Charset, String>> urlContentOfWithCharsetFunctions() {
    return Stream.of(Assertions::contentOf, BDDAssertions::contentOf, withAssertions::contentOf);
  }

  @ParameterizedTest
  @MethodSource("urlContentOfWithCharsetAsStringFunctions")
  void should_read_url_content_with_charset_as_string(BiFunction<URL, String, String> contentOfWithCharsetFunction) {
    // GIVEN
    URL sampleUrl = ClassLoader.getSystemResource("utf8.txt");
    // WHEN
    String content = contentOfWithCharsetFunction.apply(sampleUrl, "UTF8");
    // THEN
    then(content).isEqualTo("A text file encoded in UTF-8, with diacritics:\né à");
  }

  private static Stream<BiFunction<URL, String, String>> urlContentOfWithCharsetAsStringFunctions() {
    return Stream.of(Assertions::contentOf, BDDAssertions::contentOf, withAssertions::contentOf);
  }

  @ParameterizedTest
  @MethodSource("urlContentOfWithDefaultCharsetFunctions")
  void should_read_URL_content_with_default_charset(Function<URL, String> contentOfWithDefaultCharsetFunction) {
    // GIVEN
    URL sampleUrl = ClassLoader.getSystemResource("ascii.txt");
    // WHEN
    String content = contentOfWithDefaultCharsetFunction.apply(sampleUrl);
    // THEN
    then(content).isEqualTo("abc");
  }

  private static Stream<Function<URL, String>> urlContentOfWithDefaultCharsetFunctions() {
    return Stream.of(Assertions::contentOf, BDDAssertions::contentOf, withAssertions::contentOf);
  }

}
