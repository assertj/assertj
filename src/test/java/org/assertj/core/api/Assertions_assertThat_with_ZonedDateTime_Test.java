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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;

import org.junit.Test;

/**
 * Tests for <code>{@link Assertions#assertThat(ZonedDateTime)}</code>.
 * 
 * @author Joel Costigliola
 * @author Marcin Zajączkowski
 */
public class Assertions_assertThat_with_ZonedDateTime_Test {

  @Test
  public void should_create_Assert() {
    AbstractZonedDateTimeAssert<?> assertions = Assertions.assertThat(ZonedDateTime.now());
    assertThat(assertions).isNotNull();
  }

  @Test
  public void should_pass_actual() {
    ZonedDateTime dateTime = ZonedDateTime.now();
    AbstractZonedDateTimeAssert<?>  assertions = Assertions.assertThat(dateTime);
    assertThat(assertions.getActual()).isSameAs(dateTime);
  }
}
