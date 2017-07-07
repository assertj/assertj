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
package org.assertj.core.error;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Creates an error message indicating that an assertion that verifies that a class have an annotation.
 * 
 * @author William Delanoue
 * @author Joel Costigliola
 */
public class ShouldHaveAnnotations extends BasicErrorMessageFactory {

  /**
   * Creates a new <code>{@link org.assertj.core.error.ShouldHaveAnnotations}</code>.
   * 
   * @param actual the actual value in the failed assertion.
   * @param expected expected annotations for this class
   * @param missing missing annotations for this class
   * @return the created {@code ErrorMessageFactory}.
   */
  public static ErrorMessageFactory shouldHaveAnnotations(Class<?> actual,
      Collection<Class<? extends Annotation>> expected, Collection<Class<? extends Annotation>> missing) {
    return new ShouldHaveAnnotations(actual, expected, missing);
  }

  private ShouldHaveAnnotations(Class<?> actual, Collection<Class<? extends Annotation>> expected,
      Collection<Class<? extends Annotation>> missing) {
    super("%nExpecting%n  <%s>%nto have annotations:%n  <%s>%nbut the following annotations were not found:%n  <%s>", actual, expected, missing);
  }
}
