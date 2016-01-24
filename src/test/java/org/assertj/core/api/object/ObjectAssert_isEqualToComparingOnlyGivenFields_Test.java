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
package org.assertj.core.api.object;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.Comparator;

import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertBaseTest;
import org.assertj.core.test.Jedi;
import org.junit.Test;


/**
 * Tests for <code>{@link ObjectAssert#isEqualToComparingOnlyGivenFields(Object, String...)}</code>.
 * 
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
public class ObjectAssert_isEqualToComparingOnlyGivenFields_Test extends ObjectAssertBaseTest {

  private Jedi other = new Jedi("Yoda", "Blue");

  @Override
  protected ObjectAssert<Jedi> invoke_api_method() {
    return assertions.isEqualToComparingOnlyGivenFields(other, "name");
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void verify_internal_effects() {
    verify(objects).assertIsEqualToComparingOnlyGivenFields(getInfo(assertions), getActual(assertions), other,
            Collections.EMPTY_MAP, "name");
  }
  
  @Test
  public void should_be_able_to_use_a_comparator_for_specified_fields() {
    Comparator<String> alwaysEqual = new Comparator<String>() {
      public int compare(String o1, String o2) {
        return 0;
      }
    };
    Jedi actual = new Jedi("Yoda", "Green");
    Jedi other = new Jedi("Luke", "Blue");
    
    assertThat(actual).usingComparatorForFields(alwaysEqual, "name").isEqualToComparingOnlyGivenFields(other, "name");
  }
}
