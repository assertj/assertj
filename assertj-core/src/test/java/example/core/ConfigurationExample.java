/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.core;

// @formatter:off

import static org.assertj.core.util.Arrays.array;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.assertj.core.api.Assertions;
import org.assertj.core.configuration.Configuration;
import org.assertj.core.description.Description;
import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

// not run by the build (class name does not match *Test), running it changes the global AssertJ configuration
class ConfigurationExample {

  @Test
  void single_properties() {
    Representation myRepresentation = new StandardRepresentation();
    DateFormat myCustomDateFormat = new SimpleDateFormat("yyyy_MM_dd");
    StringBuilder report = new StringBuilder();
    // tag::single_properties[]
    Assertions.setAllowComparingPrivateFields(true);
    Assertions.setAllowExtractingPrivateFields(false);
    Assertions.setExtractBareNamePropertyMethods(false);
    Assertions.setMaxElementsForPrinting(100);
    Assertions.setMaxLengthForSingleLineDescription(250);
    Assertions.setRemoveAssertJRelatedElementsFromStackTrace(true);
    Assertions.useRepresentation(myRepresentation);
    Assertions.registerCustomDateFormat(myCustomDateFormat);
    Assertions.setPrintAssertionsDescription(true);
    Assertions.setDescriptionConsumer(description -> writeToFile(description, report));
    // end::single_properties[]
  }

  private static void writeToFile(Description description, StringBuilder report) {
    report.append(description);
  }

  @Test
  void single_line_description() {
    // tag::single_line_description[]
    String[] greatBooks = array("A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice");
    // end::single_line_description[]
  }

  @Test
  void multi_line_description() {
    // tag::multi_line_description[]
    String[] greatBooks = array("A Game of Thrones", "The Lord of the Rings", "Assassin's Apprentice", "Guards! Guards! (Discworld)");
    // end::multi_line_description[]
  }

  @Test
  void configuration() {
    // tag::configuration[]
    Configuration configuration = new Configuration();

    configuration.setBareNamePropertyExtraction(false);
    configuration.setComparingPrivateFields(false);
    configuration.setExtractingPrivateFields(false);
    configuration.setMaxElementsForPrinting(1001);
    configuration.setMaxLengthForSingleLineDescription(81);
    configuration.setRemoveAssertJRelatedElementsFromStackTrace(false);

    // don't forget to apply it!
    configuration.applyAndDisplay();
    // end::configuration[]
  }
}
// @formatter:on
