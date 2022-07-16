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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.function.Predicate;
import org.assertj.core.api.AbstractStackTraceAssert;
import org.assertj.core.api.StackTraceAssert;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link StackTraceAssert#takeFirstWhile}.
 *
 * @author Ashley Scopes
 */
class StackTraceAssert_takeFirstWhile_Test {

  @Test
  void fails_when_actual_is_null() {
    // Given
    AbstractStackTraceAssert<?, ?> assertion = assertThat((StackTraceElement[]) null);
    Predicate<? super StackTraceElement> predicate = mockPredicate();
    // Then
    assertThatThrownBy(() -> assertion.takeFirstWhile(predicate))
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeNull().create());
    verifyNoInteractions(predicate);
  }

  @Test
  void does_nothing_when_actual_is_empty() {
    // Given
    AbstractStackTraceAssert<?, ?> assertion = assertThat(new StackTraceElement[0]);
    Predicate<? super StackTraceElement> predicate = mockPredicate();
    // Then
    assertThatNoException()
      .isThrownBy(() -> assertion.takeFirstWhile(predicate));
    verifyNoInteractions(predicate);
  }

  @Test
  void fails_when_predicate_is_null() {
    // Given
    AbstractStackTraceAssert<?, ?> assertion = assertThat(someStackTrace());
    // Then
    assertThatThrownBy(() -> assertion.takeFirstWhile(null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("predicate must not be null");
  }

  @Test
  void does_not_call_predicate_once_it_returns_false() {
    // Given
    StackTraceElement[] stackTrace = someStackTrace();
    AbstractStackTraceAssert<?, ?> assertion = assertThat(stackTrace);
    Predicate<? super StackTraceElement> predicate = mockPredicate();
    when(predicate.test(any()))
      .thenReturn(true, true, true, false);

    // When
    assertion.takeFirstWhile(predicate);

    // Then
    verify(predicate, times(4)).test(any());
  }

  @Test
  void does_not_call_predicate_once_all_items_are_exhausted() {
    // Given
    StackTraceElement[] stackTrace = someStackTrace();
    AbstractStackTraceAssert<?, ?> assertion = assertThat(stackTrace);
    Predicate<? super StackTraceElement> predicate = mockPredicate(unused -> true);

    // When
    assertion.takeFirstWhile(predicate);

    // Then
    verify(predicate, times(stackTrace.length)).test(any());
  }

  @Test
  void succeeds_with_valid_predicate_and_produces_expected_result() {
    // Given
    StackTraceElement[] stackTrace = someStackTrace();
    AbstractStackTraceAssert<?, ?> assertion = assertThat(stackTrace);
    Predicate<? super StackTraceElement> predicate = mockPredicate(element -> {
      String className = element.getClassName();
      return className.startsWith("java.") || className.startsWith("jdk.");
    });

    // Then
    assertion
      .takeFirstWhile(predicate)
      .satisfies(
        remaining -> assertThat(remaining)
          .first()
          .hasClassName("jdk.internal.reflect.NativeMethodAccessorImpl")
          .hasMethodName("invoke0"),
        remaining -> assertThat(remaining)
          .last()
          .hasClassName("java.lang.reflect.Method")
          .hasMethodName("invoke")
      );
  }

  @SuppressWarnings("unchecked")
  static Predicate<? super StackTraceElement> mockPredicate() {
    return (Predicate<? super StackTraceElement>) mock(Predicate.class);
  }

  static Predicate<? super StackTraceElement> mockPredicate(Predicate<? super StackTraceElement> predicate) {
    // OpenJDK does not allow spying lambdas directly. We have to make a layer of indirection
    // that does not use a lambda instead. To make this simpler, I just modify the mock rather
    // than creating an anonymous class and a spy.
    Predicate<? super StackTraceElement> mock = mockPredicate();
    when(mock.test(any()))
      .thenAnswer(ctx -> predicate.test(ctx.getArgument(0)));
    return mock;
  }

  static StackTraceElement[] someStackTrace() {
    return new StackTraceElement[]{
      new StackTraceElement("jdk.internal.reflect.NativeMethodAccessorImpl", "invoke0", "NativeMethodAccessorImpl.java", -2),
      new StackTraceElement("jdk.internal.reflect.NativeMethodAccessorImpl", "invoke", "NativeMethodAccessorImpl.java", 77),
      new StackTraceElement("jdk.internal.reflect.DelegatingMethodAccessorImpl", "invoke", "DelegatingMethodAccessorImpl.java", 43),
      new StackTraceElement("java.lang.reflect.Method", "invoke", "Method.java", 568),
      new StackTraceElement("org.junit.platform.commons.util.ReflectionUtils", "invokeMethod", "ReflectionUtils.java", 725),
      new StackTraceElement("org.junit.platform.launcher.core.EngineExecutionOrchestrator", "execute", "EngineExecutionOrchestrator.java", 52),
      new StackTraceElement("org.junit.platform.launcher.core.DefaultLauncher", "execute", "DefaultLauncher.java", 114),
      new StackTraceElement("org.junit.platform.launcher.core.DefaultLauncher", "execute", "DefaultLauncher.java", 86),
      new StackTraceElement("org.junit.platform.launcher.core.DefaultLauncherSession$DelegatingLauncher", "execute", "DefaultLauncherSession.java", 86),
      new StackTraceElement("org.junit.platform.launcher.core.SessionPerRequestLauncher", "execute", "SessionPerRequestLauncher.java", 53),
      new StackTraceElement("com.intellij.junit5.JUnit5IdeaTestRunner", "startRunnerWithArgs", "JUnit5IdeaTestRunner.java", 71),
      new StackTraceElement("com.intellij.rt.junit.IdeaTestRunner$Repeater$1", "execute", "IdeaTestRunner.java", 38),
      new StackTraceElement("com.intellij.rt.execution.junit.TestsRepeater", "repeat", "TestsRepeater.java", 11),
      new StackTraceElement("com.intellij.rt.junit.IdeaTestRunner$Repeater", "startRunnerWithArgs", "IdeaTestRunner.java", 35),
      new StackTraceElement("com.intellij.rt.junit.JUnitStarter", "prepareStreamsAndStart", "JUnitStarter.java", 235),
      new StackTraceElement("com.intellij.rt.junit.JUnitStarter", "main", "JUnitStarter.java", 54),
    };
  }
}
