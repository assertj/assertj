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
package org.assertj.core.api.chararray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.CharArrays.arrayOf;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.CharArrayAssert;
import org.assertj.core.api.CharArrayAssertBaseTest;
import org.assertj.core.util.CaseInsensitiveCharacterComparator;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.api.CharArrayAssert#containsExactly(char...)}</code>.
 * 
 * @author Jean-Christophe Gay
 */
public class CharArrayAssert_containsExactly_Test extends CharArrayAssertBaseTest {

  @Override
  protected CharArrayAssert invoke_api_method() {
    return assertions.containsExactly('a', 'b');
  }

  @Override
  protected void verify_internal_effects() {
    verify(arrays).assertContainsExactly(getInfo(assertions), getActual(assertions), arrayOf('a', 'b'));
  }

  @Test
  public void should_honor_the_given_element_comparator() {
    char[] actual = arrayOf('a', 'b');
    assertThat(actual).usingElementComparator(CaseInsensitiveCharacterComparator.instance)
                      .containsExactly('a', 'B');
  }}
