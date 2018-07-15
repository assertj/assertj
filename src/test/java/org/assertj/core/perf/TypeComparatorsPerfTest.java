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
package org.assertj.core.perf;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TypeComparatorsPerfTest {

  // execution time for 1000000:
  // before change: ~700ms
  // with Comparator.comparing(Class::getName) : ~240ms
  // with anonymous class replacing Comparator.comparing(Class::getName) : ~160ms

  // comment @Disabled to run the test
  @Disabled
  @Test
  public void run_100_000_object_assertions() {
    long start = System.currentTimeMillis();
    // GIVEN
    int total = 1_000_000;
    Object object = "test";
    // WHEN
    for (int i = 0; i < total; i++) {
      assertThat(object).isNotNull();
    }
    // THEN
    long end = System.currentTimeMillis();
    long duration = ChronoUnit.MILLIS.between(Instant.ofEpochMilli(start), Instant.ofEpochMilli(end));
    System.out.println("execution time for " + total + " -> " + duration + "ms");
  }

}
