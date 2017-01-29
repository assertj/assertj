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
package org.assertj.core.presentation;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Filip Hrisafov
 */
public class StandardRepresentation_static_setters_Test extends AbstractBaseRepresentationTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_fail_on_invalid_maxElementsForPrinting() {
    thrown.expectIllegalArgumentException("maxElementsForPrinting must be >= 1, but was 0");
    StandardRepresentation.setMaxElementsForPrinting(0);
  }

  @Test
  public void should_fail_on_invalid_maxLengthForSingleLineDescription() {
    thrown.expectIllegalArgumentException("maxLengthForSingleLineDescription must be > 0 but was 0");
    StandardRepresentation.setMaxLengthForSingleLineDescription(0);
  }
}
