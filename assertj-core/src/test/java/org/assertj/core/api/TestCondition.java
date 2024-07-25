/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

/**
 * A <code>{@link Condition}</code> for testing.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 *
 * @param <T> the type of object this condition accepts.
 */
public class TestCondition<T> extends Condition<T> {

  private boolean matches;

  public TestCondition() {
    super();
  }

  public TestCondition(boolean matches) {
    this.matches = matches;
  }

  public TestCondition(String description) {
    super(description);
  }

  public void shouldMatch(boolean val) {
    matches = val;
  }

  @Override
  public boolean matches(T value) {
    return matches;
  }
}
