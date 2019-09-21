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
package org.assertj.core.description;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.util.Objects.HASH_CODE_PRIME;
import static org.assertj.core.util.Objects.areEqual;
import static org.assertj.core.util.Objects.hashCodeFor;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

import org.assertj.core.util.Preconditions;
import org.assertj.core.util.VisibleForTesting;

/**
 * The {@code Description} combining multiple {@code Description}s. It'll honor the nested descriptions and will indent
 * them as appropriate.
 *
 * @author Edgar Asatryan
 * @author Joel Costigliola
 */
public class JoinDescription extends Description {
  private static final int DEFAULT_INDENTATION = 2;
  private static final String LINE_SEPARATOR = System.lineSeparator();
  /**
   * Delimiter string between {@code descriptions}.
   */
  private static final String DELIMITER = ',' + LINE_SEPARATOR;

  @VisibleForTesting
  final Collection<Description> descriptions;
  @VisibleForTesting
  final String prefix;
  @VisibleForTesting
  final String suffix;

  /**
   * Creates a new <code>{@link JoinDescription}</code>.
   *
   * @param prefix       The beginning part of this description.
   * @param suffix       The ending part of this description.
   * @param descriptions The descriptions to combine.
   *
   * @throws NullPointerException if the given prefix is {@code null}.
   * @throws NullPointerException if the given suffix is {@code null}.
   * @throws NullPointerException if the given descriptions contains {@code null} elements or descriptions itself is
   *                              {@code null}.
   */
  public JoinDescription(String prefix, String suffix, Collection<Description> descriptions) {
    this.prefix = Objects.requireNonNull(prefix);
    this.suffix = Objects.requireNonNull(suffix);
    this.descriptions = Objects.requireNonNull(descriptions).stream().map(JoinDescription::checkNotNull).collect(toList());
  }

  private static Description checkNotNull(Description description) {
    Preconditions.checkNotNull(description, "The descriptions should not contain null elements");
    return description;
  }

  @Override
  public String value() {
    return value(new IndentedAppendable(new StringBuilder(), DEFAULT_INDENTATION)).toString();
  }

  @Override
  public int hashCode() {
    return HASH_CODE_PRIME + hashCodeFor(prefix) + hashCodeFor(suffix) + hashCodeFor(descriptions);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof JoinDescription)) return false;
    JoinDescription that = (JoinDescription) o;
    return areEqual(descriptions, that.descriptions) &&
      areEqual(prefix, that.prefix) &&
      areEqual(suffix, that.suffix);
  }

  private IndentedAppendable value(IndentedAppendable buff) {
    // shallow indention for prefix
    buff.indentDelta(-DEFAULT_INDENTATION)
      .indent()
      .append(prefix);

    if (descriptions.isEmpty()) {
      return buff.append(suffix); // no line sep
    }

    buff.append(LINE_SEPARATOR).indentDelta(DEFAULT_INDENTATION);

    Iterator<? extends Description> it = descriptions.iterator();

    while (it.hasNext()) {
      Description desc = it.next();

      if (desc instanceof JoinDescription) {
        JoinDescription joinDesc = (JoinDescription) desc;

        // increase indention to write nested conditions more deeply
        joinDesc.value(buff.indentDelta(DEFAULT_INDENTATION));
      } else {
        buff.indent().append(desc.value());
      }

      // avoid the trailing delimiter
      if (it.hasNext()) {
        buff.append(DELIMITER);
      }
    }

    return buff.append(LINE_SEPARATOR)
      .indentDelta(-DEFAULT_INDENTATION) // shallow indention to align with prefix
      .indent()
      .append(suffix);
  }

  /**
   * The wrapper for {@code StringBuilder} aware of indentation.
   */
  private static class IndentedAppendable implements Appendable {
    private final StringBuilder stringBuilder;
    private int currentIndentation;

    IndentedAppendable(StringBuilder stringBuilder, int indentation) {
      this.stringBuilder = stringBuilder;
      this.currentIndentation = indentation;
    }

    @Override
    public IndentedAppendable append(CharSequence charSequence) {
      stringBuilder.append(charSequence);
      return this;
    }

    @Override
    public IndentedAppendable append(CharSequence charSequence, int start, int end) {
      stringBuilder.append(charSequence, start, end);
      return this;
    }

    @Override
    public IndentedAppendable append(char c) {
      stringBuilder.append(c);
      return this;
    }

    /**
     * Adjusts the indentation size by {@code delta}.
     *
     * @param delta The indentation adjustment.
     *
     * @return a this instance.
     */
    IndentedAppendable indentDelta(int delta) {
      this.currentIndentation += delta;
      return this;
    }

    /**
     * Appends the indentation according to current size.
     *
     * @return a this instance.
     */
    IndentedAppendable indent() {
      for (int i = 0; i < currentIndentation; i++) {
        stringBuilder.append(' ');
      }

      return this;
    }

    public String toString() {
      return stringBuilder.toString();
    }
  }
}
