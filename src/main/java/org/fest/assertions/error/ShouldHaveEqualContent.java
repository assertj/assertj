/*
 * Created on Jan 28, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.error;

import static org.fest.util.Systems.LINE_SEPARATOR;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.fest.assertions.description.Description;

/**
 * Creates an error message indicating that an assertion that verifies that two files/inputStreams have equal content failed.
 * 
 * @author Yvonne Wang
 * @author Matthieu Baechler
 */
public class ShouldHaveEqualContent extends BasicErrorMessageFactory {

  private String diffs;

  /**
   * Creates a new <code>{@link ShouldHaveEqualContent}</code>.
   * @param actual the actual file in the failed assertion.
   * @param expected the expected file in the failed assertion.
   * @param diffs the differences between {@code actual} and {@code expected}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveEqualContent(File actual, File expected, List<String> diffs) {
    return new ShouldHaveEqualContent(actual, expected, diffsAsString(diffs));
  }

  /**
   * Creates a new <code>{@link ShouldHaveEqualContent}</code>.
   * @param actual the actual InputStream in the failed assertion.
   * @param expected the expected Stream in the failed assertion.
   * @param diffs the differences between {@code actual} and {@code expected}.
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveEqualContent(InputStream actual, InputStream expected, List<String> diffs) {
    return new ShouldHaveEqualContent(actual, expected, diffsAsString(diffs));
  }

  @Override
  public String create(Description d) {
    // we append diffs here as we can't add in super constructor call, see why below.
    //
    // case 1 - append diffs to String passed in super :
    // super("file:<%s> and file:<%s> do not have equal content:" + diffs, actual, expected);
    // this leads to a MissingFormatArgumentException if diffs contains a format specifier (like %s) because the String will
    // finally be evaluated with String.format
    //
    // case 2 - add as format arg to the String passed in super :
    // super("file:<%s> and file:<%s> do not have equal content:"actual, expected, diffs);
    // this is better than case 1 but the diffs String will be quoted before the class to String.format as all String in Fest
    // error message. This is not what we want
    //
    // The solution is to keep diffs as an attribute and append it after String.format has been applied on the error message.
    return super.create(d) + diffs;
  }

  private static String diffsAsString(List<String> diffs) {
    StringBuilder b = new StringBuilder();
    for (String diff : diffs)
      b.append(LINE_SEPARATOR).append(diff);
    return b.toString();
  }

  private ShouldHaveEqualContent(File actual, File expected, String diffs) {
    super("file:<%s> and file:<%s> do not have equal content:", actual, expected);
    this.diffs = diffs;
  }

  private ShouldHaveEqualContent(InputStream actual, InputStream expected, String diffs) {
    super("InputStreams do not have equal content:", actual, expected);
    this.diffs = diffs;
  }
}
