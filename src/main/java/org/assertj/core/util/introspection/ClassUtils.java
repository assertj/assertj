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
package org.assertj.core.util.introspection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class ClassUtils {

  /**
   * Determine whether the {@link Class} identified by the supplied name is present
   * and can be loaded. Will return {@code false} if either the class or
   * one of its dependencies is not present or cannot be loaded.
   *
   * @param className   the name of the class to check
   * @param classLoader the class loader to use
   *                    (may be {@code null}, which indicates the default class loader)
   * @return whether the specified class is present
   */
  public static boolean isPresent(String className, ClassLoader classLoader) {
    ClassLoader classLoaderToUse = classLoader;
    if (classLoaderToUse == null) {
      classLoaderToUse = getDefaultClassLoader();
    }

    try {
      classLoaderToUse.loadClass(className);
      return true;
    } catch (Throwable e) {
      return false;
    }
  }

  /**
   * Return the default ClassLoader to use: typically the thread context
   * ClassLoader, if available; the ClassLoader that loaded the ClassUtils
   * class will be used as fallback.
   * <p>Call this method if you intend to use the thread context ClassLoader
   * in a scenario where you absolutely need a non-null ClassLoader reference:
   * for example, for class path resource loading (but not necessarily for
   * {@code Class.forName}, which accepts a {@code null} ClassLoader
   * reference as well).
   *
   * @return the default ClassLoader (never {@code null})
   * @see Thread#getContextClassLoader()
   */
  private static ClassLoader getDefaultClassLoader() {
    ClassLoader cl = null;
    try {
      cl = Thread.currentThread().getContextClassLoader();
    } catch (Throwable ex) {
      // Cannot access thread context ClassLoader - falling back to system class loader...
    }
    if (cl == null) {
      // No thread context class loader -> use class loader of this class.
      cl = ClassUtils.class.getClassLoader();
    }
    return cl;
  }

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
    final List<Class<?>> classes = new ArrayList<Class<?>>();
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

}
