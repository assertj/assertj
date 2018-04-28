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
import static org.assertj.core.error.ZippedElementsShouldSatisfy.zippedElementsShouldSatisfy;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.assertj.core.description.TextDescription;
import org.assertj.core.error.ZippedElementsShouldSatisfy.ZipSatisfyError;
import org.junit.Test;

public class ElementsShouldZipSatisfy_create_Test {

  @Test
  public void should_create_error_message() {
    // GIVEN
    List<ZipSatisfyError> errors = list(new ZipSatisfyError("Luke", "LUKE", "error luke"),
                                        new ZipSatisfyError("Yo-da", "YODA", "error yoda"));
    ErrorMessageFactory factory = zippedElementsShouldSatisfy(newArrayList("Luke", "Yo-da"),
                                                              newArrayList("LUKE", "YODA"),
                                                              errors);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting zipped elements of:%n" +
                                         "  <[\"Luke\", \"Yo-da\"]>%n" +
                                         "and:%n" +
                                         "  <[\"LUKE\", \"YODA\"]>%n" +
                                         "to satisfy given requirements but these zipped elements did not:" +
                                         "%n%n- (Luke, LUKE) error: error luke" +
                                         "%n%n- (Yo-da, YODA) error: error yoda"));
  }

}
