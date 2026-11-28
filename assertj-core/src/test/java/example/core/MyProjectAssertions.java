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

import example.data.Race;
import example.data.TolkienCharacter;

// tag::user_guide[]
public class MyProjectAssertions {

  // give access to TolkienCharacter assertion
  public static TolkienCharacterAssert assertThat(TolkienCharacter actual) {
    return new TolkienCharacterAssert(actual);
  }

  // give access to TolkienCharacter Race assertion
  public static RaceAssert assertThat(Race actual) {
    return new RaceAssert(actual);
  }
}
// end::user_guide[]

class RaceAssert extends AbstractAssert<RaceAssert, Race> {

  RaceAssert(Race actual) {
    super(actual, RaceAssert.class);
  }
}
// @formatter:on
