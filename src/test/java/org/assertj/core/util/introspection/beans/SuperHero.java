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
package org.assertj.core.util.introspection.beans;

import org.assertj.core.test.Person;

public class SuperHero extends Person {

  private final Person trueIdentity;
  private final Person archenemy;

  public SuperHero(String name, Person trueIdentity, Person archenemy) {
    super(name);
    this.trueIdentity = trueIdentity;
    this.archenemy = archenemy;
  }

  public Person archenemy() {
    return archenemy;
  }
  
  public Person getArchenemy() {
	return archenemy;
  }

  public void saveTheDay() {
    // do some superheroic stuff
  }

  @SuppressWarnings("unused")
  private Person trueIdentity() {
    return trueIdentity;
  }
  
  Person getTrueIdentity() {
	return trueIdentity;
  }
}