package org.assertj.core.internal.throwables;

import static org.assertj.core.error.ShouldHaveCauseExactlyInstance.shouldHaveCauseExactlyInstance;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ThrowablesBaseTest;
import org.junit.Test;

/**
 * Tests for
 * {@link org.assertj.core.internal.Throwables#assertHasCauseExactlyInstanceOf(org.assertj.core.api.AssertionInfo, Throwable, Class)}
 * .
 * 
 * @author Jean-Christophe Gay
 */
public class Throwables_assertHasCauseExactlyInstanceOf_Test extends ThrowablesBaseTest {

  private Throwable error = new Throwable(new IllegalArgumentException());

  @Test
  public void should_pass_if_cause_is_exactly_instance_of_expected_type() throws Exception {
    throwables.assertHasCauseExactlyInstanceOf(someInfo(), error, IllegalArgumentException.class);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    throwables.assertHasCauseExactlyInstanceOf(someInfo(), null, IllegalArgumentException.class);
  }

  @Test
  public void should_throw_npe_if_given_type_is_null() throws Exception {
    thrown.expectNullPointerException("The given type should not be null");
    throwables.assertHasCauseExactlyInstanceOf(someInfo(), error, null);
  }

  @Test
  public void should_fail_if_actual_has_no_cause() throws Exception {
    AssertionInfo info = someInfo();
    Class<NullPointerException> expectedCauseType = NullPointerException.class;
    try {
      throwables.assertHasCauseExactlyInstanceOf(info, actual, expectedCauseType);
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldHaveCauseExactlyInstance(actual, expectedCauseType));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }  

  @Test
  public void should_fail_if_cause_is_not_instance_of_expected_type() throws Exception {
    AssertionInfo info = someInfo();
    Class<NullPointerException> expectedCauseType = NullPointerException.class;
    try {
      throwables.assertHasCauseExactlyInstanceOf(info, error, expectedCauseType);
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldHaveCauseExactlyInstance(error, expectedCauseType));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_cause_is_not_exactly_instance_of_expected_type() throws Exception {
    AssertionInfo info = someInfo();
    Class<RuntimeException> expectedCauseType = RuntimeException.class;
    try {
      throwables.assertHasCauseExactlyInstanceOf(info, error, expectedCauseType);
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldHaveCauseExactlyInstance(error, expectedCauseType));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
