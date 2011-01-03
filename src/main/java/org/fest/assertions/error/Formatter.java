/*
 * Created on Sep 8, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.error;

import static org.fest.util.Strings.isEmpty;
import static org.fest.util.ToString.toStringOf;

import org.fest.assertions.core.Condition;
import org.fest.assertions.description.Description;
import org.fest.util.ToString;

/**
 * General-purpose formatter.
 *
 * @author Alex Ruiz
 */
public class Formatter {

  private static final Formatter INSTANCE = new Formatter();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static Formatter instance() {
    return INSTANCE;
  }

  private Formatter() {}

  /**
   * Interprets a printf-style format {@code String} for failed assertion messages. It is similar to
   * <code>{@link String#format(String, Object...)}</code>, except for:
   * <ol>
   * <li>the value of the given <code>{@link Description}</code> is used as the first argument referenced in the format
   * string</li>
   * <li>each of the arguments in the given array is converted to a {@code String} by invoking
   * <code>{@link ToString#toStringOf(Object)}</code>.
   * </ol>
   * @param format the format string.
   * @param d the description of the failed assertion.
   * @param args arguments referenced by the format specifiers in the format string.
   * @throws NullPointerException If the format string is {@code null}.
   * @return A formatted {@code String}.
   */
  public String formatMessage(String format, Description d, Object... args) {
    // TODO test
    return String.format(format, format(d, args));
  }

  private Object[] format(Description d, Object[] args) {
    int argCount = args.length;
    String[] formatted = new String[argCount + 1];
    formatted[0] = format(d);
    for (int i = 0; i < argCount; i++)
      formatted[i + 1] = describe(args[i]);
    return formatted;
  }

  private String describe(Object o) {
    if (o instanceof Condition) return describe(((Condition<?>)o).description());
    if (o instanceof Description) return ((Description)o).value();
    return toStringOf(o);
  }

  /**
   * Formats the given <code>{@link Description}</code> by surrounding its text value with square brackets and adding a
   * space at the end.
   * @param d the description to format. It can be {@code null}.
   * @return the formatted description, or an empty {@code String} if the the {@code Description} is {@code null}.
   */
  public String format(Description d) {
    String s = (d != null) ? d.value() : null;
    if (isEmpty(s)) return "";
    return String.format("[%s] ", s);
  }
}
