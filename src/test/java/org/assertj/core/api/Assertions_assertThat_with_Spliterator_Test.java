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

import org.assertj.core.test.StringSpliterator;
import org.junit.Test;

import java.util.Spliterator;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Tests for <code>{@link Assertions#assertThat(Spliterator)}</code>.
 *
 * @author William Bakker
 */
public class Assertions_assertThat_with_Spliterator_Test {

  private Spliterator<?> stringSpliterator = new StringSpliterator();

  @Test
  public void should_create_Assert() {
    Object assertions = assertThat(Stream.of("Luke", "Leia").spliterator());
    assertThat(assertions).isNotNull();
  }

  @Test
  public void should_allow_null() {
    assertThat(assertThat((Spliterator<String>) null).actual).isNull();
  }

  @Test
  public void isEqualTo_should_honor_comparing_the_same_mocked_spliterator() {
    Spliterator<?> spliterator = mock(Spliterator.class);
    assertThat(spliterator).isEqualTo(spliterator);
  }

  @Test
  public void spliterator_can_be_asserted_twice() {
    Spliterator<String> names = Stream.of("Luke", "Leia").spliterator();
    assertThat(names).hasCharacteristics(Spliterator.SIZED)
                     .hasCharacteristics(Spliterator.SIZED);
  }

  @Test
  public void should_not_consume_spliterator_when_asserting_non_null() {
    Spliterator<?> spliterator = mock(Spliterator.class);
    assertThat(spliterator).isNotNull();
    verifyZeroInteractions(spliterator);
  }

  @Test
  public void isInstanceOf_should_check_the_original_spliterator_without_consuming_it() {
    Spliterator<?> spliterator = mock(Spliterator.class);
    assertThat(spliterator).isInstanceOf(Spliterator.class);
    verifyZeroInteractions(spliterator);
  }

  @Test
  public void isInstanceOfAny_should_check_the_original_spliterator_without_consuming_it() {
    Spliterator<?> spliterator = mock(Spliterator.class);
    assertThat(spliterator).isInstanceOfAny(Spliterator.class, String.class);
    verifyZeroInteractions(spliterator);
  }

  @Test
  public void isOfAnyClassIn_should_check_the_original_spliterator_without_consuming_it() {
    assertThat(stringSpliterator).isOfAnyClassIn(Double.class, StringSpliterator.class);
  }

  @Test
  public void isExactlyInstanceOf_should_check_the_original_spliterator() {
    assertThat(new StringSpliterator()).isExactlyInstanceOf(StringSpliterator.class);
  }

  @Test
  public void isNotExactlyInstanceOf_should_check_the_original_spliterator() {
    assertThat(stringSpliterator).isNotExactlyInstanceOf(Spliterator.class);
    try {
      assertThat(stringSpliterator).isNotExactlyInstanceOf(StringSpliterator.class);
    } catch (AssertionError e) {
      // ok
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void isNotInstanceOf_should_check_the_original_spliterator() {
    assertThat(stringSpliterator).isNotInstanceOf(Long.class);
  }

  @Test
  public void isNotInstanceOfAny_should_check_the_original_spliterator() {
    assertThat(stringSpliterator).isNotInstanceOfAny(Long.class, String.class);
  }

  @Test
  public void isNotOfAnyClassIn_should_check_the_original_spliterator() {
    assertThat(stringSpliterator).isNotOfAnyClassIn(Long.class, String.class);
  }

  @Test
  public void isSameAs_should_check_the_original_spliterator_without_consuming_it() {
    Spliterator<?> spliterator = mock(Spliterator.class);
    assertThat(spliterator).isSameAs(spliterator);
    verifyZeroInteractions(spliterator);
  }

  @Test
  public void isNotSameAs_should_check_the_original_spliterator_without_consuming_it() {
    Spliterator<?> spliterator = mock(Spliterator.class);
    try {
      assertThat(spliterator).isNotSameAs(spliterator);
    } catch (AssertionError e) {
      verifyZeroInteractions(spliterator);
      return;
    }
    Assertions.fail("Expected assertionError, because assert notSame on same spliterator.");
  }
}
