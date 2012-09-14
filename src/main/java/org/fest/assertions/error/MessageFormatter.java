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
 * Copyright @2010-2012 the original author or authors.
 */
package org.fest.assertions.error;

import static org.fest.util.Preconditions.checkNotNull;
import static org.fest.util.ToString.toStringOf;

import org.fest.assertions.description.Description;
import org.fest.assertions.internal.*;
import org.fest.util.*;

/**
 * Formats the messages to be included in assertion errors.
 * 
 * @author Alex Ruiz
 */
public class MessageFormatter {
  private static final MessageFormatter INSTANCE = new MessageFormatter();

  public static MessageFormatter instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  DescriptionFormatter descriptionFormatter = DescriptionFormatter.instance();

  @VisibleForTesting
  MessageFormatter() {}

  /**
   * Interprets a printf-style format {@code String} for failed assertion messages. It is similar to
   * <code>{@link String#format(String, Object...)}</code>, except for:
   * <ol>
   * <li>the value of the given <code>{@link Description}</code> is used as the first argument referenced in the format
   * string</li>
   * <li>each of the arguments in the given array is converted to a {@code String} by invoking
   * <code>{@link ToString#toStringOf(Object)}</code>.
   * </ol>
   * 
   * @param d the description of the failed assertion, may be {@code null}.
   * @param format the format string.
   * @param args arguments referenced by the format specifiers in the format string.
   * @throws NullPointerException if the format string is {@code null}.
   * @return A formatted {@code String}.
   */
  public String format(Description d, String format, Object... args) {
    checkNotNull(format);
    checkNotNull(args);
    return descriptionFormatter.format(d) + String.format(format, format(args));
  }

  private Object[] format(Object[] args) {
    int argCount = args.length;
    String[] formatted = new String[argCount];
    for (int i = 0; i < argCount; i++) {
      formatted[i] = asText(args[i]);
    }
    return formatted;
  }

  private String asText(Object o) {
    if (o instanceof ComparatorBasedComparisonStrategy) {
      return " according to " + o + " comparator";
    }
    if (o instanceof StandardComparisonStrategy) {
      return "";
    }
    return toStringOf(o);
  }
}
