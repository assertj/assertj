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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveBinaryContent.shouldHaveBinaryContent;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.assertj.core.internal.BinaryDiffResult;
import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ShouldHaveBinaryContent create")
public class ShouldHaveBinaryContent_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    InputStream actual = new ByteArrayInputStream(new byte[] { 1, 3 });
    BinaryDiffResult diff = new BinaryDiffResult(1, 11, 3);
    // WHEN
    String errorMessage = shouldHaveBinaryContent(actual, diff).create(new TestDescription("TEST"));
    // THEN
    then(errorMessage).isEqualTo("[TEST] %n"
                                 + "InputStream%n"
                                 + " <%s>%n"
                                 + "does not have expected binary content at offset <1>, expecting:%n"
                                 + " <\"0xB\">%n"
                                 + "but was:%n"
                                 + " <\"0x3\">",
                                 actual);
  }

}
