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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveDigest.shouldHaveDigest;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.MessageDigest;

import org.assertj.core.internal.DigestDiff;
import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShouldHaveDigest_create_Test {

  private static final TestDescription TEST_DESCRIPTION = new TestDescription("TEST");

  private DigestDiff diff;

  @BeforeEach
  public void setup() throws Exception {
    diff = new DigestDiff("actualHex", "actualHex", MessageDigest.getInstance("MD5"));
  }

  @Test
  public void should_create_error_message_with_File() {
    // GIVEN
    File actual = new FakeFile("actual.png");
    // WHEN
    String message = shouldHaveDigest(actual, diff).create(TEST_DESCRIPTION, STANDARD_REPRESENTATION);
    // THEN
    assertThat(message).isEqualTo(format("[TEST] %n" +
                                         "Expecting File " + actual + " MD5 digest to be:%n" +
                                         "  <\"" + diff.getExpected() + "\">%n" +
                                         "but was:%n" +
                                         "  <\"" + diff.getActual() + "\">"));
  }

  @Test
  public void should_create_error_message_with_Path() {
    // GIVEN
    Path actual = mock(Path.class);
    // WHEN
    String message = shouldHaveDigest(actual, diff).create(TEST_DESCRIPTION, STANDARD_REPRESENTATION);
    // THEN
    assertThat(message).isEqualTo(format("[TEST] %n" +
                                         "Expecting Path " + actual + " MD5 digest to be:%n" +
                                         "  <\"" + diff.getExpected() + "\">%n" +
                                         "but was:%n" +
                                         "  <\"" + diff.getActual() + "\">"));
  }

  @Test
  public void should_create_error_message_with_InputStream() {
    // GIVEN
    InputStream actual = mock(InputStream.class);
    // WHEN
    String message = shouldHaveDigest(actual, diff).create(TEST_DESCRIPTION, STANDARD_REPRESENTATION);
    // THEN
    assertThat(message).isEqualTo(format("[TEST] %n" +
                                         "Expecting InputStream " + actual + " MD5 digest to be:%n" +
                                         "  <\"" + diff.getExpected() + "\">%n" +
                                         "but was:%n" +
                                         "  <\"" + diff.getActual() + "\">"));
  }

}
