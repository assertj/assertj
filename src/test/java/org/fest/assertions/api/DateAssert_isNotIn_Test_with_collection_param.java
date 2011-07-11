/*
 * Created on Dec 21, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api;

import static org.fest.util.Collections.list;

import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.Before;

import org.fest.assertions.internal.Objects;

/**
 * Tests for {@link DateAssert#isIn(java.util.Collection))} with Collection of Date or String.
 *
 * @author Joel Costigliola
 */
public class DateAssert_isNotIn_Test_with_collection_param extends AbstractDateAssertWithDateArg_Test {

  @Override
  @Before
  public void setUp() {
    super.setUp();
    assertions.objects = mock(Objects.class);
  }
  
  @Override
  protected DateAssert assertionInvocationWithDateArg() {
    return assertions.isNotIn(list(otherDate));
  }

  @Override
  protected DateAssert assertionInvocationWithStringArg(String dateAsString) {
    return assertions.isNotInWithStringDateCollection(list(dateAsString));
  }

  @Override
  protected void verifyAssertionInvocation(Date date) {
    verify(assertions.objects).assertIsNotIn(assertions.info, assertions.actual, list(date));
  }

}
