package org.fest.assertions.api;

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
    public int compareTo(Object other) {
      return 0; // always equals for the test
    }
  }

  // we'd like to get rid of the compile error here
  private static final class TestClassAssert extends AbstractUnevenComparableAssert<TestClassAssert, TestClass> {

    TestClassAssert(TestClass actual) {
      super(actual, TestClassAssert.class);
    }

    static TestClassAssert assertThat(TestClass actual) {
      return new TestClassAssert(actual);
    }

  }

}