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
package org.assertj.core.api.object;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertBaseTest;
import org.assertj.core.presentation.UnicodeRepresentation;
import org.assertj.core.test.Jedi;
import org.assertj.core.test.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests for <code>{@link AbstractObjectAssert#asInstanceOf(Class)}</code>.
 *
 * @author Stefano Cordio
 */
class ObjectAssert_asInstanceOf_with_class_Test extends ObjectAssertBaseTest {

  @Override
  protected ObjectAssert<Jedi> invoke_api_method() {
    assertions.asInstanceOf(Person.class);
    return null;
  }

  @Override
  protected void verify_internal_effects() {
    verify(objects).assertIsInstanceOf(getInfo(assertions), getActual(assertions), Person.class);
  }

  @Override
  public void should_return_this() {
    // Test disabled since asInstanceOf(Class) does not return this.
  }

  @Test
  void should_throw_npe_if_no_type_is_given() {
    // GIVEN
    Class<?> type = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertions.asInstanceOf(type));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("type").create());
  }

  @Test
  void should_return_new_object_assert_with_updated_actual_type() {
    // WHEN
    AbstractAssert<?, Person> result = assertions.asInstanceOf(Person.class);
    // THEN
    then(result).isInstanceOf(AbstractObjectAssert.class);
  }

  @Test
  void should_keep_existing_assertion_state() {
    // GIVEN
    assertions.as("description")
              .overridingErrorMessage("error message")
              .withRepresentation(new UnicodeRepresentation());
    // WHEN
    AbstractAssert<?, ?> result = assertions.asInstanceOf(Person.class);
    // THEN
    then(result).hasFieldOrPropertyWithValue("objects", objects)
                .extracting(AbstractAssert::getWritableAssertionInfo)
                .isEqualToComparingFieldByField(getInfo(assertions));
  }

}
