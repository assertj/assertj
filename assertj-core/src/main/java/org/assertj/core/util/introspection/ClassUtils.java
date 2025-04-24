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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.util.introspection;

import static org.assertj.core.util.Lists.list;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

public class ClassUtils {

  /**
   * Lists primitive wrapper {@link Class}es.
   */
  private static final List<Class<?>> PRIMITIVE_WRAPPER_TYPES = list(Boolean.class, Byte.class, Character.class, Short.class,
                                                                     Integer.class, Long.class, Double.class, Float.class,
                                                                     Void.class);
  private static final List<Class<?>> OPTIONAL_TYPES = list(Optional.class, OptionalLong.class, OptionalDouble.class,
                                                            OptionalInt.class);

  /**
   * <p>Gets a {@code List} of superclasses for the given class.</p>
   *
   * @param cls the class to look up, may be {@code null}
   * @return the {@code List} of superclasses in order going up from this one
   * {@code null} if null input
   */
  public static List<Class<?>> getAllSuperclasses(final Class<?> cls) {
    if (cls == null) {
      return null;
    }
    final List<Class<?>> classes = new ArrayList<>();
    Class<?> superclass = cls.getSuperclass();
    while (superclass != null) {
      classes.add(superclass);
      superclass = superclass.getSuperclass();
    }
    return classes;
  }

  /**
   * <p>
   * Gets a {@code List} of all interfaces implemented by the given class and its superclasses.
   * </p>
   *
   * <p>
   * The order is determined by looking through each interface in turn as declared in the source file and following its
   * hierarchy up. Then each superclass is considered in the same way. Later duplicates are ignored, so the order is
   * maintained.
   * </p>
   *
   * @param cls the class to look up, may be {@code null}
   * @return the {@code List} of interfaces in order, {@code null} if null input
   */
  public static List<Class<?>> getAllInterfaces(Class<?> cls) {
    if (cls == null) return null;

    LinkedHashSet<Class<?>> interfacesFound = new LinkedHashSet<>();
    getAllInterfaces(cls, interfacesFound);

    return new ArrayList<>(interfacesFound);
  }

  /**
   * Get the interfaces for the specified class.
   *
   * @param cls the class to look up, may be {@code null}
   * @param interfacesFound the {@code Set} of interfaces for the class
   */
  static void getAllInterfaces(Class<?> cls, HashSet<Class<?>> interfacesFound) {
    while (cls != null) {
      Class<?>[] interfaces = cls.getInterfaces();

      for (Class<?> i : interfaces) {
        if (interfacesFound.add(i)) {
          getAllInterfaces(i, interfacesFound);
        }
      }

      cls = cls.getSuperclass();
    }
  }

  /**
   * Returns whether the given {@code type} is a primitive or primitive wrapper ({@link Boolean}, {@link Byte},
   * {@link Character}, {@link Short}, {@link Integer}, {@link Long}, {@link Double}, {@link Float}, {@link Void}).
   * <p>
   * Returns false if passed null since the method can't evaluate the class.
   * <p>
   * Inspired from apache commons-lang ClassUtils
   *
   * @param type The class to query or null.
   * @return true if the given {@code type} is a primitive or primitive wrapper ({@link Boolean}, {@link Byte},
   *         {@link Character}, {@link Short}, {@link Integer}, {@link Long}, {@link Double}, {@link Float}, {@link Void}).
   * @since 3.24.0
   */
  public static boolean isPrimitiveOrWrapper(final Class<?> type) {
    if (type == null) {
      return false;
    }
    return type.isPrimitive() || PRIMITIVE_WRAPPER_TYPES.contains(type);
  }

  /**
   * Returns whether the given {@code type} is a primitive or primitive wrapper ({@link Optional}, {@link OptionalInt},
   * {@link OptionalLong}, {@link OptionalDouble}).
   * <p>
   * Returns false if passed null since the method can't evaluate the class.
   *
   * @param type The class to query or null.
   * @return true if the given {@code type} is a primitive or primitive wrapper ({@link Optional}, {@link OptionalInt},
   *              {@link OptionalLong}, {@link OptionalDouble}).
   * @since 3.24.0
   */
  public static boolean isOptionalOrPrimitiveOptional(final Class<?> type) {
    if (type == null) {
      return false;
    }
    return OPTIONAL_TYPES.contains(type);
  }

  /**
   * Returns whether the given {@code type} belongs to the java.lang package itself or one of its subpackage.
   *
   * @param type The class to check or null.
   * @return true the given {@code type} belongs to the java.lang package itself or one of its subpackage, false otherwise.
   * @since 3.25.0
   */
  public static boolean isInJavaLangPackage(final Class<?> type) {
    return type != null && type.getName().startsWith("java.lang");
  }
}
