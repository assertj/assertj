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
package org.assertj.tests.core.configuration;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.function.Consumer;
import org.assertj.core.configuration.Configuration;
import org.assertj.core.description.Description;
import org.assertj.tests.core.testkit.MutatesGlobalConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

@MutatesGlobalConfiguration
class Configuration_describe_Test {

  @Test
  void should_give_a_human_readable_description() throws Exception {
    // GIVEN
    Configuration configuration = new NonDefaultConfiguration();
    configuration.setDescriptionConsumer(new Consumer<Description>() {
      @Override
      public void accept(Description t) {
        System.out.println(t);
      }

      @Override
      public String toString() {
        return "sysout";
      }
    });
    // WHEN
    String description = configuration.describe();
    // THEN
    then(description).isEqualTo(format("Applying configuration org.assertj.tests.core.configuration.NonDefaultConfiguration%n" +
                                       "- representation .................................. = BinaryRepresentation%n" +
                                       "- comparingPrivateFieldsEnabled ................... = false%n" +
                                       "- extractingPrivateFieldsEnabled .................. = false%n" +
                                       "- bareNamePropertyExtractionEnabled ............... = false%n" +
                                       "- lenientDateParsingEnabled ....................... = true%n" +
                                       "- additional date formats ......................... = [yyyy_MM_dd, yyyy|MM|dd]%n" +
                                       "- maxLengthForSingleLineDescription ............... = 81%n" +
                                       "- maxElementsForPrinting .......................... = 1001%n" +
                                       "- maxStackTraceElementsDisplayed................... = 4%n" +
                                       "- printAssertionsDescription ...................... = false%n" +
                                       "- descriptionConsumer ............................. = sysout%n" +
                                       "- removeAssertJRelatedElementsFromStackTraceEnabled = false%n" +
                                       "- preferredAssumptionException .................... = TEST_NG(org.testng.SkipException)%n"));
  }

  @AfterEach
  public void afterEach() {
    // revert whatever we did in the other tests
    Configuration.DEFAULT_CONFIGURATION.apply();
  }

}
