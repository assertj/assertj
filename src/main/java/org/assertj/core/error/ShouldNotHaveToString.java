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
package org.assertj.core.error;

public class ShouldNotHaveToString extends BasicErrorMessageFactory {

  public static ErrorMessageFactory shouldNotHaveToString(Object actual, String other) {
    return new ShouldNotHaveToString(actual, other);
  }

  private ShouldNotHaveToString(Object actual, String other) {
    super("%nExpecting actual's toString() not to be equal to:%n  <%s>%nbut was:%n  <%s>", other, actual.toString());
  }
}
