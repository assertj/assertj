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
package org.assertj.core.api.abstract_;

import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractAssertBaseTest;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ConcreteAssert;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link AbstractAssert#isSameAs(Object)}</code>.
 * 
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 */
public class AbstractAssert_isSameAs_Test extends AbstractAssertBaseTest {

  @Override
  protected ConcreteAssert invoke_api_method() {
    return assertions.isSameAs(8L);
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertSame(getInfo(assertions), getActual(assertions), 8L);
  }

  @Test
  public void should_be_loosely_typed1() {
	  List<String> expected = new ArrayList<>();
	  List<? extends String> actual = expected;
	  Assertions.assertThat(actual).isSameAs(expected);
  }

  @Test
  public void should_be_loosely_typed2() {
    List<? extends String> expected = new ArrayList<>();
    List<? extends String> actual = expected;
    Assertions.assertThat(actual).isSameAs(expected);
  }
}