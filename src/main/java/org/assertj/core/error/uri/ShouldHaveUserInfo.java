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
package org.assertj.core.error.uri;

import java.net.URI;
import java.net.URL;

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

public class ShouldHaveUserInfo extends BasicErrorMessageFactory {

  private static final String SHOULD_HAVE_NO_USER_INFO = "%nExpecting:%n  <%s>%nnot to have user info but had:%n  <%s>";
  private static final String SHOULD_HAVE_USER_INFO = "%nExpecting user info of%n  <%s>%nto be:%n  <%s>%nbut was:%n  <%s>";

  public static ErrorMessageFactory shouldHaveUserInfo(URI actual, String expectedUserInfo) {
    return expectedUserInfo == null ? new ShouldHaveUserInfo(actual) : new ShouldHaveUserInfo(actual, expectedUserInfo);
  }

  private ShouldHaveUserInfo(URI actual, String expectedUserInfo) {
    super(SHOULD_HAVE_USER_INFO, actual, expectedUserInfo, actual.getUserInfo());
  }

  private ShouldHaveUserInfo(URI actual) {
    super(SHOULD_HAVE_NO_USER_INFO, actual, actual.getUserInfo());
  }

  public static ErrorMessageFactory shouldHaveUserInfo(URL actual, String expectedUserInfo) {
    return expectedUserInfo == null ? new ShouldHaveUserInfo(actual) : new ShouldHaveUserInfo(actual, expectedUserInfo);
  }

  private ShouldHaveUserInfo(URL actual, String expectedUserInfo) {
    super(SHOULD_HAVE_USER_INFO, actual, expectedUserInfo, actual.getUserInfo());
  }

  private ShouldHaveUserInfo(URL actual) {
    super(SHOULD_HAVE_NO_USER_INFO, actual, actual.getUserInfo());
  }
}
