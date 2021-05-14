package org.assertj.core.internal.files;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.FilesBaseTest;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeReadable.shouldBeReadable;
import static org.assertj.core.test.TestData.someInfo;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class Files_getFileContent_Test extends FilesBaseTest {
  @Test
  void should_fail_if_can_not_read() {
    when(actual.canRead()).thenReturn(false);
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> files.getFileContent(info, actual, Charset.defaultCharset()));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeReadable(actual));
  }
}
