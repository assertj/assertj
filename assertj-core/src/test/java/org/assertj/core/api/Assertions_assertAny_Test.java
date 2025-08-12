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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertAny;
import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.Throwables.getStackTrace;

import java.util.stream.Stream;

import org.assertj.core.util.Executable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class Assertions_assertAny_Test {
  public static Stream<Arguments> should_throw_IllegalArgumentException_if_one_or_more_of_the_executables_are_null() {
    return Stream.of(Arguments.of((Object) new Executable[] { null, () -> {}, () -> {} }),
                     Arguments.of((Object) new Executable[] { null, null, () -> {} }),
                     Arguments.of((Object) new Executable[] { null, null, null }));
  }

  static Stream<Arguments> should_pass_when_at_least_one_of_the_executables_does_not_fail() {
    Throwable failed = new Throwable("Failed");
    return Stream.of(Arguments.of((Object) new Executable[] { throwingExecutable(failed), () -> {}, () -> {} }),
                     Arguments.of((Object) new Executable[] { throwingExecutable(failed), throwingExecutable(failed), () -> {} }),
                     Arguments.of((Object) new Executable[] { () -> {}, () -> {}, () -> {} }));
  }

  @MethodSource
  @ParameterizedTest
  void should_throw_IllegalArgumentException_if_one_or_more_of_the_executables_are_null(Executable[] executables) {
    // WHEN
    var illegalArgumentException = catchIllegalArgumentException(() -> assertAny(executables));
    // THEN
    then(illegalArgumentException).hasMessage("No executable can be null");
  }

  @Test
  void should_fail_if_one_and_only_executable_provided_fails() {
    // GIVEN
    Throwable throwable = new Throwable("Failed");
    // WHEN
    var assertionError = expectAssertionError(() -> assertAny(throwingExecutable(throwable)));
    // THEN
    then(assertionError).hasMessage("""
        None of the provided executables succeeded.
        Executable #0 failed with:
        %s""".formatted(getStackTrace(throwable)));
  }

  @Test
  void should_fail_if_all_executables_fail() {
    // GIVEN
    Throwable throwable1 = new Throwable("Failure message 1");
    Throwable throwable2 = new Throwable("Failure message 2");
    Throwable throwable3 = new Throwable("Failure message 3");
    // WHEN
    var assertionError = expectAssertionError(() -> assertAny(throwingExecutable(throwable1),
                                                              throwingExecutable(throwable2),
                                                              throwingExecutable(throwable3)));
    // THEN
    then(assertionError).hasMessage("""
        None of the provided executables succeeded.
        Executable #0 failed with:
        %s
        Executable #1 failed with:
        %s
        Executable #2 failed with:
        %s""".formatted(getStackTrace(throwable1),
                        getStackTrace(throwable2),
                        getStackTrace(throwable3)));
  }

  @MethodSource
  @ParameterizedTest
  void should_pass_when_at_least_one_of_the_executables_does_not_fail(Executable[] executables) {
    assertAny(executables);
  }

  private static Executable throwingExecutable(Throwable throwable) {
    return () -> {
      throw throwable;
    };
  }

}
