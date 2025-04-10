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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.presentation;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

class StandardRepresentation_static_setters_Test extends AbstractBaseRepresentationTest {

  @Test
  void should_fail_on_invalid_maxElementsForPrinting() {
    assertThatIllegalArgumentException().isThrownBy(() -> StandardRepresentation.setMaxElementsForPrinting(0))
                                        .withMessage("maxElementsForPrinting must be >= 1, but was 0");
  }

  @Test
  void should_fail_on_invalid_maxLengthForSingleLineDescription() {
    assertThatIllegalArgumentException().isThrownBy(() -> StandardRepresentation.setMaxLengthForSingleLineDescription(0))
                                        .withMessage("maxLengthForSingleLineDescription must be > 0 but was 0");
  }
}
