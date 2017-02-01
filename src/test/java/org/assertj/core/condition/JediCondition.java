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
package org.assertj.core.condition;

import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.Set;

import org.assertj.core.api.Condition;


/**
 * 
 * A {@code Condition} checking is a Jedi
 * 
 * @author Nicolas François
 */
public class JediCondition extends Condition<String> {

  private final Set<String> jedis = newLinkedHashSet("Luke", "Yoda", "Obiwan");

  JediCondition(String description) {
    super(description);
  }

  public JediCondition() {
    super("Jedi");
  }

  @Override
  public boolean matches(String value) {
    return jedis.contains(value);
  }

}
