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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.abstract_; // Make sure that package-private access is lost

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractSoftAssertions;
import org.assertj.core.api.AbstractStandardSoftAssertions;
import org.assertj.core.api.SoftAssertionsProvider;
import org.junit.jupiter.api.Test;

/**
 * This tests that classes extended from {@link AbstractStandardSoftAssertions} will have access to the list of
 * collected errors that the various proxies have collected.
 */
public class SoftAssertionsMultipleProjectsTest {

  static class MyClass1 {

  }

  static class MyClass2 {

  }

  static class MyClass1Assert extends AbstractAssert<MyClass1Assert, MyClass1> {
    public MyClass1Assert(MyClass1 actual) {
      super(actual, MyClass1Assert.class);
    }

    public MyClass1Assert isClassy() {
      throw new AssertionError("Not classy");
    }

    public MyClass1Assert isNotClassy() {
      return this;
    }
  }

  static class MyClass2Assert extends AbstractAssert<MyClass2Assert, MyClass2> {
    public MyClass2Assert(MyClass2 actual) {
      super(actual, MyClass2Assert.class);
    }

    public MyClass2Assert isClassy() {
      return this;
    }

    public MyClass2Assert isNotClassy() {
      throw new AssertionError("Classy");
    }
  }

  static interface Class1SoftAssertions extends SoftAssertionsProvider {
    default MyClass1Assert assertThat(MyClass1 actual) {
      return proxy(MyClass1Assert.class, MyClass1.class, actual);
    }
  }

  static interface Class2SoftAssertions extends SoftAssertionsProvider {
    default MyClass2Assert assertThat(MyClass2 actual) {
      return proxy(MyClass2Assert.class, MyClass2.class, actual);
    }
  }

  static class UberSoftAssertions extends AbstractSoftAssertions
      implements Class1SoftAssertions, Class2SoftAssertions, AbstractStandardSoftAssertions {
  }

  private final MyClass1 class1ForTesting = null;
  private final MyClass2 class2ForTesting = null;
  private final UberSoftAssertions softly = new UberSoftAssertions();

  @Test
  public void return_empty_list_of_errors_for_passing_assertions() {
    softly.assertThat(class1ForTesting).isNotClassy(); // No errors to collect
    softly.assertThat(class2ForTesting).isClassy(); // No errors to collect
    softly.assertThat("String").isEqualTo("String"); // No errors to collect
    assertThat(softly.errorsCollected()).isEmpty();
    assertThat(softly.errorsCollected()).isEqualTo(softly.assertionErrorsCollected());
  }

  @Test
  public void returns_list_of_errors_in_the_order_they_were_asserted() {
    softly.assertThat(class1ForTesting).isClassy();
    softly.assertThat(class2ForTesting).isNotClassy();
    softly.assertThat("String").isEqualTo("Strung");
    List<Throwable> errorsCollected = softly.errorsCollected();
    assertThat(errorsCollected).as("size").hasSize(3);
    assertThat(errorsCollected.get(0)).as("0").hasMessage("Not classy");
    assertThat(errorsCollected.get(1)).as("1").hasMessage("Classy");
    assertThat(errorsCollected.get(2)).as("2").hasMessageMatching("(?s).*String.*equal to.*Strung.*not.*");
    assertThat(errorsCollected).isEqualTo(softly.assertionErrorsCollected());
  }
}
