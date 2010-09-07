/*
 * Created on Oct 7, 2009
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright @2009-2010 the original author or authors.
 */
package org.fest.assertions.util;

import static org.fest.util.Strings.quote;

import java.awt.Dimension;
import java.io.File;
import java.util.*;

import org.fest.util.*;
import org.fest.util.Arrays;
import org.fest.util.Collections;

/**
 * The {@code String} representation of an object.
 *
 * @author Alex Ruiz
 */
public final class ToString {

  /**
   * Returns the {@code String} representation of the given object, or {@code null} if the given object is {@code null}.
   * @param o the given object.
   * @return the {@code String} representation of the given object, or {@code null} if the given object is {@code null}.
   */
  public static String toStringOf(Object o) {
    if (isArray(o)) return Arrays.format(o);
    if (o instanceof Class<?>) return toStringOf((Class<?>)o);
    if (o instanceof File) return toStringOf((File)o);
    if (o instanceof Collection<?>) return Collections.format((Collection<?>)o);
    if (o instanceof Map<?, ?>) return Maps.format((Map<?, ?>)o);
    if (o instanceof Dimension) return toStringOf((Dimension)o);
    if (o instanceof String) return quote((String)o);
    return o == null ? null : o.toString();
  }

  private static boolean isArray(Object o) {
    return o != null && o.getClass().isArray();
  }

  private static String toStringOf(Class<?> c) {
    return c.getName();
  }

  private static String toStringOf(File f) {
    return f.getAbsolutePath();
  }

  private static String toStringOf(Dimension d) {
    return String.format("(%d, %d)", d.width, d.height);
  }

  private ToString() {}
}
