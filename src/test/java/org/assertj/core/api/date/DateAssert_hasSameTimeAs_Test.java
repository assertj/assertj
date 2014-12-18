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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.api.date;

import org.assertj.core.api.DateAssertBaseTest;
import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

/**
 * Tests for <code>{@link org.assertj.core.api.DateAssert#hasSameTimeAs(java.util.Date)} </code>.
 *
 * @author Alexander Bischof
 */
public class DateAssert_hasSameTimeAs_Test extends DateAssertBaseTest {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_verify_that_actual_has_time_equals_to_expected() {
	Date releaseDate = new Date();
	java.sql.Timestamp sqlDate = new java.sql.Timestamp(releaseDate.getTime());
	assertThat(releaseDate).hasSameTimeAs(sqlDate);
	assertThat(sqlDate).hasSameTimeAs(releaseDate);
  }

  @Test
  public void should_throw_exception_when_date_is_null() {
	thrown.expectAssertionError("Expecting actual not to be null");

	Date releaseDate = new Date();
	assertThat(releaseDate).hasSameTimeAs(null);
  }
}
