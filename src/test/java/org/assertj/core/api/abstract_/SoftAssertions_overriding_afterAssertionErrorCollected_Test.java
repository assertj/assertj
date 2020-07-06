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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.abstract_; // Make sure that package-private access is lost

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.TolkienCharacter.Race.HOBBIT;
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.data.TolkienCharacter;
import org.assertj.core.data.TolkienCharacterAssert;
import org.junit.jupiter.api.Test;

public class SoftAssertions_overriding_afterAssertionErrorCollected_Test {

  static class RecordingSoftAssertions extends SoftAssertions {

    List<AssertionError> recordedErrors = list();

    public TolkienCharacterAssert assertThat(TolkienCharacter actual) {
      return proxy(TolkienCharacterAssert.class, TolkienCharacter.class, actual);
    }

    @Override
    public void onAssertionErrorCollected(AssertionError assertionError) {
      recordedErrors.add(assertionError);
    }
  }

  @Test
  public void should_collect_all_assertion_errors_by_implementing_AfterAssertionErrorCollected() {
    // GIVEN
    RecordingSoftAssertions recordingSoftly = new RecordingSoftAssertions();
    TolkienCharacter frodo = TolkienCharacter.of("Frodo", 33, HOBBIT);
    // WHEN
    recordingSoftly.assertThat("foo").isNull();
    recordingSoftly.assertThat(frodo).hasName("Bilbo");
    // THEN
    assertThat(recordingSoftly.recordedErrors).hasSize(2)
                                              .containsExactlyElementsOf(recordingSoftly.assertionErrorsCollected());
  }
}
