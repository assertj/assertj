/*
 * Created on Dec 21, 2013
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
 * Copyright @2009-2013 the original author or authors.
 */
package org.assertj.core.presentation;

import java.util.Formatter;

/**
 * Unicode object representation instead of standard java representation.
 *
 * @author Mariusz Smykula
 */
public class UnicodeRepresentation implements Representation {


  /**
   * Returns hexadecimal the {@code toString} representation of the given String or Character.
   *
   * @param object the given object.
   * @return the {@code toString} representation of the given object.
   */
  @Override
  public String toStringOf(Object object) {
    if (object instanceof String) {
      return toStringOf((String) object);
    }
    if (object instanceof Character) {
      return toStringOf((Character) object);
    }

    return object == null ? null : CollectionToString.toStringOf(this, object);
  }

  private String toStringOf(Character string) {
    return escapeUnicode(string.toString());
  }

  private String toStringOf(String string) {
    return escapeUnicode(string);
  }

  private String escapeUnicode(String input) {
    StringBuilder b = new StringBuilder(input.length());
    Formatter f = new Formatter(b);
    for (char c : input.toCharArray()) {
      if (c < 128) {
        b.append(c);
      } else {
        f.format("\\u%04x", (int) c);
      }
    }
    return b.toString();
  }

}
