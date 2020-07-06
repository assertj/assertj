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
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.assertj.core.api.AfterAssertionErrorCollected;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class SoftAssertions_setAfterAssertionErrorCollected_Test {

  static class AssertionErrorRecorder implements AfterAssertionErrorCollected {
    List<AssertionError> recordedErrors = list();

    @Override
    public void onAssertionErrorCollected(AssertionError assertionError) {
      recordedErrors.add(assertionError);
    }
  }

  @Test
  public void should_collect_all_assertion_errors_by_implementing_AfterAssertionErrorCollected() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    AssertionErrorRecorder assertionErrorRecorder = new AssertionErrorRecorder();
    softly.setAfterAssertionErrorCollected(assertionErrorRecorder);
    // WHEN
    softly.assertThat("foo").isNull();
    softly.assertThat("foo").contains("a").contains("b");
    // THEN
    assertThat(assertionErrorRecorder.recordedErrors).hasSize(3)
                                                     .containsExactlyElementsOf(softly.assertionErrorsCollected());
  }
}
