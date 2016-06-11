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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.error.uri;

import java.net.URI;
import java.net.URL;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

public class ShouldHaveParameter extends BasicErrorMessageFactory {

  private static final String SHOULD_HAVE_PARAMETER_BUT_MISSING = "%nExpecting:%n  <%s>%nto have parameter:%n  <%s>%nbut was absent";
  private static final String SHOULD_HAVE_PARAMETER_NO_VALUE_BUT_MISSING = "%nExpecting:%n  <%s>%nto have parameter:%n  <%s>%nwith no value, but was absent";
  private static final String SHOULD_HAVE_PARAMETER_NO_VALUE_BUT_VALUE = "%nExpecting:%n  <%s>%nto have parameter:%n  <%s>%nwith no value, but had value:%n  <%s>";
  private static final String SHOULD_HAVE_PARAMETER_VALUE_BUT_MISSING = "%nExpecting:%n  <%s>%nto have parameter:%n  <%s>%nwith value:%n  <%s>%nbut was absent";
  private static final String SHOULD_HAVE_PARAMETER_VALUE_BUT_NO_VALUE = "%nExpecting:%n  <%s>%nto have parameter:%n  <%s>%nwith value:%n  <%s>%nbut had no value";
  private static final String SHOULD_HAVE_PARAMETER_VALUE_BUT_WRONG = "%nExpecting:%n  <%s>%nto have parameter:%n  <%s>%nwith value:%n  <%s>%nbut had value:%n  <%s>";
  private static final String SHOULD_HAVE_NO_PARAMETER_BUT_NO_VALUE = "%nExpecting:%n  <%s>%nnot to have parameter:%n  <%s>%nbut was present with no value";
  private static final String SHOULD_HAVE_NO_PARAMETER_BUT_VALUE = "%nExpecting:%n  <%s>%nnot to have parameter:%n  <%s>%nbut was present with value:%n  <%s>";
  private static final String SHOULD_HAVE_NO_PARAMETER_NO_VALUE_BUT_FOUND = "%nExpecting:%n  <%s>%nnot to have parameter:%n  <%s>%nwith no value, but was present";
  private static final String SHOULD_HAVE_NO_PARAMETER_VALUE_BUT_FOUND = "%nExpecting:%n  <%s>%nnot to have parameter:%n  <%s>%nwith value:%n  <%s>%nbut was present";

  public static ErrorMessageFactory shouldHaveParameter(URI actual, String name) {
    return new ShouldHaveParameter(SHOULD_HAVE_PARAMETER_BUT_MISSING, actual, name);
  }

  public static ErrorMessageFactory shouldHaveParameter(URI actual, String name, String expectedValue) {
    if (expectedValue == null) {
      return new ShouldHaveParameter(SHOULD_HAVE_PARAMETER_NO_VALUE_BUT_MISSING, actual, name);
    }

    return new ShouldHaveParameter(SHOULD_HAVE_PARAMETER_VALUE_BUT_MISSING, actual, name, expectedValue);
  }

  public static ErrorMessageFactory shouldHaveParameter(URI actual, String name, String expectedValue,
                                                        String actualValue) {
    if (expectedValue == null) {
      return new ShouldHaveParameter(SHOULD_HAVE_PARAMETER_NO_VALUE_BUT_VALUE, actual, name, actualValue);
    }

    if (actualValue == null) {
      return new ShouldHaveParameter(SHOULD_HAVE_PARAMETER_VALUE_BUT_NO_VALUE, actual, name, expectedValue);
    }

    return new ShouldHaveParameter(SHOULD_HAVE_PARAMETER_VALUE_BUT_WRONG, actual, name, expectedValue, actualValue);
  }

  public static ErrorMessageFactory shouldHaveNoParameter(URI actual, String name, String actualValue) {
    if (actualValue == null) {
      return new ShouldHaveParameter(SHOULD_HAVE_NO_PARAMETER_BUT_NO_VALUE, actual, name);
    }

    return new ShouldHaveParameter(SHOULD_HAVE_NO_PARAMETER_BUT_VALUE, actual, name, actualValue);
  }

  public static ErrorMessageFactory shouldHaveNoParameter(URI actual, String name, String expectedValue,
                                                          String actualValue) {
    if (actualValue == null) {
      return new ShouldHaveParameter(SHOULD_HAVE_NO_PARAMETER_NO_VALUE_BUT_FOUND, actual, name);
    }

    return new ShouldHaveParameter(SHOULD_HAVE_NO_PARAMETER_VALUE_BUT_FOUND, actual, name, expectedValue);
  }

  private ShouldHaveParameter(String format, URI actual, String name) {
    super(format, actual, name);
  }

  private ShouldHaveParameter(String format, URI actual, String name, String value) {
    super(format, actual, name, value);
  }

  private ShouldHaveParameter(String format, URI actual, String name, String expectedValue, String actualValue) {
    super(format, actual, name, expectedValue, actualValue);
  }

  public static ErrorMessageFactory shouldHaveParameter(URL actual, String name) {
    return new ShouldHaveParameter(SHOULD_HAVE_PARAMETER_BUT_MISSING, actual, name);
  }

  public static ErrorMessageFactory shouldHaveParameter(URL actual, String name, String expectedValue) {
    if (expectedValue == null) {
      return new ShouldHaveParameter(SHOULD_HAVE_PARAMETER_NO_VALUE_BUT_MISSING, actual, name);
    }

    return new ShouldHaveParameter(SHOULD_HAVE_PARAMETER_VALUE_BUT_MISSING, actual, name, expectedValue);
  }

  public static ErrorMessageFactory shouldHaveParameter(URL actual, String name, String expectedValue,
                                                        String actualValue) {
    if (expectedValue == null) {
      return new ShouldHaveParameter(SHOULD_HAVE_PARAMETER_NO_VALUE_BUT_VALUE, actual, name, actualValue);
    }

    if (actualValue == null) {
      return new ShouldHaveParameter(SHOULD_HAVE_PARAMETER_VALUE_BUT_NO_VALUE, actual, name, expectedValue);
    }

    return new ShouldHaveParameter(SHOULD_HAVE_PARAMETER_VALUE_BUT_WRONG, actual, name, expectedValue, actualValue);
  }

  public static ErrorMessageFactory shouldHaveNoParameter(URL actual, String name, String actualValue) {
    if (actualValue == null) {
      return new ShouldHaveParameter(SHOULD_HAVE_NO_PARAMETER_BUT_NO_VALUE, actual, name);
    }

    return new ShouldHaveParameter(SHOULD_HAVE_NO_PARAMETER_BUT_VALUE, actual, name, actualValue);
  }

  public static ErrorMessageFactory shouldHaveNoParameter(URL actual, String name, String expectedValue,
                                                          String actualValue) {
    if (actualValue == null) {
      return new ShouldHaveParameter(SHOULD_HAVE_NO_PARAMETER_NO_VALUE_BUT_FOUND, actual, name);
    }

    return new ShouldHaveParameter(SHOULD_HAVE_NO_PARAMETER_VALUE_BUT_FOUND, actual, name, expectedValue);
  }

  private ShouldHaveParameter(String format, URL actual, String name) {
    super(format, actual, name);
  }

  private ShouldHaveParameter(String format, URL actual, String name, String value) {
    super(format, actual, name, value);
  }

  private ShouldHaveParameter(String format, URL actual, String name, String expectedValue, String actualValue) {
    super(format, actual, name, expectedValue, actualValue);
  }
}
