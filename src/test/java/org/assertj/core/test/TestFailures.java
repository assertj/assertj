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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.test;

import static org.assertj.core.api.Assertions.fail;

/**
 * @author Yvonne Wang
 * @author Francis Galiegue
 */

public final class TestFailures {

  /**
   * <b>Note for developers</b> :
   *
   * Avoid using this method to make a test fail
   * <p></p>
   *  The idiom to check an expected behavior when an exception is thrown should be avoided :
   *  <pre><code class='java'>
   *    try{
   *       doSomething();
   *    }
   *    catch(MyException e){
   *      assertTheExceptionState();
   *      assertOnOtherThingsThanTheException();
   *      return;
   *    }
   *    Test.failBecauseExpectedAssertionErrorWasNotThrown();
   *  </pre>
   *  </code>
   *
   * And replaced by the straighter way :
   *
   * <pre><code class='java'>
   *   Assertions.assertThatExceptionOfType(MyException.class)
   *             .isThrownBy(()-> doSomething())
   *             .withXXX(...);
   *   assertOnOtherThingsThanTheException();
   *  </pre>
   *  </code>
   *
   */
  //FIXME A developer method probably to deprecate but as currently very used in the testing code, I add only the information in the javadoc
  public static void failBecauseExpectedAssertionErrorWasNotThrown() {
    fail("Assertion error expected");
  }

  public static void wasExpectingAssertionError() {
    throw new AssertionErrorExpectedException();
  }

  private TestFailures() {
  }
}
