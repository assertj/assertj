package org.assertj.core.internal.files;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.FilesBaseTest;
import org.junit.Test;

import java.io.File;

import static org.assertj.core.error.ShouldHaveParent.shouldHaveParent;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Files#assertHasParent(org.assertj.core.api.AssertionInfo, java.io.File, java.io.File)}</code>
 * .
 * 
 * @author Jean-Christophe Gay
 */
public class Files_assertHasParent_Test extends FilesBaseTest {

  private File expectedParent = mock(File.class);

  @Test
  public void should_throw_error_if_actual_is_null() throws Exception {
    thrown.expectAssertionError(actualIsNull());
    files.assertHasParent(someInfo(), null, expectedParent);
  }

  @Test
  public void should_throw_npe_if_expected_is_null() throws Exception {
    thrown.expectNullPointerException("The expected parent file should not be null.");
    files.assertHasParent(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_has_no_parent() throws Exception {
    AssertionInfo info = someInfo();
    when(actual.getParentFile()).thenReturn(null);
    try {
      files.assertHasParent(info, actual, expectedParent);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveParent(actual, expectedParent));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_have_the_expected_parent() throws Exception {
    AssertionInfo info = someInfo();
    File notExpectedParent = mock(File.class);
    when(actual.getParentFile()).thenReturn(notExpectedParent);
    try {
      files.assertHasParent(info, actual, expectedParent);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveParent(actual, expectedParent));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_has_expected_parent() throws Exception {
    when(actual.getParentFile()).thenReturn(expectedParent);
    files.assertHasParent(someInfo(), actual, expectedParent);
  }
}
