/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal.urls;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.uri.ShouldHavePath.shouldHavePath;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.net.URI;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.UrisBaseTest;
import org.junit.jupiter.api.Test;

public class Uris_assertHasPath_Test extends UrisBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> uris.assertHasPath(info, null, "path"))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_actual_uri_has_the_given_path() {
    uris.assertHasPath(info, URI.create("http://example.com/pages/"), "/pages/");
    uris.assertHasPath(info, URI.create("http://example.com"), "");
  }

  @Test
  public void should_pass_if_actual_uri_has_no_path_and_the_given_path_is_null() {
    uris.assertHasPath(info, URI.create("mailto:java-net@java.sun.com"), null);
  }

  @Test
  public void should_fail_if_actual_URI_path_is_not_the_given_path() {
    AssertionInfo info = someInfo();
    URI uri = URI.create("http://example.com/pages/");
    String expectedPath = "/news/";

    Throwable error = catchThrowable(() -> uris.assertHasPath(info, uri, expectedPath));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHavePath(uri, expectedPath));
  }

  @Test
  public void should_fail_if_actual_URI_has_path_and_the_given_path_null() {
    AssertionInfo info = someInfo();
    URI uri = URI.create("http://example.com/pages/");
    String expectedPath = null;

    Throwable error = catchThrowable(() -> uris.assertHasPath(info, uri, expectedPath));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHavePath(uri, expectedPath));
  }

  @Test
  public void should_fail_if_actual_URI_has_no_path_and_the_given_path_is_not_null() {
    AssertionInfo info = someInfo();
    URI uri = URI.create("mailto:java-net@java.sun.com");
    String expectedPath = "";

    Throwable error = catchThrowable(() -> uris.assertHasPath(info, uri, expectedPath));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHavePath(uri, expectedPath));
  }
}
