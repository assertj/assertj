/*
 * Created on Dec 3, 2010
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

import java.util.*;

/**
 * Contains all the implementations of <code>{@link ToStringRule}</code>.
 *
 * @author Alex Ruiz
 */
class AllRules implements Iterable<ToStringRule> {

  private final Collection<ToStringRule> rules = new HashSet<ToStringRule>();
  private final ToStringRule defaultRule = new ObjectToStringRule();

  AllRules() {
    rules.add(new ArrayToStringRule());
    rules.add(ClassToStringRule.instance());
    rules.add(new CollectionToStringRule());
    rules.add(new ConditionToStringRule());
    rules.add(new DimensionToStringRule());
    rules.add(new FileToStringRule());
    rules.add(new MapToStringRule());
    rules.add(new StringToStringRule());
  }

  public Iterator<ToStringRule> iterator() {
    return rules.iterator();
  }

  ToStringRule defaultRule() { return defaultRule; }
}
