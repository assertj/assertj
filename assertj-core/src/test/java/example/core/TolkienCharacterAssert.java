/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.core;

// @formatter:off

import java.util.Objects;

import org.assertj.core.api.AbstractAssert;

import example.data.TolkienCharacter;

// tag::user_guide[]
public class TolkienCharacterAssert extends AbstractAssert<TolkienCharacterAssert, TolkienCharacter> { // <1>

  public TolkienCharacterAssert(TolkienCharacter actual) { // <2>
    super(actual, TolkienCharacterAssert.class);
  }

  public static TolkienCharacterAssert assertThat(TolkienCharacter actual) { // <3>
    return new TolkienCharacterAssert(actual);
  }

  public TolkienCharacterAssert hasName(String name) { // <4>
    // check that actual TolkienCharacter we want to make assertions on is not null.
    isNotNull();
    // check assertion logic
    if (!Objects.equals(actual.getName(), name)) {
      failWithMessage("Expected character's name to be <%s> but was <%s>", name, actual.getName());
    }
    // return this to allow chaining other assertion methods
    return this;
  }

  public TolkienCharacterAssert hasAge(int age) { // <4>
    // check that actual TolkienCharacter we want to make assertions on is not null.
    isNotNull();
    // check assertion logic
    if (actual.getAge() != age) {
      failWithMessage("Expected character's age to be <%s> but was <%s>", age, actual.getAge());
    }
    // return this to allow chaining other assertion methods
    return this;
  }
}
// end::user_guide[]
// @formatter:on
