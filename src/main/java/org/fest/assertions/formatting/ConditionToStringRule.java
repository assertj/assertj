/*
 * Created on Sep 30, 2010
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

import org.fest.assertions.core.Condition;
import org.fest.assertions.description.Description;

/**
 * Returns the {@code String} representation of a <code>{@link Condition}</code>.
 *
 * @author Alex Ruiz
 */
class ConditionToStringRule extends GenericToStringRule<Condition<?>> {

  @Override String doGetToString(Condition<?> c) {
    Description d = c.description();
    return d != null ? d.value() : null;
  }

  @SuppressWarnings("unchecked") @Override Class<Condition<?>> supportedType() {
    Class<?> type = Condition.class;
    return (Class<Condition<?>>)type;
  }
}
