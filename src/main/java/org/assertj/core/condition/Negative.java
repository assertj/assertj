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
package org.assertj.core.condition;

import org.assertj.core.api.Condition;
import org.assertj.core.util.VisibleForTesting;

/**
 * Inverse the condition.
 * 
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
public abstract class Negative<T> extends Condition<T> {

  @VisibleForTesting
  final Condition<? super T> condition;

  @Override
  public boolean matches(T value) {
    return !condition.matches(value);
  }

  protected Negative(Condition<? super T> condition) {
    this.condition = condition;
  }

}
