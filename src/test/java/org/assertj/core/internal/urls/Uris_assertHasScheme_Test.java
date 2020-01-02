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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal.urls;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.uri.ShouldHaveScheme.shouldHaveScheme;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.net.URI;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.UrisBaseTest;
import org.junit.jupiter.api.Test;

public class Uris_assertHasScheme_Test extends UrisBaseTest {

  @Test
  public void should_pass_if_actual_uri_has_the_given_scheme() {
    uris.assertHasScheme(info, URI.create("http://example.com/pages/"), "http");
    uris.assertHasScheme(info, URI.create("example.com/pages/"), null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> uris.assertHasScheme(info, null, "http"))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_scheme_is_not_the_expected_scheme() {
    AssertionInfo info = someInfo();
    URI uri = URI.create("http://example.com/pages/");
    String expectedScheme = "ftp";

    Throwable error = catchThrowable(() -> uris.assertHasScheme(info, uri, expectedScheme));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveScheme(uri, expectedScheme));
  }

}
