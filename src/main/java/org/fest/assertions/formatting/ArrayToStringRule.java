/*
 * Created on Sep 10, 2010
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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.formatting;

import static org.fest.util.Arrays.format;

/**
 * Returns the {@code String} representation of an array.
 *
 * @author Alex Ruiz
 */
class ArrayToStringRule implements ToStringRule {

  public boolean canHandle(Object o) {
    if (o == null) return false;
    return o.getClass().isArray();
  }

  public String toStringOf(Object o) {
    if (o.getClass().getComponentType().equals(Class.class)) {
      ClassToStringRule classToStringRule = ClassToStringRule.instance();
      StringBuilder buffer = new StringBuilder();
      buffer.append("[");
      Class<?>[] array = (Class<?>[]) o;
      int size = array.length;
      for (int i = 0; i < size; i++) {
        if (i != 0) buffer.append(", ");
        Class<?> type = array[i];
        String s = (type == null)? "null" : classToStringRule.toStringOf(type);
        buffer.append(s);
      }
      buffer.append("]");
      return buffer.toString();
    }
    return format(o);
  }
}
