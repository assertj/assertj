/*
 * Created on Sep 10, 2010
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

import static org.fest.util.Maps.format;

import java.util.Map;

/**
 * Returns the {@code String} representation of a <code>{@link Map}</code>.
 * @author Alex Ruiz
 */
class MapToStringRule extends GenericToStringRule<Map<?, ?>> {

  @Override String doGetToString(Map<?, ?> m) {
    return format(m);
  }

  @SuppressWarnings("unchecked") @Override Class<Map<?, ?>> supportedType() {
    Class<?> type = Map.class;
    return (Class<Map<?, ?>>) type;
  }
}
