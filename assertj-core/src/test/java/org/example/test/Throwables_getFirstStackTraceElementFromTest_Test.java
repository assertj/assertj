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
package org.example.test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Throwables.getFirstStackTraceElementFromTest;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class Throwables_getFirstStackTraceElementFromTest_Test {

  @Test
  void should_return_first_stack_trace_element_from_test() {
    // GIVEN
    StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
    // WHEN
    StackTraceElement firstStackTraceElementFromTest = getFirstStackTraceElementFromTest(stackTraceElements);
    // THEN
    then(firstStackTraceElementFromTest).hasToString("org.example.test.Throwables_getFirstStackTraceElementFromTest_Test.should_return_first_stack_trace_element_from_test(Throwables_getFirstStackTraceElementFromTest_Test.java:34)");
  }

  @ParameterizedTest
  @MethodSource
  void should_ignore_test_frameworks_ides_build_tool_and_jdk_stack_traces(String[] fullQualifiedClassNames,
                                                                          String expectedStackTraceElement) {
    // GIVEN
    StackTraceElement[] stackTraceElements = buildStackTraceFrom(fullQualifiedClassNames);
    // WHEN
    StackTraceElement firstStackTraceElementFromTest = getFirstStackTraceElementFromTest(stackTraceElements);
    // THEN
    then(firstStackTraceElementFromTest).hasToString(expectedStackTraceElement);
  }

  private StackTraceElement[] buildStackTraceFrom(String[] fullQualifiedClassNames) {
    StackTraceElement[] stackTraceElements = new StackTraceElement[fullQualifiedClassNames.length];
    for (int i = 0; i < fullQualifiedClassNames.length; i++) {
      stackTraceElements[i] = new StackTraceElement(fullQualifiedClassNames[i], "foo", "Foo.java", i + 1);
    }
    return stackTraceElements;
  }

  private static Stream<Arguments> should_ignore_test_frameworks_ides_build_tool_and_jdk_stack_traces() {
    return Stream.of(arguments(array("com.foo.Foo", "sun.reflect", "org.assertj.core.api"), "com.foo.Foo.foo(Foo.java:1)"),
                     arguments(array("sun.reflect", "com.foo.Foo", "org.assertj.core.api"), "com.foo.Foo.foo(Foo.java:2)"),
                     arguments(array("sun.reflect", "org.assertj.core.api", "com.foo.Foo"), "com.foo.Foo.foo(Foo.java:3)"),
                     arguments(array("jdk.internal.reflect",
                                     "java.",
                                     "javax.",
                                     "org.junit.",
                                     "org.eclipse.jdt.internal.junit.",
                                     "org.eclipse.jdt.internal.junit4.",
                                     "org.eclipse.jdt.internal.junit5.",
                                     "com.intellij.junit5.",
                                     "com.intellij.rt.execution.junit.",
                                     "com.intellij.rt.junit.",
                                     "org.apache.maven.surefire",
                                     "org.pitest.",
                                     "org.assertj",
                                     "com.foo.Foo"),
                               "com.foo.Foo.foo(Foo.java:14)"));
  }
}
