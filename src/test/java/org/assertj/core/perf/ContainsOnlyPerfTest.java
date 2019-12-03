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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.perf;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;

/**
 * See https://github.com/joel-costigliola/assertj-core/issues/1718
 */
public class ContainsOnlyPerfTest {

  @Test(timeout = 1000) // Should complete in 1 second
  public void test_containsOnly_1mElements() {
    final ArrayList<Object> objects = new ArrayList<>();
    for (int i = 0; i < 1_000_000; i++) {
      objects.add(ThreadLocalRandom.current().nextBoolean());
    }
    assertThat(objects).containsOnly(Boolean.TRUE, Boolean.FALSE);
  }

  @Test(timeout = 1000) // Should complete in 1 second
  public void test_containsOnly_1mElements_usingCustomComparator() {
    final ArrayList<Integer> objects = new ArrayList<>();
    for (int i = 0; i < 1_000_000; i++) {
      objects.add(ThreadLocalRandom.current().nextInt(2));
    }
    assertThat(objects).usingElementComparator(Integer::compare).containsOnly(0, 1);
  }
}
