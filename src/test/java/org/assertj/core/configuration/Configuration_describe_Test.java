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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.configuration;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class Configuration_describe_Test {

  @Test
  public void should_give_a_human_readable_description() throws Exception {
    // GIVEN
    Configuration configuration = new NonDefaultConfiguration();
    // WHEN
    String description = configuration.describe();
    // THEN
    assertThat(description).isEqualTo(format("- representation .................................. = BinaryRepresentation%n" +
                                             "- comparingPrivateFieldsEnabled ................... = false%n" +
                                             "- extractingPrivateFieldsEnabled .................. = false%n" +
                                             "- bareNamePropertyExtractionEnabled ............... = false%n" +
                                             "- lenientDateParsingEnabled ....................... = true%n" +
                                             "- additionnal date formats ........................ = [yyyy_MM_dd, yyyy|MM|dd]%n" +
                                             "- maxLengthForSingleLineDescription ............... = 81%n" +
                                             "- maxElementsForPrinting .......................... = 1001%n" +
                                             "- removeAssertJRelatedElementsFromStackTraceEnabled = false%n"));
  }

  @AfterEach
  public void afterEach() {
    // revert whatever we did in the other tests
    Configuration.DEFAULT_CONFIGURATION.apply();
  }

}
