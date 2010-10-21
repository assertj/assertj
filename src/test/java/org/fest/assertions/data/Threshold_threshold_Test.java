/*
 * Created on Oct 20, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.data;

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.test.ExpectedException.none;

import org.fest.assertions.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link Threshold#threshold(int)}</code>.
 *
 * @author Yvonne Wang
 */
public class Threshold_threshold_Test {

  @Rule public ExpectedException thrown = none();

  @Test public void should_throw_error_if_value_is_negative() {
    thrown.expectIllegalArgumentException("The value of the threshold should not be negative");
    Threshold.threshold(-6);
  }

  @Test public void should_create_new_Threshold_with_given_value() {
    Threshold threshold = Threshold.threshold(8);
    assertEquals(8, threshold.value());
  }
}
