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
package org.assertj.core.api;


import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.test.ExpectedException;
import org.junit.Assert;
import org.junit.Rule;

/**
 * A simple base class for test.
 */
public class BaseTest {

  @Rule
  public ExpectedException thrown = none();

  protected void expectException(Class<? extends Throwable> type, String message) {
    thrown.expect(type);
    thrown.expectMessage(message);
  }
  
  protected void expectIllegalArgumentException(String message) {
    expectException(IllegalArgumentException.class, message);
  }

  public void failBecauseExpectedAssertionErrorWasNotThrown() {
    Assert.fail("Assertion error expected");
  }

}