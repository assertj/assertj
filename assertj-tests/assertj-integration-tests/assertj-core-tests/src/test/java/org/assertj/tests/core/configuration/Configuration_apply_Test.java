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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.assertj.core.api.AssumptionExceptionFactory;
import org.assertj.core.configuration.Configuration;
import org.assertj.core.internal.Failures;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.introspection.FieldSupport;
import org.assertj.core.util.introspection.Introspection;
import org.assertj.tests.core.testkit.MutatesGlobalConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

@MutatesGlobalConfiguration
class Configuration_apply_Test {

  @Test
  void apply_should_change_assertj_behavior() throws Exception {
    // GIVEN
    Configuration configuration = new NonDefaultConfiguration();
    // WHEN
    configuration.apply();
    // THEN
    then(FieldSupport.extraction().isAllowedToUsePrivateFields()).isEqualTo(configuration.extractingPrivateFieldsEnabled());
    then(FieldSupport.comparison().isAllowedToUsePrivateFields()).isEqualTo(configuration.comparingPrivateFieldsEnabled());
    then(Introspection.canExtractBareNamePropertyMethods()).isEqualTo(configuration.bareNamePropertyExtractionEnabled());
    then(configuration.representation()).isNotSameAs(STANDARD_REPRESENTATION);
    // a bit dodgy but since our custom representation inherits StandardRepresentation, changing maxElementsForPrinting and
    // maxLengthForSingleLineDescription will be effective.
    then(StandardRepresentation.getMaxElementsForPrinting()).isEqualTo(configuration.maxElementsForPrinting());
    then(StandardRepresentation.getMaxStackTraceElementsDisplayed()).isEqualTo(configuration.maxStackTraceElementsDisplayed());
    then(StandardRepresentation.getMaxLengthForSingleLineDescription()).isEqualTo(configuration.maxLengthForSingleLineDescription());
    boolean removeAssertJRelatedElementsFromStackTrace = Failures.instance().isRemoveAssertJRelatedElementsFromStackTrace();
    then(removeAssertJRelatedElementsFromStackTrace).isEqualTo(configuration.removeAssertJRelatedElementsFromStackTraceEnabled());
    // check lenient is honored by parsing a string that would fail if the DateFormat was not lenient.
    then(configuration.lenientDateParsingEnabled()).isTrue();
    Date dateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse("2001-02-03T04:05:06");
    then(dateTime).isEqualTo("2001-02-03T04:05:06") // passes whether the lenient flag is enabled or not
                  .isEqualTo("2001-01-34T04:05:06"); // passes only when the lenient flag is enabled
    // check that additional date formats can be used
    Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2001-02-03");
    then(date).isEqualTo("2001_02_03")
              .isEqualTo("2001|02|03");
    then(AssumptionExceptionFactory.getPreferredAssumptionException()).isEqualTo(configuration.preferredAssumptionException());
  }

  @Test
  void should_reset_date_formats() throws Exception {
    // GIVEN
    Configuration configuration = new NonDefaultConfiguration();
    // WHEN
    configuration.apply();
    Configuration.DEFAULT_CONFIGURATION.apply();
    // THEN
    then(Configuration.DEFAULT_CONFIGURATION.additionalDateFormats()).isEmpty();
    Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2001-02-03");
    expectAssertionError(() -> then(date).isEqualTo("2001_02_03"));
  }

  @AfterEach
  public void afterEach() {
    // revert whatever we did in the other tests
    Configuration.DEFAULT_CONFIGURATION.apply();
  }
}
