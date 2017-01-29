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

import org.assertj.core.error.BasicErrorMessageFactory;
import org.assertj.core.error.ErrorMessageFactory;

public class ShouldHaveFragment extends BasicErrorMessageFactory {
  public static ErrorMessageFactory shouldHaveFragment(URI actual, String expectedFragment) {
    return expectedFragment == null ? new ShouldHaveFragment(actual) : new ShouldHaveFragment(actual, expectedFragment);
  }

  private ShouldHaveFragment(URI actual, String expectedFragment) {
    super("%nExpecting fragment of%n  <%s>%nto be:%n  <%s>%nbut was:%n  <%s>", actual, expectedFragment,
          actual.getFragment());
  }

  private ShouldHaveFragment(URI actual) {
    super("%nExpecting URI:%n  <%s>%nnot to have a fragment but had:%n  <%s>", actual, actual.getFragment());
  }

}
