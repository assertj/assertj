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

/**
 * Returns {@code true} if the condition is not satisfied.
 * 
 * @author Nicolas François
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class DoesNotHave<T> extends Negative<T> {

  /**
   * Creates a new <code>{@link DoesNotHave}</code>.
   * 
   * @param <T> the type of value the {@link Condition} applies to.
   * @param condition the condition to inverse.
   * @return The Not condition created.
   */
  public static <T> DoesNotHave<T> doesNotHave(Condition<? super T> condition) {
    return new DoesNotHave<>(condition);
  }

  private DoesNotHave(Condition<? super T> condition) {
    super(condition);
  }

  @Override
  public String toString() {
    return String.format("does not have :<%s>", condition);
  }

}
