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
 * Uses Generics to simplify creation of <code>{@link ToStringRule}</code>s.
 *
 * @author Alex Ruiz
 */
abstract class GenericToStringRule<T> implements ToStringRule {

  public final String toStringOf(Object o) {
    return doGetToString(supportedType().cast(o));
  }

  abstract String doGetToString(T o);

  public final boolean canHandle(Object o) {
    return supportedType().isInstance(o);
  }

  abstract Class<T> supportedType();
}
