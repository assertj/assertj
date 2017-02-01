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

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveName.shouldHaveName;

/**
 * Tests for <code>{@link org.assertj.core.error.ShouldHaveName#shouldHaveName(java.io.File, String)}</code>
 * 
 * @author Jean-Christophe Gay
 */
public class ShouldHaveName_create_Test {

  private final String expectedName = "java";

  private File actual = new FakeFile("somewhere/actual-file".replace("/", File.separator));

  @Test
  public void should_create_error_message() {
    assertThat(createMessage()).isEqualTo(String.format("[TEST] %n" +
                                                        "Expecting%n" +
                                                        "  <" + actual + ">%n" +
                                                        "to have name:%n" +
                                                        "  <\"" + expectedName + "\">%n" +
                                                        "but had:%n" +
                                                        "  <\"actual-file\">"));
  }

  private String createMessage() {
    return shouldHaveName(actual, expectedName).create(new TestDescription("TEST"), new StandardRepresentation());
  }
}
