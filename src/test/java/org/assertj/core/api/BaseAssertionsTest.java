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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Filip Hrisafov
 */
public class BaseAssertionsTest {

  private static final int ACCESS_MODIFIERS = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;

  protected static List<Method> findMethodsWithName(Class<?> clazz, String name) {
    List<Method> matchingMethods = new ArrayList<>();
    for (Method method : clazz.getDeclaredMethods()) {
      if (method.getName().equals(name)) {
        matchingMethods.add(method);
      }
    }
    return matchingMethods;
  }

  protected static Comparator<Method> ignoringDeclaringClassAndMethodName() {
    return new Comparator<Method>() {
      @Override
      public int compare(Method o1, Method o2) {

        // the methods should be with the same access type
        // static vs not static is not important Soft vs Not Soft assertions
        boolean equal = (ACCESS_MODIFIERS & o1.getModifiers() & o2.getModifiers()) != 0;
        equal = equal && o1.getReturnType().equals(o2.getReturnType());

        Class<?>[] pTypes1 = o1.getParameterTypes();
        Class<?>[] pTypes2 = o2.getParameterTypes();
        equal = equal && pTypes1.length == pTypes2.length;
        if (equal) {
          for (int i = 0; i < pTypes1.length; i++) {
            equal = equal && pTypes1[i].equals(pTypes2[i]);
          }
        }
        return equal ? 0 : 1;
      }
    };
  }
}
