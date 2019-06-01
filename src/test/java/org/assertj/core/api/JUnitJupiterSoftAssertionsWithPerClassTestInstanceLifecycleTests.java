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
package org.assertj.core.api;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;

@TestMethodOrder(Alphanumeric.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@Disabled // test1 is meant to fail (but not test2)
public class JUnitJupiterSoftAssertionsWithPerClassTestInstanceLifecycleTests {

  @RegisterExtension
  JUnitJupiterSoftAssertions softly = new JUnitJupiterSoftAssertions();

  @Nested
  class A {
    @Test
    void test1() {
      // should fail
      softly.assertThat(1).isLessThan(0);
    }

    @Test
    void test2() {
      // should pass
      softly.assertThat(0).isLessThan(1);
    }
  }
}
