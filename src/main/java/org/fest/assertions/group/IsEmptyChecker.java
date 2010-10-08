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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.group;

/**
 * Indicates whether a group of values (e.g. a collection or an array) is empty or not.
 *
 * @author Alex Ruiz
 */
interface IsEmptyChecker {

  /**
   * Indicates if this checker is able to verify that the given object is an empty group of values.
   * @param o the object to verify.
   * @return {@code true} if this checker is able to verify that the given object is an empty group of values;
   * {@code false} otherwise.
   */
  boolean canHandle(Object o);

  /**
   * Indicates whether the given object is an empty group of values. This method assumes that
   * <code>{@link #canHandle(Object)}</code> was already invoked using the same argument and that it returned
   * {@code true}.
   * @param o the object to verify.
   * @return {@code true} if the given object is an empty group of values; {@code false} otherwise.
   */
  boolean isEmpty(Object o);
}
