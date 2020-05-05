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
package org.assertj.core.presentation;

import org.junit.jupiter.api.Test;
import org.assertj.core.configuration.Configuration;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests for <code>{@link StandardRepresentation#toStringOf(Throwable)}</code>.
 *
 *  @author XiaoMingZHM Eveneko
 */
public class StandardRepresentation_throwable_format_Test {
  private static final StandardRepresentation STANDARD_REPRESENTATION = new StandardRepresentation();

  // Just to make sure the stack trace is long enough.
  public static class test1 {
    public static class test2 {

      public void exception_layer_2() {
        throw new RuntimeException();
      }
    }

    public void exception_layer_1() {
      test2 t = new test2();
      t.exception_layer_2();
    }
  }

  private String cutStackTraceToFixedLines(String string, int lineNumber) {
    String[] lines = string.split(format("%n"));
    // note that there is one more line for throwable.toString().
    StringBuilder result = new StringBuilder();
    int len = lines.length;
    if (len - 1 <= lineNumber) {
      return string;
    }
    for (int i = 0; i <= lineNumber; ++i) {
      result.append(lines[i] + format("%n"));
    }
    result.append(format("\t...( %d lines folded)%n", len - lineNumber - 1));
    return result.toString();
  }

  private String getStackTrace(Throwable throwable) {
    StringWriter sw = new StringWriter();
    throwable.printStackTrace(new PrintWriter(sw));
    return sw.toString();
  }

  private String ignoreLineNumberInStackTrace(String traces) {
    StringBuilder result = new StringBuilder();
    int len = traces.length();
    int p = 0;
    while (p < len) {
      if (traces.charAt(p) == ':') {
        ++p;
        while (p < len && traces.charAt(p) <= '9' && traces.charAt(p) >= '0') {
          ++p;
        }
      }
      if (p < len) {
        result.append(traces.charAt(p));
      }
      ++p;
    }
    return result.toString();
  }

  @Test
  public void should_return_stacktrace_zero() {
    // define a new configuration to set the maxStackTrace()
    Configuration configuration = new Configuration();
    configuration.setMaxStackTraceLine(0);
    configuration.apply();
    Exception e = null;
    try {
      test1 t = new test1();
      t.exception_layer_1();
      fail("should throw exception");
    } catch (RuntimeException e2) {
      e = e2;
    }
    assert e != null;
    assertThat(STANDARD_REPRESENTATION.toStringOf(e)).isEqualTo("java.lang.RuntimeException");
    assertThat(STANDARD_REPRESENTATION.toStringOf(new Throwable())).isEqualTo("java.lang.Throwable");
  }

  @Test
  public void should_return_stacktrace_less() {
    int maxStackTraceLine = 2;
    // define a new configuration to set the maxStackTrace()
    Configuration configuration = new Configuration();
    configuration.setMaxStackTraceLine(maxStackTraceLine);
    configuration.apply();
    Exception e = null;
    try {
      test1 t = new test1();
      t.exception_layer_1();
      fail("should throw exception");
    } catch (RuntimeException e2) {
      e = e2;
    }
    assert e != null;
    String expected = getStackTrace(e);
    expected = cutStackTraceToFixedLines(expected, maxStackTraceLine);
    assertThat(ignoreLineNumberInStackTrace(STANDARD_REPRESENTATION.toStringOf(e)))
      .isEqualTo(ignoreLineNumberInStackTrace(expected));
  }

  @Test
  public void should_return_stacktrace_equal_or_more() {
    int maxStackTraceLine = 100;
    // define a new configuration to set the maxStackTrace()
    Configuration configuration = new Configuration();
    configuration.setMaxStackTraceLine(maxStackTraceLine);
    configuration.apply();
    Exception e = null;
    try {
      test1 t = new test1();
      t.exception_layer_1();
      fail("should throw exception");
    } catch (RuntimeException e2) {
      e = e2;
    }
    assert e != null;
    String expected = getStackTrace(e);
    expected = cutStackTraceToFixedLines(expected, maxStackTraceLine);
    assertThat(ignoreLineNumberInStackTrace(STANDARD_REPRESENTATION.toStringOf(e)))
      .isEqualTo(ignoreLineNumberInStackTrace(expected));
  }

}
