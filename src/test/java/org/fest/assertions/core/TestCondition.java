/*
 * Created on Jul 15, 2010
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
package org.fest.assertions.core;

/**
 * A <code>{@link Condition}</code> for testing.
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class TestCondition extends Condition<Object> {

  public TestCondition() {
    super();
  }

  public TestCondition(String description) {
    super(description);
  }

  @Override public boolean matches(Object value) {
    return false;
  }
}
