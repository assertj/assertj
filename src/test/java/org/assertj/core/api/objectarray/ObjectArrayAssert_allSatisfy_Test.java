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

import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectArrayAssertBaseTest;
import org.junit.jupiter.api.BeforeEach;

import java.util.function.Consumer;

public class ObjectArrayAssert_allSatisfy_Test extends ObjectArrayAssertBaseTest {

  private Consumer<Object> restrictions;

  @BeforeEach
  public void beforeOnce() {
    restrictions = o -> assertThat(o).isNotNull();
  }

  @Override
  protected ObjectArrayAssert<Object> invoke_api_method() {
    return assertions.allSatisfy(restrictions);
  }

  @Override
  protected void verify_internal_effects() {
    verify(iterables).assertAllSatisfy(getInfo(assertions), newArrayList(getActual(assertions)), restrictions);
  }
}
