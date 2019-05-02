package org.assertj.core.api;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.error.ShouldBeInstance.shouldBeInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class InstanceOfAssertFactoryTest {

  private InstanceOfAssertFactory<Integer, Assert<?, ?>> underTest;

  @Mock
  private AssertFactory<Integer, Assert<?, ?>> assertFactory;

  @Mock
  private Assert<?, ?> mockAssert;

  @BeforeEach
  public void setUp() {
    initMocks(this);
    willReturn(mockAssert).given(assertFactory).apply(any());
    underTest = new InstanceOfAssertFactory<>(Integer.class, assertFactory);
  }

  @Test
  public void should_throw_npe_if_no_type_is_given() {
    assertThatNullPointerException().isThrownBy(() -> new InstanceOfAssertFactory<>(null, assertFactory));
  }

  @Test
  public void should_throw_npe_if_no_assert_factory_is_given() {
    assertThatNullPointerException().isThrownBy(() -> new InstanceOfAssertFactory<>(Object.class, null));
  }

  @Test
  public void should_return_assert_factory_result_if_actual_is_an_instance_of_given_type() {
    // When
    Assert<?, ?> result = underTest.apply(0);

    // Then
    assertThat(result).isSameAs(mockAssert);
  }

  @Test
  public void should_throw_assertion_error_if_actual_is_not_an_instance_of_given_type() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> underTest.apply("string"))
                                                   .withMessage(shouldBeInstance("string", Integer.class).create());
  }

}
