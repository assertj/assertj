package org.assertj.core.api.path;

import org.assertj.core.api.PathAssert;
import org.assertj.core.api.PathAssertBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.TempFileUtil.createTempPathWithContent;
import static org.assertj.core.util.TempFileUtil.createTempPathWithNoContent;
import static org.mockito.Mockito.verify;

/**
 * Test for <code>{@link PathAssert#isNotEmpty()}</code>.
 */
@DisplayName("PathAssert isNotEmpty")
class PathAssert_isNotEmpty_Test extends PathAssertBaseTest {

  @Override
  protected PathAssert invoke_api_method() {
    return assertions.isNotEmpty();
  }

  @Override
  protected void verify_internal_effects() {
    verify(paths).assertIsNotEmptyFile(getInfo(assertions), getActual(assertions));
  }

  @Test
  void should_pass_if_file_has_content() throws IOException {
    //GIVEN
    Path actual = createTempPathWithContent("Coffee", defaultCharset);
    //WHEN/THEN
    then(actual).isNotEmpty();
  }

  @Test
  void should_fail_if_file_has_no_content() throws IOException {
    //GIVEN
    Path actual = createTempPathWithNoContent();
    //WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isNotEmpty());
    // THEN
    then(assertionError).hasMessage(shouldNotBeEmpty(actual.toFile()).create());
  }

}
