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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.guava.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

public class RangeMapAssert_containsValues_Test extends RangeMapAssertBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values() {
	assertThat(actual).containsValues("violet");
	assertThat(actual).containsValues("blue", "red");
  }

  @Test
  public void should_fail_if_actual_is_null() {
	expectException(AssertionError.class, actualIsNull());
	actual = null;
	assertThat(actual).containsValues("violet", "red");
  }

  @Test
  public void should_fail_if_values_to_look_for_are_null() {
	expectException(IllegalArgumentException.class, "The values to look for should not be null");
	assertThat(actual).containsValues((String[]) null);
  }

  @Test
  public void should_fail_if_values_to_look_for_are_empty() {
	expectException(IllegalArgumentException.class, "The values to look for should not be empty");
	assertThat(actual).containsValues();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_all_given_values() {
	try {
	  assertThat(actual).containsValues("violet", "black");
	} catch (AssertionError e) {
	  // @format:off
	  assertThat(e).hasMessage(String.format("%n" +
		                       "Expecting:%n" +
		                       "  <[[380..450)=violet, [450..495)=blue, [495..570)=green, [570..590)=yellow, [590..620)=orange, [620..750)=red]>%n" +
		                       "to contain values:%n" +
		                       "  <[\"violet\", \"black\"]>%n" +
		                       "but could not find:%n" +
		                       "  <[\"black\"]>"));
	  // @format:on
	  return;
	}
	fail("Assertion error expected");
  }

  @Test
  public void should_fail_if_actual_does_not_contain_the_given_value() {
	try {
	  assertThat(actual).containsValues("black");
	} catch (AssertionError e) {
	  // error message shows that we were looking for a unique value (not many)
	  // @format:off
	  assertThat(e).hasMessage(String.format("%n" +
		                       "Expecting:%n" +
		                       "  <[[380..450)=violet, [450..495)=blue, [495..570)=green, [570..590)=yellow, [590..620)=orange, [620..750)=red]>%n" +
		                       "to contain value:%n" +
		                       "  <\"black\">"));
	  // @format:on
	  return;
	}
	fail("Assertion error expected");
  }
}