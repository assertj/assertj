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
package org.assertj.core.api;

import static java.lang.Class.forName;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.from;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.mockito.AdditionalAnswers.delegatesTo;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.List;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.assertj.core.api.AssertFactory.ValueProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Stefano Cordio
 */
@ExtendWith(MockitoExtension.class)
class InstanceOfAssertFactoryTest {

  @Mock
  private AbstractAssert<?, ?> abstractAssert;

  @Nested
  class With_Class {

    private InstanceOfAssertFactory<Integer, AbstractAssert<?, ?>> underTest;

    @Mock
    private AssertFactory<Integer, AbstractAssert<?, ?>> delegate;

    @BeforeEach
    void setUp() {
      underTest = new InstanceOfAssertFactory<>(Integer.class, delegate);
    }

    @Test
    void constructor_should_fail_if_type_is_null() {
      // WHEN
      Throwable thrown = catchThrowable(() -> new InstanceOfAssertFactory<>(null, delegate));
      // THEN
      then(thrown).isInstanceOf(NullPointerException.class)
                  .hasMessage(shouldNotBeNull("type").create());
    }

    @Test
    void constructor_should_fail_if_delegate_is_null() {
      // WHEN
      Throwable thrown = catchThrowable(() -> new InstanceOfAssertFactory<>(Object.class, null));
      // THEN
      then(thrown).isInstanceOf(NullPointerException.class)
                  .hasMessage(shouldNotBeNull("delegate").create());
    }

    @Test
    void getRawClass_should_return_given_class() {
      // WHEN
      Class<Integer> result = underTest.getRawClass();
      // THEN
      then(result).isEqualTo(Integer.class);
    }

    @Test
    void createAssert_should_return_assert_factory_result_if_actual_is_an_instance_of_given_type() {
      // GIVEN
      int actual = 0;
      willReturn(abstractAssert).given(delegate).createAssert(actual);
      // WHEN
      Assert<?, ?> result = underTest.createAssert(actual);
      // THEN
      then(result).isSameAs(abstractAssert);
    }

    @Test
    void createAssert_should_throw_assertion_error_if_actual_is_not_an_instance_of_given_type() {
      // GIVEN
      String actual = "string";
      // WHEN
      Throwable throwable = catchThrowable(() -> underTest.createAssert(actual));
      // THEN
      then(throwable).isInstanceOf(ClassCastException.class)
                     .hasMessage("Cannot cast %s to %s", actual.getClass().getName(), underTest.getRawClass().getName());
    }

    @Test
    void createAssert_with_ValueProvider_should_return_assert_factory_result_if_provided_value_is_an_instance_of_given_type() {
      // GIVEN
      int actual = 0;
      ValueProvider<Integer> valueProvider = mockThatDelegatesTo(type -> actual);
      willReturn(abstractAssert).given(delegate).createAssert(actual);
      // WHEN
      Assert<?, ?> result = underTest.createAssert(valueProvider);
      // THEN
      then(result).isSameAs(abstractAssert);
      verify(valueProvider).apply(Integer.class);
    }

    @Test
    void createAssert_with_ValueProvider_should_throw_assertion_error_if_provided_value_is_not_an_instance_of_given_type() {
      // GIVEN
      String actual = "string";
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      Throwable throwable = catchThrowable(() -> underTest.createAssert(valueProvider));
      // THEN
      then(throwable).isInstanceOf(ClassCastException.class)
                     .hasMessage("Cannot cast %s to %s", actual.getClass().getName(), underTest.getRawClass().getName());
    }

    @Test
    void toString_should_return_expected_value() {
      // WHEN
      String result = underTest.toString();
      // THEN
      then(result).isEqualTo("InstanceOfAssertFactory for %s", Integer.class.getTypeName());
    }

  }

  @Nested
  class With_Class_and_Type_array {

    @SuppressWarnings("rawtypes")
    private InstanceOfAssertFactory<List, AbstractAssert<?, ?>> underTest;

    @SuppressWarnings("rawtypes")
    @Mock
    private AssertFactory<List, AbstractAssert<?, ?>> delegate;

    @BeforeEach
    void setUp() {
      underTest = new InstanceOfAssertFactory<>(List.class, new Class[] { Integer.class }, delegate);
    }

    @Test
    void constructor_should_fail_if_rawClass_is_null() {
      // WHEN
      Throwable thrown = catchThrowable(() -> new InstanceOfAssertFactory<>(null, new Class[] { Integer.class }, delegate));
      // THEN
      then(thrown).isInstanceOf(NullPointerException.class)
                  .hasMessage(shouldNotBeNull("rawClass").create());
    }

