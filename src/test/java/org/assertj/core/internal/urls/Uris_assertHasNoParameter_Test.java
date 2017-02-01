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
package org.assertj.core.internal.urls;

import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveNoParameter;
import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveNoParameters;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.assertj.core.internal.UrisBaseTest;
import org.junit.Test;

public class Uris_assertHasNoParameter_Test extends UrisBaseTest {

  @Test
  public void should_pass_if_parameter_is_missing() throws URISyntaxException {
    uris.assertHasNoParameter(info, new URI("http://assertj.org/news"), "article");
  }

  @Test
  public void should_fail_if_parameter_is_present_without_value() throws URISyntaxException {
    URI uri = new URI("http://assertj.org/news?article");
    String name = "article";
    List<String> actualValues = newArrayList((String)null);

    try {
      uris.assertHasNoParameter(info, uri, name);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveNoParameter(uri, name, actualValues));
      return;
    }

    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_parameter_is_present_with_value() throws URISyntaxException {
    URI uri = new URI("http://assertj.org/news?article=10");
    String name = "article";
    List<String> actualValue = newArrayList("10");

    try {
      uris.assertHasNoParameter(info, uri, name);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveNoParameter(uri, name, actualValue));
      return;
    }

    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_parameter_is_present_multiple_times() throws URISyntaxException {
    URI uri = new URI("http://assertj.org/news?article&article=10");
    String name = "article";
    List<String> actualValues = newArrayList(null, "10");

    try {
      uris.assertHasNoParameter(info, uri, name);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveNoParameter(uri, name, actualValues));
      return;
    }

    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_parameter_without_value_is_missing() throws URISyntaxException {
    uris.assertHasNoParameter(info, new URI("http://assertj.org/news"), "article", null);
  }

  @Test
  public void should_fail_if_parameter_without_value_is_present() throws URISyntaxException {
    URI uri = new URI("http://assertj.org/news?article");
    String name = "article";
    String expectedValue = null;
    List<String> actualValues = newArrayList((String)null);

    try {
      uris.assertHasNoParameter(info, uri, name, expectedValue);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveNoParameter(uri, name, expectedValue, actualValues));
      return;
    }

    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_parameter_without_value_is_present_with_value() throws URISyntaxException {
    uris.assertHasNoParameter(info, new URI("http://assertj.org/news=10"), "article", null);
  }

  @Test
  public void should_pass_if_parameter_with_value_is_missing() throws URISyntaxException {
    uris.assertHasNoParameter(info, new URI("http://assertj.org/news"), "article", "10");
  }

  @Test
  public void should_pass_if_parameter_with_value_is_present_without_value() throws URISyntaxException {
    uris.assertHasNoParameter(info, new URI("http://assertj.org/news?article"), "article", "10");
  }

  @Test
  public void should_pass_if_parameter_with_value_is_present_with_wrong_value() throws URISyntaxException {
    uris.assertHasNoParameter(info, new URI("http://assertj.org/news?article=11"), "article", "10");
  }

  @Test
  public void should_fail_if_parameter_with_value_is_present() throws URISyntaxException {
    URI uri = new URI("http://assertj.org/news?article=10");
    String name = "article";
    String expectedValue = "10";
    List<String> actualValue = newArrayList("10");

    try {
      uris.assertHasNoParameter(info, uri, name, expectedValue);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveNoParameter(uri, name, expectedValue, actualValue));
      return;
    }

    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_uri_has_no_parameters() throws URISyntaxException {
    uris.assertHasNoParameters(info, new URI("http://assertj.org/news"));
  }

  @Test
  public void should_fail_if_uri_has_some_parameters() throws URISyntaxException {
    URI uri = new URI("http://assertj.org/news?article=10&locked=false");

    try {
      uris.assertHasNoParameters(info, uri);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveNoParameters(uri, newLinkedHashSet("article", "locked")));
      return;
    }

    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_uri_has_one_parameter() throws URISyntaxException {
    URI uri = new URI("http://assertj.org/news?article=10");

    try {
      uris.assertHasNoParameters(info, uri);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveNoParameters(uri, newLinkedHashSet("article")));
      return;
    }

    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
