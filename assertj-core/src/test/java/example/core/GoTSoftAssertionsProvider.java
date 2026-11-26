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

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.SoftAssertionsProvider;

// tag::user_guide[]
// let's add a Game of Thrones entry point
public interface GoTSoftAssertionsProvider extends SoftAssertionsProvider {
  // custom assertions
  default GoTCharacterAssert assertThat(GoTCharacter actual) {
    return proxy(GoTCharacterAssert.class, GoTCharacter.class, actual);
  }
  // end::user_guide[]

  class GoTCharacter {

    final String name;
    final boolean dead;

    public GoTCharacter(String name, boolean dead) {
      this.name = name;
      this.dead = dead;
    }
  }

  class GoTCharacterAssert extends AbstractAssert<GoTCharacterAssert, GoTCharacter> {

    public GoTCharacterAssert(GoTCharacter actual) {
      super(actual, GoTCharacterAssert.class);
    }

    public GoTCharacterAssert isDead() {
      isNotNull();
      if (!actual.dead) {
        failWithMessage("Expected <%s> to be dead", actual.name);
      }
      return this;
    }
  }
  // tag::user_guide[]
}
// end::user_guide[]
// @formatter:on
