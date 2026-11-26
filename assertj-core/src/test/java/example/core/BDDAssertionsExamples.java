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

import static example.data.Race.HOBBIT;

import org.junit.jupiter.api.Test;

// tag::user_guide[]
import static org.assertj.core.api.BDDAssertions.then;

public class BDDAssertionsExamples extends AbstractAssertionsExamples {

  // the data used are initialized in AbstractAssertionsExamples.

  @Test
  public void withAssertions_examples() {

    // then methods come from BDDAssertions.then static
    then(frodo.age).isEqualTo(33);
    then(frodo.getName()).isEqualTo("Frodo").isNotEqualTo("Frodon");

    then(frodo).isIn(fellowshipOfTheRing);
    then(frodo).isIn(sam, frodo, pippin);
    then(sauron).isNotIn(fellowshipOfTheRing);

    then(frodo).matches(p -> p.age > 30 && p.getRace() == HOBBIT);
    then(frodo.age).matches(p -> p > 30);
  }
}
// end::user_guide[]
// @formatter:on
