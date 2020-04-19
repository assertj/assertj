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
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for proxyable methods which change the object under assertion, providing a new assertion instance.
 *
 * @author Stefano Cordio
 */
public interface ProxyableObjectChangingMethodTest<ASSERT extends AbstractAssert<ASSERT, ?>>
    extends ObjectChangingMethodTest<ASSERT> {

  ASSERT getSoftAssertion(AbstractSoftAssertions softly);

  @ParameterizedTest
  @MethodSource("softAssertions")
  default void soft_assertions_should_succeed_after_changing_the_object_under_test(AbstractSoftAssertions softly) {
    // GIVEN
    ASSERT softAssertion1 = getSoftAssertion(softly);
    ASSERT softAssertion2 = getSoftAssertion(softly);
    // WHEN
    invoke_object_changing_method(softAssertion1).isNotEqualTo(UUID.randomUUID()); // should always succeed
    invoke_object_changing_method(softAssertion2).isNotSameAs(UUID.randomUUID()); // should always succeed
    // THEN
    softly.assertAll();
  }

  @ParameterizedTest
  @MethodSource("softAssertions")
  default void soft_assertions_should_collect_errors_after_changing_the_object_under_test(AbstractSoftAssertions softly) {
    // GIVEN
    ASSERT softAssertion1 = getSoftAssertion(softly).as("assertion 1")
                                                    .overridingErrorMessage("error message 1");
    ASSERT softAssertion2 = getSoftAssertion(softly).as("assertion 2")
                                                    .overridingErrorMessage("error message 2");
    // WHEN
    invoke_object_changing_method(softAssertion1).isEqualTo(UUID.randomUUID()); // should always fail
    invoke_object_changing_method(softAssertion2).isSameAs(UUID.randomUUID()); // should always fail
    // THEN
    then(softly.errorsCollected()).extracting(Throwable::getMessage)
                                  .containsExactly("[assertion 1] error message 1", "[assertion 2] error message 2");
  }

  static Stream<AbstractSoftAssertions> softAssertions() {
    return Stream.of(new SoftAssertions(), new BDDSoftAssertions());
  }

}
