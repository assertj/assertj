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
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.uri.ShouldBeEqualToWithSortedQueryParameters.shouldBeEqualToWithSortedQueryParameters;
import static org.assertj.core.test.TestData.someInfo;
import static org.mockito.Mockito.verify;

import java.net.MalformedURLException;
import java.net.URL;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.UrlsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Urls#assertIsEqualToWithSortedQueryParameters(AssertionInfo, URL, URL)} (org.assertj.core.api.AssertionInfo, java.net.URL, String)}  </code>
 * .
 *
 * @author SUN Ting
 */

public class Urls_assertIsEqualToWithSortedQueryParameters_Test extends UrlsBaseTest {

  @Test
  public void should_pass_if_only_query_parameters_orders_are_different() throws MalformedURLException {
    URL actual = new URL("https://example.com/path/to/page?color=purple&name=ferret#hello");
    URL expected = new URL("https://example.com/path/to/page?name=ferret&color=purple#hello");
    urls.assertIsEqualToWithSortedQueryParameters(info, actual, expected);
  }

  @Test
  public void should_pass_if_both_expected_and_actual_have_query_and_they_are_literally_equivalent()
    throws MalformedURLException {
    URL actual = new URL("https://example.com/path/to/page?color=purple&name=ferret#hello");
    URL expected = new URL("https://example.com/path/to/page?color=purple&name=ferret#hello");
    urls.assertIsEqualToWithSortedQueryParameters(info, actual, expected);
  }

  @Test
  public void should_pass_if_both_expected_and_actual_have_no_query_and_they_are_literally_equivalent()
    throws MalformedURLException {
    URL actual = new URL("https://example.com/path/to/page#hello");
    URL expected = new URL("https://example.com/path/to/page#hello");
    urls.assertIsEqualToWithSortedQueryParameters(info, actual, expected);
  }

  @Test
  public void should_fail_if_sorted_query_parameters_are_different() throws MalformedURLException {
    AssertionInfo info = someInfo();
    URL actual = new URL("https://example.com/path/to/page?color=purple&name=ferret#hello");
    URL expected = new URL("https://example.com/path/to/page?color=red&name=jackson#hello");

    Throwable error = catchThrowable(() -> urls.assertIsEqualToWithSortedQueryParameters(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqualToWithSortedQueryParameters(actual, expected));
  }

  @Test
  public void should_fail_if_domains_are_different() throws MalformedURLException {
    AssertionInfo info = someInfo();
    URL actual = new URL("https://example.com/path/to/page?color=red&name=jackson#hello");
    URL expected = new URL("https://example2.com/path/to/page?color=red&name=jackson#hello");

    Throwable error = catchThrowable(() -> urls.assertIsEqualToWithSortedQueryParameters(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqualToWithSortedQueryParameters(actual, expected));
  }

  @Test
  public void should_fail_if_fragments_are_different() throws MalformedURLException {
    AssertionInfo info = someInfo();
    URL actual = new URL("https://example.com/path/to/page?color=red&name=jackson#hello");
    URL expected = new URL("https://example.com/path/to/page?color=red&name=jackson#world");

    Throwable error = catchThrowable(() -> urls.assertIsEqualToWithSortedQueryParameters(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqualToWithSortedQueryParameters(actual, expected));
  }

  @Test
  public void should_fail_if_actual_has_no_query_and_expected_has_query() throws MalformedURLException {
    AssertionInfo info = someInfo();
    URL actual = new URL("https://example.com/path/to/page#hello");
    URL expected = new URL("https://example.com/path/to/page?color=red&name=jackson#hello");

    Throwable error = catchThrowable(() -> urls.assertIsEqualToWithSortedQueryParameters(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqualToWithSortedQueryParameters(actual, expected));
  }

  @Test
  public void should_fail_if_actual_has_query_and_expected_has_no_query() throws MalformedURLException {
    AssertionInfo info = someInfo();
    URL actual = new URL("https://example.com/path/to/page?color=red&name=jackson#hello");
    URL expected = new URL("https://example.com/path/to/page#hello");

    Throwable error = catchThrowable(() -> urls.assertIsEqualToWithSortedQueryParameters(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqualToWithSortedQueryParameters(actual, expected));
  }
}
