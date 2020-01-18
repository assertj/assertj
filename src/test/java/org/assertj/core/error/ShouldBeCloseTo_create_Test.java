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
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeCloseTo.shouldBeCloseTo;
import static org.assertj.core.util.DateUtil.parseDatetimeWithMs;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldBeCloseTo#create(Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Joel Costigliola
 */
public class ShouldBeCloseTo_create_Test {

  @Test
  public void should_create_error_message_with_period_boundaries_included() {
    // GIVEN
    ErrorMessageFactory factory = shouldBeCloseTo(parseDatetimeWithMs("2011-01-01T00:00:00.000"),
                                                  parseDatetimeWithMs("2011-01-01T00:00:00.101"),
                                                  100, 101);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting:%n <2011-01-01T00:00:00.000>%nto be close to:%n <2011-01-01T00:00:00.101>%nby less than 100ms but difference was 101ms"));
  }

}
