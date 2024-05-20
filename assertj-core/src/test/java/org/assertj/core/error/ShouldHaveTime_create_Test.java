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
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveTime.shouldHaveTime;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import java.util.Date;
import org.assertj.core.description.TextDescription;
import org.assertj.core.util.DateUtil;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link ShouldHaveTime#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 *
 * @author Guillaume Girou
 * @author Nicolas François
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
class ShouldHaveTime_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    Date date = DateUtil.parseDatetime("2011-01-01T05:01:00");
    // WHEN
    String message = shouldHaveTime(date, 123).create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting%n" +
                                   "  2011-01-01T05:01:00.000 (java.util.Date)%n" +
                                   "to have time:%n" +
                                   "  123L%n" +
                                   "but was:%n" +
                                   "  " + date.getTime() + "L"));
  }
}
