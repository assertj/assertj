/*
 * Created on Nov 18, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api;

import org.fest.assertions.core.WritableAssertionInfo;
import org.fest.assertions.internal.Objects;

/**
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ConcreteAssert extends AbstractAssert<ConcreteAssert, Object> {

  public ConcreteAssert(Object actual) {
    super(actual, ConcreteAssert.class);
  }

  /**
   * Not a really relevant assertion, the goal is to show how to write a new assertion with a specific error message
   * that honors the description set by the assertion user.
   */
  public ConcreteAssert checkNull() {
    // set a specific error message
    WritableAssertionInfo info = getWritableAssertionInfo();
    info.overridingErrorMessage("specific error message");
    Objects.instance().assertNull(info, actual);
    // return the current assertion for method chaining
    return this;
  }

}
