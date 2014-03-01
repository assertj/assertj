package org.assertj.core.internal.files;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.FilesBaseTest;
import org.junit.Test;

import static org.assertj.core.error.ShouldHaveName.shouldHaveName;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Files#assertHasName(org.assertj.core.api.AssertionInfo, java.io.File, String)} </code>
 * .
 * 
 * @author Jean-Christophe Gay
 */
public class Files_assertHasName_Test extends FilesBaseTest {

  private String expectedName = "expected.name";

  @Test
  public void should_throw_error_if_actual_is_null() throws Exception {
    thrown.expectAssertionError(actualIsNull());
    files.assertHasName(someInfo(), null, expectedName);
  }

  @Test
  public void should_throw_npe_if_name_is_null() throws Exception {
    thrown.expectNullPointerException("The expected name should not be null.");
    files.assertHasName(someInfo(), actual, null);
  }

  @Test
  public void should_throw_error_if_actual_does_not_have_the_expected_name() throws Exception {
    AssertionInfo info = someInfo();
    when(actual.getName()).thenReturn("not.expected.name");
    try {
      files.assertHasName(info, actual, expectedName);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveName(actual, expectedName));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_has_expected_extension() throws Exception {
    when(actual.getName()).thenReturn(expectedName);
    files.assertHasName(someInfo(), actual, expectedName);
  }
}
