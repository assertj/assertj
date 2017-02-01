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
package org.assertj.core.api;

import java.text.DateFormat;
import java.util.Date;


/**
 * Assertions for {@link Date}s.
 * <p>
 * To create a new instance of this class invoke <code>{@link Assertions#assertThat(Date)}</code>.
 * </p>
 * Note that assertions with date parameter comes with two flavor, one is obviously a {@link Date} and the other is a String
 * representing a Date.<br>
 * For the latter, the default format follows ISO 8901 : "yyyy-MM-dd", user can override it with a custom format by calling
 * {@link #withDateFormat(DateFormat)}.<br>
 * The user custom format will then be used for all next Date assertions (i.e not limited to the current assertion) in the test
 * suite.<br>
 * To turn back to default format, simply call {@link #withDefaultDateFormatsOnly()}.
 * 
 * @author Tomasz Nurkiewicz (thanks for giving assertions idea)
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
public class DateAssert extends AbstractDateAssert<DateAssert> {

  /**
   * Creates a new {@link DateAssert}.
   * @param actual the target to verify.
   */
  public DateAssert(Date actual) {
    super(actual, DateAssert.class);
  }
}
