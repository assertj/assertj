/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;
import static org.assertj.core.description.EmptyTextDescription.emptyDescription;
import static org.assertj.core.util.Objects.HASH_CODE_PRIME;
import static org.assertj.core.util.Objects.areEqual;
import static org.assertj.core.util.Objects.hashCodeFor;
import static org.assertj.core.util.Preconditions.checkNotNull;
import static org.assertj.core.util.Strings.quote;

import java.util.Arrays;

import org.assertj.core.description.Description;
import org.assertj.core.presentation.Representation;
import org.assertj.core.util.VisibleForTesting;

/**
 * A factory of error messages typically shown when an assertion fails.
 * 
 * @author Alex Ruiz
 */
public class BasicErrorMessageFactory implements ErrorMessageFactory {

  protected final String format;
  protected final Object[] arguments;

  @VisibleForTesting
  MessageFormatter formatter = MessageFormatter.instance();

  /**
   * To avoid quoted String in message format.
   */
  private static class UnquotedString implements CharSequence {

    private final String string;

    private UnquotedString(String string) {
      this.string = checkNotNull(string, "string is mandatory");
    }

    @Override
    public int length() {
      return string.length();
    }

    @Override
    public char charAt(int index) {
      return string.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
      return string.subSequence(start, end);
    }

    @Override
    public String toString() {
      return string;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((string == null) ? 0 : string.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null) return false;
      if (getClass() != obj.getClass()) return false;
      UnquotedString other = (UnquotedString) obj;
      if (string == null) {
        if (other.string != null) return false;
      } else if (!string.equals(other.string)) return false;
      return true;
    }
  }

  /**
   * Creates a new <code>{@link BasicErrorMessageFactory}</code>.
   * 
   * @param format the format string.
   * @param arguments arguments referenced by the format specifiers in the format string.
   */
  public BasicErrorMessageFactory(String format, Object... arguments) {
    this.format = format;
    this.arguments = arguments;
  }

  /** {@inheritDoc} */
  @Override
  public String create(Description d, Representation representation) {
    return formatter.format(d, representation, format, arguments);
  }

  /** {@inheritDoc} */
  @Override
  public String create(Description d) {
    return formatter.format(d, CONFIGURATION_PROVIDER.representation(), format, arguments);
  }

  /** {@inheritDoc} */
  @Override
  public String create() {
    return formatter.format(emptyDescription(), CONFIGURATION_PROVIDER.representation(), format, arguments);
  }

  /**
   * Return a string who will be unquoted in message format (without '')
   * 
   * @param string the string who will be unquoted.
   * @return an unquoted string in message format.
   */
  protected static CharSequence unquotedString(String string) {
    return new UnquotedString(string);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BasicErrorMessageFactory other = (BasicErrorMessageFactory) obj;
    if (!areEqual(format, other.format))
      return false;
    // because it does not manage array recursively, don't use : Arrays.equals(arguments, other.arguments);
    // example if arguments[1] and other.arguments[1] are logically same arrays but not same object, it will return
    // false
    return areEqual(arguments, other.arguments);
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + hashCodeFor(format);
    result = HASH_CODE_PRIME * result + Arrays.hashCode(arguments);
    return result;
  }

  @Override
  public String toString() {
    return format("%s[format=%s, arguments=%s]", getClass().getSimpleName(), quote(format),
                  CONFIGURATION_PROVIDER.representation().toStringOf(arguments));
  }

}
