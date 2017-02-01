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

import static org.assertj.core.error.uri.ShouldHaveParameter.shouldHaveParameter;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.assertj.core.internal.UrisBaseTest;
import org.junit.Test;

public class Uris_assertHasParameter_Test extends UrisBaseTest {

  @Test
  public void should_fail_if_parameter_is_missing() throws URISyntaxException {
    URI uri = new URI("http://assertj.org/news");
    String name = "article";

    try {
      uris.assertHasParameter(info, uri, name);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveParameter(uri, name));
      return;
    }

    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_parameter_has_no_value() throws URISyntaxException {
    uris.assertHasParameter(info, new URI("http://assertj.org/news?article"), "article");
  }

  @Test
  public void should_pass_if_parameter_has_value() throws URISyntaxException {
    uris.assertHasParameter(info, new URI("http://assertj.org/news?article=10"), "article");
  }

  @Test
  public void should_fail_if_parameter_without_value_is_missing() throws URISyntaxException {
    URI uri = new URI("http://assertj.org/news");
    String name = "article";
    String expectedValue = null;

    try {
      uris.assertHasParameter(info, uri, name, expectedValue);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveParameter(uri, name, expectedValue));
      return;
    }

    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_parameter_without_value_is_found() throws URISyntaxException {
    uris.assertHasParameter(info, new URI("http://assertj.org/news?article"), "article", null);
  }

  @Test
  public void should_fail_if_parameter_without_value_has_value() throws URISyntaxException {
    URI uri = new URI("http://assertj.org/news?article=11");
    String name = "article";
    String expectedValue = null;
    List<String> actualValue = newArrayList("11");

    try {
      uris.assertHasParameter(info, uri, name, expectedValue);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveParameter(uri, name, expectedValue, actualValue));
      return;
    }

    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_parameter_without_value_has_multiple_values() throws URISyntaxException {
    URI uri = new URI("http://assertj.org/news?article=11&article=12");
    String name = "article";
    String expectedValue = null;
    List<String> actualValues = newArrayList("11", "12");

    try {
      uris.assertHasParameter(info, uri, name, expectedValue);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveParameter(uri, name, expectedValue, actualValues));
      return;
    }

    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_parameter_with_value_is_missing() throws URISyntaxException {
    URI uri = new URI("http://assertj.org/news");
    String name = "article";
    String expectedValue = "10";

    try {
      uris.assertHasParameter(info, uri, name, expectedValue);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveParameter(uri, name, expectedValue));
      return;
    }

    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_parameter_with_value_has_no_value() throws URISyntaxException {
    URI uri = new URI("http://assertj.org/news?article");
    String name = "article";
    String expectedValue = "10";
    List<String> actualValues = newArrayList((String)null);

    try {
      uris.assertHasParameter(info, uri, name, expectedValue);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveParameter(uri, name, expectedValue, actualValues));
      return;
    }

    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_parameter_with_value_has_multiple_no_values() throws URISyntaxException {
    URI uri = new URI("http://assertj.org/news?article&article");
    String name = "article";
    String expectedValue = "10";
    List<String> actualValues = newArrayList(null, null);


    try {
      uris.assertHasParameter(info, uri, name, expectedValue);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveParameter(uri, name, expectedValue, actualValues));
      return;
    }

    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_parameter_with_value_is_wrong() throws URISyntaxException {
    URI uri = new URI("http://assertj.org/news?article=11");
    String name = "article";
    String expectedValue = "10";
    List<String> actualValues = newArrayList("11");

    try {
      uris.assertHasParameter(info, uri, name, expectedValue);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveParameter(uri, name, expectedValue, actualValues));
      return;
    }

    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_parameter_with_value_is_found() throws URISyntaxException {
    uris.assertHasParameter(info, new URI("http://assertj.org/news?article=10"), "article", "10");
  }
}
