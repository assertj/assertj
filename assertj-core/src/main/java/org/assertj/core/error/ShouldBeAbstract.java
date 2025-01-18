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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.error;

import java.lang.reflect.Executable;

/**
 * Creates an error message indicating that an assertion that verifies
 * that a class or executable should be abstract failed.
 *
 * @author William Bakker
 */
public class ShouldBeAbstract extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldBeAbstract(Class<?> actual) {
    return new ShouldBeAbstract(actual);
  }

  public static ErrorMessageFactory shouldBeAbstract(Executable actual) {
    return new ShouldBeAbstract(actual);
  }

  private ShouldBeAbstract(Class<?> actual) {
    super("%nExpecting actual:%n  %s%nto be abstract", actual);
  }

  private ShouldBeAbstract(Executable actual) {
    super("%nExpecting actual:%n  %s%nto be abstract", actual);
  }
}
