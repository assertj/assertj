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

import static org.fest.util.Collections.format;

import java.util.Collection;

/**
 * Returns the {@code String} representation of an <code>{@link Collection}</code>.
 * @author Alex Ruiz
 */
class CollectionToStringRule extends TypeBasedToStringRule<Collection<?>> {

  @Override String doGetToString(Collection<?> c) {
    return format(c);
  }

  @SuppressWarnings("unchecked") @Override Class<Collection<?>> supportedType() {
    Class<?> type = Collection.class;
    return (Class<Collection<?>>) type;
  }
}
