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
package org.assertj.core.api.objectarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.function.BiConsumer;

import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectArrayAssertBaseTest;
import org.junit.Before;

public class ObjectArrayAssert_zipSatisfy_Test extends ObjectArrayAssertBaseTest {

  private BiConsumer<Object, String> requirements;
  private String[] other;

  @Before
  public void beforeOnce() {
    requirements = (o1, o2) -> assertThat(o1).hasSameHashCodeAs(o2);
  }

  @Override
  protected ObjectArrayAssert<Object> invoke_api_method() {
    return assertions.zipSatisfy(other, requirements);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertZipSatisfy(getInfo(assertions),
                                       newArrayList(getActual(assertions)),
                                       newArrayList(other),
                                       requirements);
  }
}
