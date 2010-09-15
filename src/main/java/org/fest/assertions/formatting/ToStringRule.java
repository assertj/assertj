/*
 * Created on Sep 9, 2010
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
package org.fest.assertions.formatting;

/**
 * Returns the {@code String} representation of a {@code String}.
 *
 * @author Alex Ruiz
 */
interface ToStringRule {

  /**
   * Returns the {@code String} representation of the given {@code Object}.
   * @param o the given {@code Object}. It will be never {@code null}.
   * @return the {@code String} representation of the given {@code Object}.
   */
  String toStringOf(Object o);

  /**
   * Indicates whether this rule can obtain the {@code String} representation of the given {@code Object}.
   * @param o the given {@code Object}. It will be never {@code null}.
   * @return {@code true} if this rule can obtain the {@code String} representation of the given {@code Object};
   * {@code false} otherwise.
   */
  boolean canHandle(Object o);
}
