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
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.testkit.TolkienCharacter.Race.HOBBIT;

import org.assertj.core.testkit.TolkienCharacter;
import org.assertj.core.testkit.TolkienCharacterAssert;
import org.junit.jupiter.api.Test;

class SoftAssertionProviderTest extends BaseAssertionsTest {

  @Test
  void proxy_should_create_soft_aware_assertion_instance() {
    // GIVEN
    TolkienCharacter frodo = TolkienCharacter.of("frodo", 33, HOBBIT);
    TolkienSoftAssertions softly = new TolkienSoftAssertions();
    // WHEN
    softly.assertThat(frodo).hasAge(10); // Expect failure - age will be 0 due to not being initialized.
    softly.assertThat((TolkienCharacter) null).hasAge(11); // Expect failure - argument is null.
    // THEN
    then(softly.errorsCollected()).hasSize(2);
  }

  static class TolkienSoftAssertions extends SoftAssertions {

    public TolkienCharacterAssert assertThat(TolkienCharacter actual) {
      return proxy(TolkienCharacterAssert.class, TolkienCharacter.class, actual);
    }
  }

}
