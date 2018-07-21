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
import static org.assertj.core.error.ShouldBeInSameYear.shouldBeInSameYear;
import static org.assertj.core.util.DateUtil.parse;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link ShouldBeInSameYear#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 * 
 * @author Joel Costigliola
 */
public class ShouldBeInSameYear_create_Test {

  @Test
  public void should_create_error_message() {
    ErrorMessageFactory factory = shouldBeInSameYear(parse("2010-01-01"), parse("2011-01-01"));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting:%n" +
                                         " <2010-01-01T00:00:00.000>%n" +
                                         "to be on same year as:%n" +
                                         " <2011-01-01T00:00:00.000>"));
  }

}
