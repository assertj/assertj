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
import static org.assertj.core.error.uri.ShouldHaveQuery.shouldHaveQuery;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.net.URI;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.UrisBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Uris#assertHasQuery(org.assertj.core.api.AssertionInfo, java.net.URI, String)}  </code>
 * .
 *
 * @author Alexander Bischof
 */
public class Uris_assertHasQuery_Test extends UrisBaseTest {

  @Test
  public void should_pass_if_actual_uri_has_the_expected_query() {
    uris.assertHasQuery(info, URI.create("http://www.helloworld.org/index.html?type=test"), "type=test");
  }

  @Test
  public void should_pass_if_actual_uri_has_no_query_and_given_is_null() {
    uris.assertHasQuery(info, URI.create("http://www.helloworld.org/index.html"), null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> uris.assertHasQuery(info, null, "http://www.helloworld.org/index.html?type=test"))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_URI_query_is_not_the_given_query() {
    AssertionInfo info = someInfo();
    URI uri = URI.create("http://assertj.org/news?type=beta");
    String expectedQuery = "type=final";

    Throwable error = catchThrowable(() -> uris.assertHasQuery(info, uri, expectedQuery));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveQuery(uri, expectedQuery));
  }

  @Test
  public void should_fail_if_actual_URI_has_no_query_and_expected_query_is_not_null() {
    AssertionInfo info = someInfo();
    URI uri = URI.create("http://assertj.org/news");
    String expectedQuery = "type=final";

    Throwable error = catchThrowable(() -> uris.assertHasQuery(info, uri, expectedQuery));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveQuery(uri, expectedQuery));
  }

  @Test
  public void should_fail_if_actual_URI_has_a_query_and_expected_query_is_null() {
    AssertionInfo info = someInfo();
    URI uri = URI.create("http://assertj.org/news?type=beta");
    String expectedQuery = null;

    Throwable error = catchThrowable(() -> uris.assertHasQuery(info, uri, expectedQuery));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveQuery(uri, expectedQuery));
  }
}
