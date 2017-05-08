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
package org.assertj.core.api.date;

import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.Date;

import org.assertj.core.api.DateAssert;


/**
 * Tests for {@link DateAssert#isIn(java.util.Collection))} with Collection of Date or String.
 * 
 * @author Joel Costigliola
 */
public class DateAssert_isIn_with_collection_param_Test extends AbstractDateAssertWithDateArg_Test {

  @Override
  protected DateAssert assertionInvocationWithDateArg() {
    return assertions.isIn(newArrayList(otherDate));
  }

  @Override
  protected DateAssert assertionInvocationWithStringArg(String dateAsString) {
    return assertions.isInWithStringDateCollection(newArrayList(dateAsString));
  }

  @Override
  protected void verifyAssertionInvocation(Date date) {
    verify(objects).assertIsIn(getInfo(assertions), getActual(assertions), newArrayList(date));
  }

}
