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
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveParameter;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.assertj.core.internal.UrlsBaseTest;
import org.junit.jupiter.api.Test;

public class Urls_assertHasParameter_Test extends UrlsBaseTest {

  @Test
  public void should_fail_if_parameter_is_missing() throws MalformedURLException {
    URL url = new URL("http://assertj.org/news");
    String name = "article";

    Throwable error = catchThrowable(() -> urls.assertHasParameter(info, url, name));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveParameter(url, name));
  }

  @Test
  public void should_pass_if_parameter_has_no_value() throws MalformedURLException {
    urls.assertHasParameter(info, new URL("http://assertj.org/news?article"), "article");
  }

  @Test
  public void should_pass_if_parameter_has_value() throws MalformedURLException {
    urls.assertHasParameter(info, new URL("http://assertj.org/news?article=10"), "article");
  }

  @Test
  public void should_fail_if_parameter_without_value_is_missing() throws MalformedURLException {
    URL url = new URL("http://assertj.org/news");
    String name = "article";
    String expectedValue = null;

    Throwable error = catchThrowable(() -> urls.assertHasParameter(info, url, name, expectedValue));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveParameter(url, name, expectedValue));
  }

  @Test
  public void should_pass_if_parameter_without_value_is_found() throws MalformedURLException {
    urls.assertHasParameter(info, new URL("http://assertj.org/news?article"), "article", null);
  }

  @Test
  public void should_fail_if_parameter_without_value_has_value() throws MalformedURLException {
    URL url = new URL("http://assertj.org/news?article=11");
    String name = "article";
    String expectedValue = null;
    List<String> actualValues = newArrayList("11");

    Throwable error = catchThrowable(() -> urls.assertHasParameter(info, url, name, expectedValue));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveParameter(url, name, expectedValue, actualValues));
  }

  @Test
  public void should_fail_if_parameter_without_value_has_multiple_values() throws MalformedURLException {
    URL url = new URL("http://assertj.org/news?article=11&article=12");
    String name = "article";
    String expectedValue = null;
    List<String> actualValues = newArrayList("11", "12");

    Throwable error = catchThrowable(() -> urls.assertHasParameter(info, url, name, expectedValue));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveParameter(url, name, expectedValue, actualValues));
  }

  @Test
  public void should_fail_if_parameter_with_value_is_missing() throws MalformedURLException {
    URL url = new URL("http://assertj.org/news");
    String name = "article";
    String expectedValue = "10";

    Throwable error = catchThrowable(() -> urls.assertHasParameter(info, url, name, expectedValue));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveParameter(url, name, expectedValue));
  }

  @Test
  public void should_fail_if_parameter_with_value_has_no_value() throws MalformedURLException {
    URL url = new URL("http://assertj.org/news?article");
    String name = "article";
    String expectedValue = "10";
    List<String> actualValues = newArrayList((String)null);

    Throwable error = catchThrowable(() -> urls.assertHasParameter(info, url, name, expectedValue));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveParameter(url, name, expectedValue, actualValues));
  }

  @Test
  public void should_fail_if_parameter_with_value_has_multiple_no_values() throws MalformedURLException {
    URL url = new URL("http://assertj.org/news?article&article");
    String name = "article";
    String expectedValue = "10";
    List<String> actualValues = newArrayList(null, null);

    Throwable error = catchThrowable(() -> urls.assertHasParameter(info, url, name, expectedValue));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveParameter(url, name, expectedValue, actualValues));
  }

  @Test
  public void should_fail_if_parameter_with_value_is_wrong() throws MalformedURLException {
    URL url = new URL("http://assertj.org/news?article=11");
    String name = "article";
    String expectedValue = "10";
    List<String> actualValues = newArrayList("11");

    Throwable error = catchThrowable(() -> urls.assertHasParameter(info, url, name, expectedValue));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveParameter(url, name, expectedValue, actualValues));
  }

  @Test
  public void should_pass_if_parameter_with_value_is_found() throws MalformedURLException {
    urls.assertHasParameter(info, new URL("http://assertj.org/news?article=10"), "article", "10");
  }
}
