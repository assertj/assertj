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
package org.assertj.core.data;

import java.time.temporal.Temporal;

/**
 * {@link Temporal} offset.
 * @param <T> the type of the {@code Temporal} values to be checked against offset.
 * @since 3.7.0
 */
public interface TemporalOffset<T extends Temporal> {

  /**
   * Checks if difference between temporal values is beyond offset.
   * @param temporal1 first temporal value to be validated against second temporal value.
   * @param temporal2 second temporal value.
   * @return true if difference between temporal values is beyond offset.
   */
  boolean isBeyondOffset(T temporal1, T temporal2);

  /**
   * Returns description of the difference between temporal values and expected offset details.
   * Is designed for the case when difference is beyond offset.
   * @param temporal1 first temporal value which is being validated against second temporal value.
   * @param temporal2 second temporal value.
   * @return difference description.
   */
  String getBeyondOffsetDifferenceDescription(T temporal1, T temporal2);

}
