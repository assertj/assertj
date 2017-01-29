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

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveSameHourAs.shouldHaveSameHourAs;

/**
 * Tests for <code>{@link org.assertj.core.error.ShouldHaveSameHourAs}</code>
 *
 * @author Alexander Bischof
 */
public class ShouldHaveSameHourAs_create_Test {

    private ErrorMessageFactory factory;

    @Test
    public void should_create_error_message_localtime() {
        factory = shouldHaveSameHourAs(LocalTime.of(12, 0), LocalTime.of(13, 0));
        assertThat(factory.create(new TextDescription("Test"), new StandardRepresentation()))
            .isEqualTo(format("[Test] %nExpecting:%n  <12:00>%nto have same hour as:%n  <13:00>%nbut had not."));
    }

    @Test
    public void should_create_error_message_offset() {
        factory = shouldHaveSameHourAs(OffsetTime.of(12, 0, 0, 0, ZoneOffset.UTC),
                                       OffsetTime.of(13, 0, 0, 0, ZoneOffset.UTC));
        assertThat(factory.create(new TextDescription("Test"), new StandardRepresentation()))
            .isEqualTo(format("[Test] %nExpecting:%n  <12:00Z>%nto have same hour as:%n  <13:00Z>%nbut had not."));
    }
}
