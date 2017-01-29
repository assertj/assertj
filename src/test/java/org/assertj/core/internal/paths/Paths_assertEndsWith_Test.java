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

import static org.assertj.core.error.ShouldEndWithPath.shouldEndWith;
import static org.assertj.core.test.TestFailures.wasExpectingAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;

import org.assertj.core.api.exception.PathsException;
import org.junit.Test;

public class Paths_assertEndsWith_Test extends MockPathsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    paths.assertEndsWith(info, null, other);
  }

  @Test
  public void should_fail_if_other_is_null() {
    thrown.expectNullPointerException("the expected end path should not be null");
    paths.assertEndsWith(info, actual, null);
  }

  @Test
  public void should_fail_with_PathsException_if_actual_cannot_be_resolved() throws IOException {
	final IOException causeException = new IOException();
	when(actual.toRealPath()).thenThrow(causeException);

    thrown.expectWithCause(PathsException.class, "failed to resolve actual real path", causeException);

    paths.assertEndsWith(info, actual, other);
  }

  @Test
  public void should_fail_if_canonical_actual_does_not_end_with_normalized_other() throws IOException {
	final Path canonicalActual = mock(Path.class);
	final Path normalizedOther = mock(Path.class);

	when(actual.toRealPath()).thenReturn(canonicalActual);
	when(other.normalize()).thenReturn(normalizedOther);

	// This is the default, but...
	when(canonicalActual.endsWith(normalizedOther)).thenReturn(false);

	try {
	  paths.assertEndsWith(info, actual, other);
	  wasExpectingAssertionError();
	} catch (AssertionError e) {
	  verify(failures).failure(info, shouldEndWith(actual, other));
	}
  }

  @Test
  public void should_succeed_if_canonical_actual_ends_with_normalized_other() throws IOException {
	final Path canonicalActual = mock(Path.class);
	final Path normalizedOther = mock(Path.class);

	when(actual.toRealPath()).thenReturn(canonicalActual);
	when(other.normalize()).thenReturn(normalizedOther);

	// We want the canonical versions to be compared, not the arguments
	when(canonicalActual.endsWith(normalizedOther)).thenReturn(true);

	paths.assertEndsWith(info, actual, other);
  }
}
