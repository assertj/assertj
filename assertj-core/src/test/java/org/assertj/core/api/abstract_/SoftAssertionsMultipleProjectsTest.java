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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.abstract_; // Make sure that package-private access is lost

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.testkit.ErrorMessagesForTest.shouldBeEqualMessage;

import java.util.List;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractSoftAssertions;
import org.assertj.core.api.SoftAssertionsProvider;
import org.assertj.core.api.StandardSoftAssertionsProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * This tests that classes extended from {@link StandardSoftAssertionsProvider} will have access to the list of
 * collected errors that the various proxies have collected.
 */
@DisplayName("Multiple custom SoftAssertions")
class SoftAssertionsMultipleProjectsTest {

  static class NotClassy {

  }

  static class Classy {

  }

  static class NotClassyAssert extends AbstractAssert<NotClassyAssert, NotClassy> {
    public NotClassyAssert(NotClassy actual) {
      super(actual, NotClassyAssert.class);
    }

    public NotClassyAssert isClassy() {
      throw new AssertionError("Sorry I'm not classy!");
    }

    public NotClassyAssert isNotClassy() {
      return this;
    }
  }

  static class ClassyAssert extends AbstractAssert<ClassyAssert, Classy> {
    public ClassyAssert(Classy actual) {
      super(actual, ClassyAssert.class);
    }

    public ClassyAssert isClassy() {
      return this;
    }

    public ClassyAssert isNotClassy() {
      throw new AssertionError("Hey I'm classy!");
    }
  }

  interface Class1SoftAssertions extends SoftAssertionsProvider {
    default NotClassyAssert assertThat(NotClassy actual) {
      return proxy(NotClassyAssert.class, NotClassy.class, actual);
    }
  }

  interface Class2SoftAssertions extends SoftAssertionsProvider {
    default ClassyAssert assertThat(Classy actual) {
      return proxy(ClassyAssert.class, Classy.class, actual);
    }
  }

  static class UberSoftAssertions extends AbstractSoftAssertions
      implements Class1SoftAssertions, Class2SoftAssertions, StandardSoftAssertionsProvider {
  }

  private final NotClassy notClassy = new NotClassy();
  private final Classy classy = new Classy();
  private final UberSoftAssertions softly = new UberSoftAssertions();

  @Test
  void should_return_an_empty_list_of_errors_for_passing_assertions() {
    // GIVEN succesfull assertions
    softly.assertThat(notClassy).isNotClassy();
    softly.assertThat(classy).isClassy();
    softly.assertThat("String").isEqualTo("String");
    // WHEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    // THEN
    assertThat(errorsCollected).isEqualTo(softly.assertionErrorsCollected())
                               .isEmpty();
  }

  @Test
  void should_return_errors_in_the_order_they_were_asserted() {
    // GIVEN
    softly.assertThat(notClassy).isClassy();
    softly.assertThat(classy).isNotClassy();
    softly.assertThat("String").isEqualTo("Strung");
    softly.assertThat("String").isEqualTo("String"); // No errors to collect
    // WHEN
    List<Throwable> errorsCollected = softly.errorsCollected();
    // THEN
    assertThat(errorsCollected).isEqualTo(softly.assertionErrorsCollected())
                               .hasSize(3);
    assertThat(errorsCollected.get(0)).as("0").hasMessage("Sorry I'm not classy!");
    assertThat(errorsCollected.get(1)).as("1").hasMessage("Hey I'm classy!");
    assertThat(errorsCollected.get(2)).as("2").hasMessage(shouldBeEqualMessage("\"String\"", "\"Strung\""));
  }
}
