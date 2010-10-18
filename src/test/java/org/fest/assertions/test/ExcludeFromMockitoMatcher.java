/*
 * Created on Oct 18, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.test;

import org.hamcrest.*;

/**
 * Matcher that excludes <code>{@link AssertionError}</code>s thrown by Mockito.
 *
 * @author Alex Ruiz
 */
class ExcludeFromMockitoMatcher extends BaseMatcher<Object> {

  static ExcludeFromMockitoMatcher excludeFromMockito() {
    return new ExcludeFromMockitoMatcher();
  }

  private ExcludeFromMockitoMatcher() {}

  public boolean matches(Object item) {
    if (!(item instanceof AssertionError)) return false;
    AssertionError error = (AssertionError) item;
    boolean matches = !error.getMessage().startsWith("\nWanted but not invoked:");
    return matches;
  }

  public void describeTo(Description description) {
    description.appendValue("Expecting an AssertionError, excluding the ones thrown by Mockito]");
  }
}
