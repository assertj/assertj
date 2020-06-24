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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.assumptions;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.util.AssertionsUtil.expectAssumptionViolatedException;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("assumeThat after succeedsWithin")
public class Assumptions_assumeThat_with_succeedsWithin_Test {

  private static final Duration ONE_MILLIS = Duration.ofMillis(1);

  @Test
  public void should_run_test_when_assumption_after_succeedsWithin_passes() {
    // GIVEN
    String value = "ook!";
    CompletableFuture<String> future = completedFuture(value);
    // WHEN
    ThrowingCallable code = () -> assumeThat(future).succeedsWithin(1, MILLISECONDS)
                                                    .isEqualTo(value);
    // THEN
    assertThatCode(code).doesNotThrowAnyException();
  }

  @Test
  public void should_ignore_test_when_assumption_after_succeedsWithin_fails() {
    // GIVEN
    String value = "ook!";
    CompletableFuture<String> future = completedFuture(value);
    // WHEN
    expectAssumptionViolatedException(() -> assumeThat(future).succeedsWithin(1, MILLISECONDS)
                                                              .isEqualTo("eeek!"));
  }

  @Test
  public void should_run_test_when_assumption_after_succeedsWithin_asString_passes() {
    // GIVEN
    String value = "ook!";
    CompletableFuture<String> future = completedFuture(value);
    // WHEN
    ThrowingCallable code = () -> assumeThat(future).succeedsWithin(1, MILLISECONDS, as(STRING))
                                                    .startsWith("oo");
    // THEN
    assertThatCode(code).doesNotThrowAnyException();
  }

  @Test
  public void should_ignore_test_when_assumption_after_succeedsWithin_asString_fails() {
    // GIVEN
    String value = "ook!";
    CompletableFuture<String> future = completedFuture(value);
    // WHEN
    expectAssumptionViolatedException(() -> assumeThat(future).succeedsWithin(1, MILLISECONDS, as(STRING))
                                                              .startsWith("eek"));
  }

  @Test
  public void should_run_test_when_assumption_after_succeedsWithin_with_Duration_passes() {
    // GIVEN
    String value = "ook!";
    CompletableFuture<String> future = completedFuture(value);
    // WHEN
    ThrowingCallable code = () -> assumeThat(future).succeedsWithin(ONE_MILLIS)
                                                    .isEqualTo(value);
    // THEN
    assertThatCode(code).doesNotThrowAnyException();
  }

  @Test
  public void should_ignore_test_when_assumption_after_succeedsWithin_with_Duration_fails() {
    // GIVEN
    String value = "ook!";
    CompletableFuture<String> future = completedFuture(value);
    // WHEN
    expectAssumptionViolatedException(() -> assumeThat(future).succeedsWithin(ONE_MILLIS)
                                                              .isEqualTo("eeek!"));
  }

  @Test
  public void should_run_test_when_assumption_after_succeedsWithin_with_Duration_asString_passes() {
    // GIVEN
    String value = "ook!";
    CompletableFuture<String> future = completedFuture(value);
    // WHEN
    ThrowingCallable code = () -> assumeThat(future).succeedsWithin(ONE_MILLIS, as(STRING))
                                                    .startsWith("oo");
    // THEN
    assertThatCode(code).doesNotThrowAnyException();
  }

  @Test
  public void should_ignore_test_when_assumption_after_succeedsWithin_with_Duration_asString_fails() {
    // GIVEN
    String value = "ook!";
    CompletableFuture<String> future = completedFuture(value);
    // WHEN
    expectAssumptionViolatedException(() -> assumeThat(future).succeedsWithin(ONE_MILLIS, as(STRING))
                                                              .startsWith("eek"));
  }
}
