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
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveNoParameter;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveNoParameters;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import java.net.URI;
import java.util.List;

import org.assertj.core.internal.UrisBaseTest;
import org.junit.jupiter.api.Test;

public class Uris_assertHasNoParameter_Test extends UrisBaseTest {

  @Test
  public void should_pass_if_parameter_is_missing() {
    uris.assertHasNoParameter(info, URI.create("http://assertj.org/news"), "article");
  }

  @Test
  public void should_fail_if_parameter_is_present_without_value() {
    URI uri = URI.create("http://assertj.org/news?article");
    String name = "article";
    List<String> actualValues = newArrayList((String)null);

    Throwable error = catchThrowable(() -> uris.assertHasNoParameter(info, uri, name));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveNoParameter(uri, name, actualValues));
  }

  @Test
  public void should_fail_if_parameter_is_present_with_value() {
    URI uri = URI.create("http://assertj.org/news?article=10");
    String name = "article";
    List<String> actualValue = newArrayList("10");

    Throwable error = catchThrowable(() -> uris.assertHasNoParameter(info, uri, name));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveNoParameter(uri, name, actualValue));
  }

  @Test
  public void should_fail_if_parameter_is_present_multiple_times() {
    URI uri = URI.create("http://assertj.org/news?article&article=10");
    String name = "article";
    List<String> actualValues = newArrayList(null, "10");

    Throwable error = catchThrowable(() -> uris.assertHasNoParameter(info, uri, name));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveNoParameter(uri, name, actualValues));
  }

  @Test
  public void should_pass_if_parameter_without_value_is_missing() {
    uris.assertHasNoParameter(info, URI.create("http://assertj.org/news"), "article", null);
  }

  @Test
  public void should_fail_if_parameter_without_value_is_present() {
    URI uri = URI.create("http://assertj.org/news?article");
    String name = "article";
    String expectedValue = null;
    List<String> actualValues = newArrayList((String)null);

    Throwable error = catchThrowable(() -> uris.assertHasNoParameter(info, uri, name, expectedValue));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveNoParameter(uri, name, expectedValue, actualValues));
  }

  @Test
  public void should_pass_if_parameter_without_value_is_present_with_value() {
    uris.assertHasNoParameter(info, URI.create("http://assertj.org/news=10"), "article", null);
  }

  @Test
  public void should_pass_if_parameter_with_value_is_missing() {
    uris.assertHasNoParameter(info, URI.create("http://assertj.org/news"), "article", "10");
  }

  @Test
  public void should_pass_if_parameter_with_value_is_present_without_value() {
    uris.assertHasNoParameter(info, URI.create("http://assertj.org/news?article"), "article", "10");
  }

  @Test
  public void should_pass_if_parameter_with_value_is_present_with_wrong_value() {
    uris.assertHasNoParameter(info, URI.create("http://assertj.org/news?article=11"), "article", "10");
  }

  @Test
  public void should_fail_if_parameter_with_value_is_present() {
    URI uri = URI.create("http://assertj.org/news?article=10");
    String name = "article";
    String expectedValue = "10";
    List<String> actualValue = newArrayList("10");

    Throwable error = catchThrowable(() -> uris.assertHasNoParameter(info, uri, name, expectedValue));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveNoParameter(uri, name, expectedValue, actualValue));
  }

  @Test
  public void should_pass_if_uri_has_no_parameters() {
    uris.assertHasNoParameters(info, URI.create("http://assertj.org/news"));
  }

  @Test
  public void should_fail_if_uri_has_some_parameters() {
    URI uri = URI.create("http://assertj.org/news?article=10&locked=false");

    Throwable error = catchThrowable(() -> uris.assertHasNoParameters(info, uri));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveNoParameters(uri, newLinkedHashSet("article", "locked")));
  }

  @Test
  public void should_fail_if_uri_has_one_parameter() {
    URI uri = URI.create("http://assertj.org/news?article=10");

    Throwable error = catchThrowable(() -> uris.assertHasNoParameters(info, uri));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldHaveNoParameters(uri, newLinkedHashSet("article")));
  }
}
