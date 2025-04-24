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
package org.assertj.core.internal;

import java.util.Map;
import java.util.stream.Stream;

/**
 * An internal holder of the custom message for type. It is used to store messages for registered classes.
 * When looking for a message for a given class the holder returns the most relevant comparator.
 */
public class TypeMessages extends TypeHolder<String> {

  /**
   * This method returns the most relevant error message for the given class. The most relevant message is the
   * message which is registered for the class that is closest in the inheritance chain of the given {@code clazz}.
   * The order of checks is the following:
   * 1. If there is a registered message for {@code clazz} then this one is used
   * 2. We check if there is a registered message for a superclass of {@code clazz}
   * 3. We check if there is a registered message for an interface of {@code clazz}
   *
   * @param clazz the class for which to find an error message
   * @return the most relevant error message, or {@code null} if no message could be found
   */
  public String getMessageForType(Class<?> clazz) {
    return super.get(clazz);
  }

  /**
   * Checks, whether an any custom error message is associated with the giving type.
   *
   * @param type the type for which to check a error message
   * @return is the giving type associated with any custom error message
   */
  public boolean hasMessageForType(Class<?> type) {
    return super.hasEntity(type);
  }

  /**
   * Puts the {@code message} for the given {@code clazz}.
   *
   * @param clazz the class for the error message
   * @param message the error message itself
   * @param <T> the type of the objects to associate with the message for
   */
  public <T> void registerMessage(Class<T> clazz, String message) {
    super.put(clazz, message);
  }

  /**
   * Returns a sequence of all type-message pairs which the current holder supplies.
   *
   * @return sequence of field-message pairs
   */
  public Stream<Map.Entry<Class<?>, String>> messageByTypes() {
    return super.entityByTypes();
  }
}