    @Test
    void constructor_should_fail_if_typeArguments_is_null() {
      // WHEN
      Throwable thrown = catchThrowable(() -> new InstanceOfAssertFactory<>(List.class, null, delegate));
      // THEN
      then(thrown).isInstanceOf(NullPointerException.class)
                  .hasMessage(shouldNotBeNull("typeArguments").create());
    }

    @Test
    void constructor_should_fail_if_delegate_is_null() {
      // WHEN
      Throwable thrown = catchThrowable(() -> new InstanceOfAssertFactory<>(List.class, new Class[] { Integer.class }, null));
      // THEN
      then(thrown).isInstanceOf(NullPointerException.class)
                  .hasMessage(shouldNotBeNull("delegate").create());
    }

    @SuppressWarnings("rawtypes")
    @Test
    void getRawClass_should_return_given_raw_class() {
      // WHEN
      Class<List> result = underTest.getRawClass();
      // THEN
      then(result).isEqualTo(List.class);
    }

    @Test
    void createAssert_should_return_assert_factory_result_if_actual_is_an_instance_of_given_type() {
      // GIVEN
      List<Integer> actual = Collections.emptyList();
      willReturn(abstractAssert).given(delegate).createAssert(actual);
      // WHEN
      Assert<?, ?> result = underTest.createAssert(actual);
      // THEN
      then(result).isSameAs(abstractAssert);
    }

    @Test
    void createAssert_should_throw_assertion_error_if_actual_is_not_an_instance_of_given_type() {
      // GIVEN
      String actual = "string";
      // WHEN
      Throwable throwable = catchThrowable(() -> underTest.createAssert(actual));
      // THEN
      then(throwable).isInstanceOf(ClassCastException.class)
                     .hasMessage("Cannot cast %s to %s", actual.getClass().getName(), underTest.getRawClass().getName());
    }

    @Test
    void createAssert_with_ValueProvider_should_return_assert_factory_result_if_provided_value_is_an_instance_of_given_type() {
      // GIVEN
      List<Integer> actual = Collections.emptyList();
      ValueProvider<List<Integer>> valueProvider = mockThatDelegatesTo(type -> actual);
      willReturn(abstractAssert).given(delegate).createAssert(actual);
      // WHEN
      Assert<?, ?> result = underTest.createAssert(valueProvider);
      // THEN
      then(result).isSameAs(abstractAssert);
      verify(valueProvider).apply(parameterizedType(List.class, Integer.class));
    }

    @Test
    void createAssert_with_ValueProvider_should_throw_assertion_error_if_provided_value_is_not_an_instance_of_given_type() {
      // GIVEN
      String actual = "string";
      ValueProvider<?> valueProvider = mockThatDelegatesTo(type -> actual);
      // WHEN
      Throwable throwable = catchThrowable(() -> underTest.createAssert(valueProvider));
      // THEN
      then(throwable).isInstanceOf(ClassCastException.class)
                     .hasMessage("Cannot cast %s to %s", actual.getClass().getName(), underTest.getRawClass().getName());
    }

    @Test
    void toString_should_return_expected_value() {
      // WHEN
      String result = underTest.toString();
      // THEN
      then(result).isEqualTo("InstanceOfAssertFactory for %s<%s>", List.class.getTypeName(), Integer.class.getTypeName());
    }

  }

  @Nested
  class SyntheticParameterizedTypeTest {

    private final Class<?> underTest = forName(InstanceOfAssertFactory.class.getName() + "$SyntheticParameterizedType");

    SyntheticParameterizedTypeTest() throws ClassNotFoundException {}

    @Test
    void should_be_private() {
      // WHEN/THEN
      assertThat(underTest).isPrivate();
    }

    @Test
    void should_honor_equals_contract() {
      // WHEN/THEN
      EqualsVerifier.forClass(underTest)
                    .withNonnullFields("rawClass", "typeArguments")
                    .verify();
    }

  }

  @SuppressWarnings("unchecked")
  @SafeVarargs
  private static <T> T mockThatDelegatesTo(T delegate, T... reified) {
    if (reified.length > 0) {
      throw new IllegalArgumentException("NioJava will detect class automagically.");
    }
    return mock((Class<T>) reified.getClass().getComponentType(), delegatesTo(delegate));
  }

  private static ParameterizedType parameterizedType(Class<?> rawClass, Class<?>... typeArguments) {
    return argThat(argument -> {
      assertThat(argument).returns(typeArguments, from(ParameterizedType::getActualTypeArguments))
                          .returns(rawClass, from(ParameterizedType::getRawType))
                          .returns(null, from(ParameterizedType::getOwnerType));
      return true;
    });
  }

}
