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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.error;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.util.Strings.formatIfArgs;

import org.assertj.core.description.Description;
import org.assertj.core.internal.AbstractComparisonStrategy;
import org.assertj.core.presentation.Representation;
import org.assertj.core.util.VisibleForTesting;

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
   * <code>{@link org.assertj.core.presentation.Representation#toStringOf(Object)}</code>.
   * </ol>
   * 
   * @param d the description of the failed assertion, may be {@code null}.
   * @param p the Representation used
   * @param format the format string.
   * @param args arguments referenced by the format specifiers in the format string.
   * @return A formatted {@code String}.
   * @throws NullPointerException if the format string is {@code null}.
   */
  public String format(Description d, Representation p, String format, Object... args) {
    requireNonNull(format);
    requireNonNull(args);
    return descriptionFormatter.format(d) + formatIfArgs(format, format(p, args));
  }

  private Object[] format(Representation p, Object[] args) {
    int argCount = args.length;
    String[] formatted = new String[argCount];
    for (int i = 0; i < argCount; i++) {
      formatted[i] = asText(p, args[i]);
    }
    return formatted;
  }

  private String asText(Representation p, Object o) {
    if (o instanceof AbstractComparisonStrategy) {
      return ((AbstractComparisonStrategy) o).asText();
    }
    return p.toStringOf(o);
  }
}
