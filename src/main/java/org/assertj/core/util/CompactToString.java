/*
 * Created on Nov 30, 2013
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
package org.assertj.core.util;

import java.util.Collection;

import static org.assertj.core.util.Strings.quote;

/**
 * Obtains the compact version of {@code toString} representation of an object.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Yvonne Wang
 * @author Mariusz Smykula
 */
public final class CompactToString {

  final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

  /**
   * Returns the {@code toString} representation of the given object. It may or not the object's own implementation of
   * {@code toString}.
   *
   * @param o the given object.
   * @return the {@code toString} representation of the given object.
   */
  public static String toStringOf(Object o) {
    if (o instanceof Class<?>) {
      return toStringOf((Class<?>) o);
    }
    if (o instanceof Collection<?>) {
      return toStringOf((Collection<?>) o);
    }
    if (o instanceof String) {
      return quote((String) o);
    }
    if (o instanceof Byte) {
      return toStringOf((Byte) o);
    }
    return o == null ? null : o.toString();
  }

  private static String toStringOf(Byte b) {
    int v = b & 0xFF;
    return new String(new char[]{hexArray[v >>> 4], hexArray[v & 0x0F]});
  }

  private static String toStringOf(Class<?> c) {
    return c.getCanonicalName();
  }

  private static String toStringOf(Collection<?> c) {
    return Collections.format(c);
  }

  private CompactToString() {
  }
}
