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
package org.assertj.core.api;

import static org.assertj.core.test.TypeCanonizer.canonize;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * @author Filip Hrisafov
 */
public abstract class BaseAssertionsTest {

  private static final int ACCESS_MODIFIERS = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;

  static final Comparator<Method> IGNORING_DECLARING_CLASS_AND_METHOD_NAME = internalMethodComparator(false, true);

  static final Comparator<Method> IGNORING_DECLARING_CLASS_AND_RETURN_TYPE = internalMethodComparator(true, false);

  static final Comparator<Method> IGNORING_DECLARING_CLASS_ONLY = internalMethodComparator(false, false);

  static final Comparator<Method> IGNORING_DECLARING_CLASS_RETURN_TYPE_AND_METHOD_NAME = internalMethodComparator(true,
                                                                                                                  true);
  // Object is ignored because of the AssertProvider
  static final Class<?>[] SPECIAL_IGNORED_RETURN_TYPES = array(AssertDelegateTarget.class,
                                                               FactoryBasedNavigableListAssert.class,
                                                               FactoryBasedNavigableIterableAssert.class,
                                                               ClassBasedNavigableListAssert.class,
                                                               ClassBasedNavigableIterableAssert.class,
                                                               Object.class);

  static Method[] findMethodsWithName(Class<?> clazz, String name, Class<?>... ignoredReturnTypes) {
    List<Method> matchingMethods = new ArrayList<>();
    Set<Class<?>> ignoredReturnTypesSet = newLinkedHashSet(ignoredReturnTypes);
    for (Method method : clazz.getMethods()) {
      if (!ignoredReturnTypesSet.contains(method.getReturnType()) && method.getName().equals(name)) {
        matchingMethods.add(method);
      }
    }
    return matchingMethods.toArray(new Method[matchingMethods.size()]);
  }

  private static Comparator<Method> internalMethodComparator(final boolean ignoreReturnType,
                                                             final boolean ignoreMethodName) {
    return new Comparator<Method>() {
      @Override
      public int compare(Method method1, Method method2) {

        // the methods should be with the same access type
        // static vs not static is not important Soft vs Not Soft assertions
        boolean equal = (ACCESS_MODIFIERS & method1.getModifiers() & method2.getModifiers()) != 0;
        equal = equal && (ignoreReturnType || sameGenericReturnType(method1, method2));
        equal = equal && (ignoreMethodName || sameMethodName(method1, method2));

        equal = equal && sameGenericParameterTypes(method1, method2);
        return equal ? 0 : 1;
      }
    };
  }

  /**
   * Checks if the methods have same generic parameter types.
   *
   * @param method1 the first method
   * @param method2 the second method
   * @return {@code true} if the methods have same generic parameters, {@code false} otherwise
   */
  private static boolean sameGenericParameterTypes(Method method1, Method method2) {
    Type[] pTypes1 = method1.getGenericParameterTypes();
    Type[] pTypes2 = method2.getGenericParameterTypes();
    if (pTypes1.length != pTypes2.length) {
      return false;
    }

    for (int i = 0; i < pTypes1.length; i++) {
      if (!sameType(pTypes1[i], pTypes2[i])) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if the methods have the same name.
   *
   * @param method1 the first method
   * @param method2 the second method
   * @return {@code true} if the methods have the same name, {@code false} otherwise
   */
  private static boolean sameMethodName(Method method1, Method method2) {
    return method1.getName().equals(method2.getName());
  }

  /**
   * Checks if the methods have same generic return type.
   *
   * @param method1 the first method
   * @param method2 the second method
   * @return {@code true} if the methods have same generic return type, {@code false} otherwise.
   */
  private static boolean sameGenericReturnType(Method method1, Method method2) {
    return sameType(method1.getGenericReturnType(), method2.getGenericReturnType());
  }

  /**
   * Checks if the types are equal.
   *
   * @param type1 the first type
   * @param type2 the second type
   * @return {@code true} if the types are equal, {@code false} otherwise
   */
  private static boolean sameType(Type type1, Type type2) {
    return canonize(type1).equals(canonize(type2));
  }
}
