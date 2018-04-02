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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.data;

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;

public class TolkienCharacterAssert extends AbstractAssert<TolkienCharacterAssert, TolkienCharacter> {

  public TolkienCharacterAssert(TolkienCharacter actual) {
    super(actual, TolkienCharacterAssert.class);
  }

  public static TolkienCharacterAssert assertThat(TolkienCharacter actual) {
    return new TolkienCharacterAssert(actual);
  }

  // 4 - a specific assertion !
  public TolkienCharacterAssert hasName(String name) {
    // check that actual TolkienCharacter we want to make assertions on is not null.
    isNotNull();

    // check condition
    if (!Objects.equals(actual.name, name)) {
      failWithMessage("Expected character's name to be <%s> but was <%s>", name, actual.name);
    }

    // return the current assertion for method chaining
    return this;
  }

  // 4 - another specific assertion !
  public TolkienCharacterAssert hasAge(int age) {
    // check that actual TolkienCharacter we want to make assertions on is not null.
    isNotNull();

    // check condition
    if (actual.age != age) {
      failWithMessage("Expected character's age to be <%s> but was <%s>", age, actual.age);
    }

    // return the current assertion for method chaining
    return this;
  }
}