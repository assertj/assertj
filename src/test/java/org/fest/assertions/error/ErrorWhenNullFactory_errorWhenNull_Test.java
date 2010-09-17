/*
 * Created on Sep 16, 2010
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
package org.fest.assertions.error;

import static junit.framework.Assert.assertSame;

import org.junit.Test;

/**
 * Tests for <code>{@link ErrorWhenNullFactory#errorWhenNull()}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class ErrorWhenNullFactory_errorWhenNull_Test {

  @Test public void should_return_singleton_ErrorWhenNullFactory() {
    AssertionErrorFactory factory = ErrorWhenNullFactory.errorWhenNull();
    assertSame(ErrorWhenNullFactory.INSTANCE, factory);
  }
}
