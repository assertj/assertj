/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.util;

import java.util.Comparator;

/**
 * Compares classes by fully qualified name.
 */
public class ClassNameComparator implements Comparator<Class<?>> {

  /** Shared class name comparator instance. */
  public static final ClassNameComparator INSTANCE = new ClassNameComparator();

  /** Creates a new class name comparator. */
  public ClassNameComparator() {}

  @Override
  public int compare(Class<?> class1, Class<?> class2) {
    return class1.getName().compareTo(class2.getName());
  }
}
