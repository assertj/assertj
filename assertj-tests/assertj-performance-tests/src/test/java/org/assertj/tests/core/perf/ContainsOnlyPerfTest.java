/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.perf;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * These tests ensure assertThat(list_of_1m_elements).containsOnly(...) is an O(N) rather than O(N^2)
 * operation. Given that the list has 1 million elements, O(N^2) is O(1000 billion), which should
 * take from several dozens to several thousands seconds, given that the constant in the actual
 * complexity is low (which indeed is, in case of ArrayList, where the main contributor to the time
 * consumed by the operation is System.arraycopy()). O(N) is O(1 million), which should take perhaps
 * from one to a hundred milliseconds, depending on the frequency of the test machine, the JVM
 * version used, etc.
 * <p>
 * Therefore, 5 seconds (the limit used in the tests below) seems to be a good threshold that would
 * clearly distinguish .containsOnly(...) being O(N) or O(N^2) on any test agent, thus preventing
 * a regression of .containsOnly(...) back to O(N^2) complexity.
 *
 * @see <a href="https://github.com/assertj/assertj/issues/1718">assertj/assertj#1718</a>
 */
class ContainsOnlyPerfTest {

  @Test
  @Timeout(value = 5)
  void test_containsOnly_1mElements() {
    final ArrayList<Object> objects = new ArrayList<>();
    for (int i = 0; i < 1_000_000; i++) {
      objects.add(ThreadLocalRandom.current().nextBoolean());
    }
    assertThat(objects).containsOnly(TRUE, FALSE);
  }

  @Test
  @Timeout(value = 5)
  void test_containsOnly_1mElements_usingCustomComparator() {
    final ArrayList<Integer> objects = new ArrayList<>();
    for (int i = 0; i < 1_000_000; i++) {
      objects.add(ThreadLocalRandom.current().nextInt(2));
    }
    assertThat(objects).usingElementComparator(Integer::compare)
                       .containsOnly(0, 1);
  }

}
