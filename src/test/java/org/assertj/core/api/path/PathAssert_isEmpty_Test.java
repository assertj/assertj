package org.assertj.core.api.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import org.assertj.core.api.PathAssert;
import org.assertj.core.api.PathAssertBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.TempFileUtil.createTempPathWithContent;
import static org.assertj.core.util.TempFileUtil.createTempPathWithNoContent;

import java.io.IOException;
import java.nio.file.Path;

import static org.mockito.Mockito.verify;

/**
 * Test for <code>{@link PathAssert#isEmpty()}</code>.
 */
@DisplayName("PathAssert isEmpty")
class PathAssert_isEmpty_Test extends PathAssertBaseTest {

  @Override
  protected PathAssert invoke_api_method() {
    return assertions.isEmpty();
  }

  @Override
  protected void verify_internal_effects() {
    verify(paths).assertIsEmptyFile(getInfo(assertions), getActual(assertions));
  }

  @Test
  void should_pass_if_file_has_no_content() throws IOException {
    //GIVEN
    Path actual = createTempPathWithNoContent();
    //WHEN/THEN
    then(actual).isEmpty();
  }

  @Test
  void should_fail_if_file_has_content() throws IOException {
    //GIVEN
    Path actual = createTempPathWithContent("Coffee", defaultCharset);
    //WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isEmpty());
    // THEN
    then(assertionError).hasMessage(shouldBeEmpty(actual).create());
  }

}
