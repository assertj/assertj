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
package org.assertj.core.description;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.util.Objects.HASH_CODE_PRIME;
import static org.assertj.core.util.Objects.areEqual;
import static org.assertj.core.util.Objects.hashCodeFor;

import java.util.Collection;
import java.util.Iterator;

import org.assertj.core.util.VisibleForTesting;

/**
 * The {@code Description} combining multiple {@code Description}s. It'll honor the nested descriptions and will indent
 * them as appropriate.
 *
 * @author Edgar Asatryan
 * @author Joel Costigliola
 */
public class JoinDescription extends Description {
  private static final int DEFAULT_INDENTATION = 3;
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
    this.prefix = requireNonNull(prefix);
    this.suffix = requireNonNull(suffix);
    this.descriptions = requireNonNull(descriptions).stream()
                                                    .map(JoinDescription::checkNotNull)
                                                    .collect(toList());
  }

  private static Description checkNotNull(Description description) {
    requireNonNull(description, "The descriptions should not contain null elements");
    return description;
  }

  @Override
  public String value() {
    IndentedAppendable indentableBuilder = new IndentedAppendable(new StringBuilder());
    return appendIndentedValueTo(indentableBuilder).toString();
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

  private IndentedAppendable appendIndentedValueTo(IndentedAppendable indentableBuilder) {
    // indent and begin adding the current description starting with prefix
    indentableBuilder.indent().append(prefix);

    // special case where there is no descriptions
    if (descriptions.isEmpty()) return indentableBuilder.append(suffix); // no line sep

    // descriptions section
    indentableBuilder.append(LINE_SEPARATOR);
    // increase indention to write nested conditions
    indentableBuilder.changeIndentationBy(DEFAULT_INDENTATION);
    Iterator<? extends Description> it = descriptions.iterator();
    while (it.hasNext()) {
      Description description = it.next();
      if (description instanceof JoinDescription) {
        JoinDescription joinDescription = (JoinDescription) description;
        joinDescription.appendIndentedValueTo(indentableBuilder);
      } else {
        // we indent according to the current indentation and then we append the value
        indentableBuilder.indent().append(description.value());
      }
      // avoid the trailing delimiter
      if (it.hasNext()) indentableBuilder.append(DELIMITER);
    }

    // suffix section
    return indentableBuilder.append(LINE_SEPARATOR)
                            .indentBy(-DEFAULT_INDENTATION) // back indention to align with prefix
                            .append(suffix);
  }

  /**
   * The wrapper for {@code StringBuilder} aware of indentation.
   */
  private static class IndentedAppendable implements Appendable {
    private final StringBuilder stringBuilder;
    private int currentIndentation;

    IndentedAppendable(StringBuilder stringBuilder) {
      this.stringBuilder = stringBuilder;
      this.currentIndentation = 0;
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
     * Adjusts the indentation size by {@code indentation}.
     *
     * @param indentation The indentation adjustment.
     *
     * @return a this instance.
     */
    IndentedAppendable changeIndentationBy(int indentation) {
      this.currentIndentation += indentation;
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

    /**
     * Shortcut method from {@link #changeIndentationBy(int)} and {@link #indent()}
     *
     * @param indentation The indentation adjustment.
     *
     * @return a this instance.
     */
    IndentedAppendable indentBy(int indentation) {
      return changeIndentationBy(indentation).indent();
    }

    @Override
    public String toString() {
      return stringBuilder.toString();
    }
  }
}
