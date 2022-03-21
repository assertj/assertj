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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

class ClassLoadingStrategyFactory {

  private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
  private static final Method PRIVATE_LOOKUP_IN;
  // Class loader of AssertJ
  static final ClassLoader ASSERTJ_CLASS_LOADER = ClassLoadingStrategyFactory.class.getClassLoader();

  static {
    Method privateLookupIn;
    try {
      privateLookupIn = MethodHandles.class.getMethod("privateLookupIn", Class.class, MethodHandles.Lookup.class);
    } catch (Exception e) {
      privateLookupIn = null;
    }
    PRIVATE_LOOKUP_IN = privateLookupIn;
  }

  static ClassLoadingStrategyPair classLoadingStrategy(Class<?> assertClass) {
    // Use ClassLoader of assertion class to allow ByteBuddy to always find it.
    // This is needed in an OSGi runtime when a custom assertion class is
    // defined in a different OSGi bundle.
    ClassLoader assertClassLoader = assertClass.getClassLoader();
    if (assertClassLoader != ASSERTJ_CLASS_LOADER) {
      // Return a new CompositeClassLoader if the assertClass is from a
      // different class loader than AssertJ. Otherwise return the class
      // loader of AssertJ since there is no need to use a composite class
      // loader.
      CompositeClassLoader compositeClassLoader = new CompositeClassLoader(assertClassLoader);
      return new ClassLoadingStrategyPair(compositeClassLoader, compositeClassLoader);
    }
    if (ClassInjector.UsingReflection.isAvailable()) {
      return new ClassLoadingStrategyPair(assertClassLoader, ClassLoadingStrategy.Default.INJECTION);
    } else if (ClassInjector.UsingLookup.isAvailable()) {
      try {
        return new ClassLoadingStrategyPair(assertClassLoader,
                                            ClassLoadingStrategy.UsingLookup.of(PRIVATE_LOOKUP_IN.invoke(null, assertClass,
                                                                                                         LOOKUP)));
      } catch (Exception e) {
        throw new IllegalStateException("Could not access package of " + assertClass, e);
      }
    } else {
      throw new IllegalStateException("No code generation strategy available");
    }
  }

  // Pair holder of class loader and class loading strategy to use
  // for ByteBuddy class generation.
  static class ClassLoadingStrategyPair {
    private final ClassLoader classLoader;
    private final ClassLoadingStrategy<ClassLoader> classLoadingStrategy;

    ClassLoadingStrategyPair(ClassLoader classLoader, ClassLoadingStrategy<ClassLoader> classLoadingStrategy) {
      this.classLoader = classLoader;
      this.classLoadingStrategy = classLoadingStrategy;
    }

    ClassLoader getClassLoader() {
      return classLoader;
    }

    ClassLoadingStrategy<ClassLoader> getClassLoadingStrategy() {
      return classLoadingStrategy;
    }
  }

  // Composite class loader for when the assert class is from a different
  // class loader than AssertJ. This can occur in OSGi when the assert class is
  // from a bundle. The composite class loader provides access to the internal,
  // non-exported types of AssertJ. ByteBuddy will define the proxy class in the
  // CompositeClassLoader rather than in the class loader of the assert class.
  // This means the assert class cannot assume package private access to super
  // types, interfaces, etc. since the proxy class is defined in a different
  // class loader (the CompositeClassLoader) than the assert class.
  static class CompositeClassLoader extends ClassLoader implements ClassLoadingStrategy<ClassLoader> {
    CompositeClassLoader(ClassLoader parent) {
      super(parent);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
      return ASSERTJ_CLASS_LOADER.loadClass(name);
    }

    @Override
    protected URL findResource(String name) {
      return ASSERTJ_CLASS_LOADER.getResource(name);
    }

    @Override
    protected Enumeration<URL> findResources(String name) throws IOException {
      return ASSERTJ_CLASS_LOADER.getResources(name);
    }

    @Override
    public Map<TypeDescription, Class<?>> load(ClassLoader classLoader, Map<TypeDescription, byte[]> types) {
      Map<TypeDescription, Class<?>> result = new LinkedHashMap<>();
      for (Map.Entry<TypeDescription, byte[]> entry : types.entrySet()) {
        TypeDescription typeDescription = entry.getKey();
        String name = typeDescription.getName();
        synchronized (getClassLoadingLock(name)) {
          Class<?> type = findLoadedClass(name);
          if (type != null) {
            throw new IllegalStateException("Cannot define already loaded type: " + type);
          }
          byte[] typeDefinition = entry.getValue();
          type = defineClass(name, typeDefinition, 0, typeDefinition.length);
          result.put(typeDescription, type);
        }
      }
      return result;
    }
  }
}
