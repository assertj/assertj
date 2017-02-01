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
package org.assertj.core.test;

import org.assertj.core.api.AbstractIterableAssert;

/**
 * Helper class for testing <code>{@link AbstractIterableAssert#extractingResultOf(String)}</code>.
 * 
 * @author Michał Piotrkowski
 *
 */
public class FluentJedi {

  private final Name name;
  private final int age;
  private final boolean darkSide;
  
  public FluentJedi(Name name, int years, boolean darkSide) {
    this.name = name;
    this.age = years;
    this.darkSide = darkSide;
  }

  public Name name(){
    return name;
  }
  
  public boolean darkSide(){
    return darkSide;
  }
  
  public int age(){
    return age;
  }
  
  @Override
  public String toString() {
    return name.getFirst();
  }
}
