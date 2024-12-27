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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api.inputstream;

import static java.lang.System.lineSeparator;
import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_16;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.stream.Stream;

import org.assertj.core.api.InputStreamAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link InputStreamAssert#isNotEncodedIn(Charset)} methods.
 *
 * @author Ludovic VIEGAS
 */
class InputStreamAssert_isNotEncodedIn_Test {

  private final String specialStr = "àâäéèêëiìîïoòôöuùûü";

  public static Stream<Charset> commonCharsetsArguments() {
    return Stream.of(UTF_8, ISO_8859_1, UTF_16, Charset.forName("windows-1252"));
  }

  @ParameterizedTest(name = "[{index}] Charset {0}")
  @MethodSource("commonCharsetsArguments")
  void should_validate(Charset charset) throws Exception {
    // Test charset against UTF-8, and UTF-8 against UTF-16
    Charset otherCharset = charset != UTF_8 ? UTF_8 : UTF_16;

    InputStream inputStream = toInputStream(specialStr, charset);
    assertThat(inputStream).isNotEncodedIn(otherCharset);

    inputStream.reset();
    assertThat(inputStream).isNotEncodedIn(otherCharset, false);

    inputStream.reset();
    assertThat(inputStream).isNotEncodedIn(otherCharset, true);
  }

  @Test
  void should_validate_lenient() {
    String replacement = UTF_8.newDecoder().replacement();

    String str = Stream.of(
                           "line1 is ok",
                           "line2 is nok" + replacement,
                           "line3 is ok",
                           "line4 is nok" + replacement)
                       .collect(joining(lineSeparator()));

    // Test lenient validation rejects the replacement char
    final InputStream inputStream = toInputStream(str, UTF_8);
    assertThat(inputStream).isNotEncodedIn(UTF_8, false);

    // Test strict validation does not accept the replacement char
    assertThatCode(() -> {
      inputStream.reset();
      assertThat(inputStream).isNotEncodedIn(UTF_8, true);
    })
      .hasMessageContaining("InputStream should not be encoded in " + UTF_8);
  }

  private static ByteArrayInputStream toInputStream(String str, Charset charset) {
    return new ByteArrayInputStream(str.getBytes(charset));
  }
}
