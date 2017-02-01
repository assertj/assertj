/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.paths;

import static java.lang.String.format;
import static java.nio.charset.Charset.defaultCharset;
import static org.assertj.core.error.ShouldBeReadable.shouldBeReadable;
import static org.assertj.core.error.ShouldExist.shouldExist;
import static org.assertj.core.error.ShouldHaveSameContent.shouldHaveSameContent;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.exception.RuntimeIOException;
import org.assertj.core.internal.Paths;
import org.assertj.core.util.diff.Delta;
import org.junit.Test;

/**
 * Tests for <code>{@link Paths#assertHasSameContentAs(AssertionInfo, Path, Path)}</code>.
 */
public class Paths_assertHasSameContentAs_Test extends MockPathsBaseTest {

  @Test
  public void should_pass_if_path_has_same_content_as_other() throws IOException {
	when(diff.diff(actual, defaultCharset(), other, defaultCharset())).thenReturn(new ArrayList<Delta<String>>());
	when(nioFilesWrapper.exists(actual)).thenReturn(true);
	when(nioFilesWrapper.isReadable(actual)).thenReturn(true);
	when(nioFilesWrapper.isReadable(other)).thenReturn(true);
	paths.assertHasSameContentAs(someInfo(), actual, defaultCharset(), other, defaultCharset());
  }

  @Test
  public void should_throw_error_if_other_is_null() {
	thrown.expectNullPointerException("The given Path to compare actual content to should not be null");
	paths.assertHasSameContentAs(someInfo(), actual, defaultCharset(), null, defaultCharset());
  }

  @Test
  public void should_fail_if_actual_is_null() {
	thrown.expectAssertionError(actualIsNull());
	when(nioFilesWrapper.isReadable(other)).thenReturn(true);
	paths.assertHasSameContentAs(someInfo(), null, defaultCharset(), other, defaultCharset());
  }

  @Test
  public void should_fail_if_actual_path_does_not_exist() {
	AssertionInfo info = someInfo();
	when(nioFilesWrapper.exists(actual)).thenReturn(false);
	when(nioFilesWrapper.isReadable(other)).thenReturn(true);
	try {
	  paths.assertHasSameContentAs(info, actual, defaultCharset(), other, defaultCharset());
	} catch (AssertionError e) {
	  verify(failures).failure(info, shouldExist(actual));
	  return;
	}
	failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_not_a_readable_file() {
	AssertionInfo info = someInfo();
	when(nioFilesWrapper.exists(actual)).thenReturn(true);
	when(nioFilesWrapper.isReadable(actual)).thenReturn(false);
	when(nioFilesWrapper.isReadable(other)).thenReturn(true);
	try {
	  paths.assertHasSameContentAs(info, actual, defaultCharset(), other, defaultCharset());
	} catch (AssertionError e) {
	  verify(failures).failure(info, shouldBeReadable(actual));
	  return;
	}
	failBecauseExpectedAssertionErrorWasNotThrown();
  }
  
  @Test
  public void should_fail_if_other_is_not_a_readable_file() {
    when(nioFilesWrapper.isReadable(other)).thenReturn(false);

    thrown.expectIllegalArgumentException(format("The given Path <%s> to compare actual content to should be readable", other));

    paths.assertHasSameContentAs(someInfo(), actual, defaultCharset(), other, defaultCharset());
  }
  
  @Test
  public void should_throw_error_wrapping_catched_IOException() throws IOException {
	IOException cause = new IOException();
	when(diff.diff(actual, defaultCharset(), other, defaultCharset())).thenThrow(cause);
	when(nioFilesWrapper.exists(actual)).thenReturn(true);
	when(nioFilesWrapper.isReadable(actual)).thenReturn(true);
	when(nioFilesWrapper.isReadable(other)).thenReturn(true);

    thrown.expectWithCause(RuntimeIOException.class, cause);

    paths.assertHasSameContentAs(someInfo(), actual, defaultCharset(), other, defaultCharset());
  }

  @Test
  public void should_fail_if_actual_and_given_path_does_not_have_the_same_content() throws IOException {
    @SuppressWarnings("unchecked")
    List<Delta<String>> diffs = newArrayList((Delta<String>) mock(Delta.class));
	when(diff.diff(actual, defaultCharset(), other, defaultCharset())).thenReturn(diffs);
	when(nioFilesWrapper.exists(actual)).thenReturn(true);
	when(nioFilesWrapper.isReadable(actual)).thenReturn(true);
	when(nioFilesWrapper.isReadable(other)).thenReturn(true);
	AssertionInfo info = someInfo();
	try {
	  paths.assertHasSameContentAs(info, actual, defaultCharset(), other, defaultCharset());
	} catch (AssertionError e) {
	  verify(failures).failure(info, shouldHaveSameContent(actual, other, diffs));
	  return;
	}
	failBecauseExpectedAssertionErrorWasNotThrown();
  }
}


