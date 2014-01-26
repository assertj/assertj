/*
 * Created on Oct 7, 2009
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
 * Copyright @2009-2012 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.util.Hexadecimals.byteToHexString;

/**
 * Obtains the {@code toString} representation of an object - richer version.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Yvonne Wang
 * @author Mariusz Smykula
 */
public final class ToString {

  /**
   * Returns the {@code toString} representation of the given object. It may or not the object's own implementation of
   * {@code toString}.
   *
   * @param o the given object.
   * @return the {@code toString} representation of the given object.
   */
  public static String toStringOf(Object o) {

    if (o instanceof Byte) {
      return toStringOf((Byte) o);
    }

    return o == null ? null : SimpleToString.toStringOf(o);
  }

  private static String toStringOf(Byte b) {
    return "0x" + byteToHexString(b);
  }

  private ToString() {}
}
