/*
 * Created on Oct 7, 2010
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
 * Copyright @2010-2012 the original author or authors.
 */

package org.assertj.core.util;

import static org.assertj.core.util.Lists.newArrayList;

import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Utilities related to Java data types.
 * 
 * @author Alex Ruiz
 */
public final class Types {
  private static final Class<?>[] PRIMITIVE_TYPES =
    { boolean.class, byte.class, short.class, int.class, long.class, float.class, double.class, char.class };

  private static final Class<?>[] COLLECTION_TYPES = { Collection.class, List.class, Queue.class, Set.class };

  public static List<Class<?>> primitiveTypes() {
    return newArrayList(PRIMITIVE_TYPES);
  }

  public static List<Class<?>> collectionTypes() {
    return newArrayList(COLLECTION_TYPES);
  }

  private Types() {}
}
