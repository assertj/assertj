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
package org.assertj.core.api;

import org.junit.Test;

public class ComparableAssertion_should_be_flexible_Test {

  @Test
  public void comparable_api_should_be_flexible() {
    TestClass testClass1 = new TestClass();
    TestClass testClass2 = new TestClass();

    TestClassAssert.assertThat(testClass1).isEqualByComparingTo(testClass2);
  }

  // The important thing here is that TestClass implements Comparable<Object> and not Comparable<TestClass>
  // even
  private static final class TestClass implements Comparable<Object> {
    @Override
    public int compareTo(Object other) {
      return 0; // always equals for the test
    }
  }

  // we'd like to get rid of the compile error here
  private static final class TestClassAssert extends AbstractComparableAssert<TestClassAssert, TestClass> {

    TestClassAssert(TestClass actual) {
      super(actual, TestClassAssert.class);
    }

    static TestClassAssert assertThat(TestClass actual) {
      return new TestClassAssert(actual);
    }

  }

}