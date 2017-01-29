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

import static org.assertj.core.error.ShouldStartWithPath.shouldStartWith;
import static org.assertj.core.test.TestFailures.wasExpectingAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;

import org.assertj.core.api.exception.PathsException;
import org.junit.Before;
import org.junit.Test;

public class Paths_assertStartsWith_Test extends MockPathsBaseTest {

  private Path canonicalActual;
  private Path canonicalOther;

  @Before
  public void init() {
	super.init();
	canonicalActual = mock(Path.class);
	canonicalOther = mock(Path.class);
  }

  @Test
  public void should_fail_if_actual_is_null() {
	thrown.expectAssertionError(actualIsNull());
	paths.assertStartsWith(info, null, other);
  }

  @Test
  public void should_fail_if_other_is_null() {
    thrown.expectNullPointerException("the expected start path should not be null");
    paths.assertStartsWith(info, actual, null);
  }

  @Test
  public void should_throw_PathsException_if_actual_cannot_be_resolved() throws IOException
  {
	final IOException exception = new IOException();
	when(actual.toRealPath()).thenThrow(exception);

    thrown.expectWithCause(PathsException.class, "failed to resolve actual real path", exception);

    paths.assertStartsWith(info, actual, other);
  }

  @Test
  public void should_throw_PathsException_if_other_cannot_be_resolved() throws IOException {
	final IOException exception = new IOException();
	when(actual.toRealPath()).thenReturn(canonicalActual);
	when(other.toRealPath()).thenThrow(exception);

    thrown.expectWithCause(PathsException.class, "failed to resolve argument real path", exception);

    paths.assertStartsWith(info, actual, other);
  }

  @Test
  public void should_fail_if_actual_does_not_start_with_other() throws IOException {
	when(actual.toRealPath()).thenReturn(canonicalActual);
	when(other.toRealPath()).thenReturn(canonicalOther);
	// This is the default, but let's make this explicit
	when(canonicalActual.startsWith(canonicalOther)).thenReturn(false);

	try {
	  paths.assertStartsWith(info, actual, other);
	  wasExpectingAssertionError();
	} catch (AssertionError e) {
	  verify(failures).failure(info, shouldStartWith(actual, other));
	}
  }

  @Test
  public void should_succeed_if_actual_starts_with_other() throws IOException {
	when(actual.toRealPath()).thenReturn(canonicalActual);
	when(other.toRealPath()).thenReturn(canonicalOther);

	when(canonicalActual.startsWith(canonicalOther)).thenReturn(true);

	paths.assertStartsWith(info, actual, other);
  }

}
