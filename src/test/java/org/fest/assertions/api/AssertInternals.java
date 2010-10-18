/*
 * Created on Oct 17, 2010
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
package org.fest.assertions.api;

import static org.fest.reflect.core.Reflection.*;

import org.fest.assertions.core.*;

/**
 * Reads the description of implementations of <code>{@link Assert}</code>.
 *
 * @author Alex Ruiz
 */
final class AssertInternals {

  static String descriptionOf(Assert<?> assertions)  {
    return method("descriptionText").withReturnType(String.class).in(assertions).invoke();
  }

  static Object actualIn(Assert<?> assertions) {
    return field("actual").ofType(Object.class).in(assertions).get();
  }

  static AssertionInfo infoIn(Assert<?> assertions) {
    return field("info").ofType(AssertionInfo.class).in(assertions).get();
  }

  private AssertInternals() {}
}
