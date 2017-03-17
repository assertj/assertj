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
package org.assertj.core.presentation;

/**
 * Controls the formatting (String representation) of types in assertion error message.
 * 
 * @author Mariusz Smykula
 */
public interface Representation {

  /**
   * Returns the {@code String} representation of the given object. It may or not the object's own implementation of
   * {@code toString}.
   *
   * @param object the object to represent.
   * @return the {@code toString} representation of the given object.
   */
  String toStringOf(Object object);

  /**
   * Returns the {@code String} representation of the given object with its type and hexadecimal hash code so that 
   * it can be differentied from other objects with the same {@link #toStringOf(Object)} representation.
   *
   * @param object the object to represent.
   * @return the {@code toString} representation of the given object.
   */
  String unambiguousToStringOf(Object object);

}
