/*
 * Created on Sep 9, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.formatting;

import java.util.*;

import org.fest.util.VisibleForTesting;

/**
 * Returns the {@code String} representation of a {@code Object}, based on registered
 * <code>{@link ToStringRule}</code>s.
 *
 * @author Alex Ruiz
 */
public class ToStringConverter {

  private final Set<ToStringRule> rules = new HashSet<ToStringRule>();

  private final ToStringRule defaultRule = new ObjectToStringRule();

  @VisibleForTesting
  ToStringConverter() {
    rules.add(new ArrayToStringRule());
    rules.add(new ClassToStringRule());
    rules.add(new CollectionToStringConverter());
    rules.add(new FileToStringConverter());
    rules.add(new MapToStringConverter());
  }

  public String toStringOf(Object o) {
    if (o == null) return defaultToString(o);
    for (ToStringRule rule : rules)
      if (rule.canHandle(o)) return rule.toStringOf(o);
    return defaultToString(o);
  }

  private String defaultToString(Object o) {
    return defaultRule.toStringOf(o);
  }
}
