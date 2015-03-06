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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api.fail;

import static org.assertj.core.api.Fail.failBecauseExceptionWasNotThrown;
import static org.assertj.core.test.ExpectedException.none;

import org.assertj.core.api.Fail;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;


/**
 * Tests for <code>{@link Fail#shouldHaveThrown(Class)} (Class)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Fail_fail_because_exception_was_not_thrown_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_include_message_built_with_given_exception_name() {
    thrown.expectAssertionError("Expected NullPointerException to be thrown");
    failBecauseExceptionWasNotThrown(NullPointerException.class);
  }

  @Test
  public void should_include_message_built_with_given_throwable_name() {
    thrown.expectAssertionError("Expected OutOfMemoryError to be thrown");
    failBecauseExceptionWasNotThrown(OutOfMemoryError.class);
  }
}
