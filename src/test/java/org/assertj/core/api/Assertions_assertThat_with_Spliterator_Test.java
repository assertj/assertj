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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Spliterator;
import java.util.stream.Stream;

import org.assertj.core.test.StringSpliterator;
import org.junit.Test;

/**
 * Tests for <code>{@link Assertions#assertThat(Spliterator)}</code>.
 *
 * @author William Bakker
 */
public class Assertions_assertThat_with_Spliterator_Test {

  private Spliterator<?> stringSpliterator = new StringSpliterator();

  @Test
  public void should_create_Assert() {
    SpliteratorAssert<String> assertions = assertThat(Stream.of("Luke", "Leia").spliterator());
    assertThat(assertions).isNotNull();
  }

  @Test
  public void should_pass_actual() {
    SpliteratorAssert<?> assertion = assertThat(stringSpliterator);
    assertThat(assertion.actual).isSameAs(stringSpliterator);
  }

}
