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

import com.google.common.collect.TreeRangeMap;

public class RangeMapAssert_containsKeys_Test extends RangeMapAssertBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_keys() throws Exception {
	// given

	// when
	Assertions.assertThat(actual).containsKeys(500, 600, 700);

	// then
  }

  @Test
  public void should_fail_if_actual_is_null() {
	expectException(AssertionError.class, actualIsNull());
	TreeRangeMap<Integer, String> actual = null;
	assertThat(actual).containsKeys(500, 600, 700);
  }

  @Test
  public void should_fail_if_keys_to_look_for_are_null() {
	expectException(IllegalArgumentException.class, "The keys to look for should not be null");
	assertThat(actual).containsKeys((Integer[]) null);
  }

  @Test
  public void should_fail_if_keys_to_look_for_are_empty() {
	expectException(IllegalArgumentException.class, "The keys to look for should not be empty");
	assertThat(actual).containsKeys();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_all_given_keys() {
	try {
	  assertThat(actual).containsKeys(100, 200, 900);
	} catch (AssertionError e) {
	  assertThat(e).hasMessage(String.format("%n" +
		                       "Expecting:%n" +
		                       "  <[[380‥450)=violet, [450‥495)=blue, [495‥570)=green, [570‥590)" +
		                       "=yellow, [590‥620)=orange, [620‥750)=red]>%n" +
		                       "to contain keys:%n" +
		                       "  <[100, 200, 900]>%n" +
		                       "but could not find:%n" +
		                       "  <[100, 200, 900]>"));
	  return;
	}
	fail("Assertion error expected");
  }

  @Test
  public void should_fail_if_actual_does_not_contain_the_given_key() {
	try {
	  assertThat(actual).containsKeys(100);
	} catch (AssertionError e) {
	  // error message shows that we were looking for a unique key (not many)
	  // @format:off
	  assertThat(e).hasMessage(String.format("%n" +
		                       "Expecting:%n" +
		                       "  <[[380‥450)=violet, [450‥495)=blue, [495‥570)=green, [570‥590)=yellow, [590‥620)=orange, [620‥750)=red]>%n" +
		                       "to contain key:%n" +
		                       "  <100>"));
	  // @format:on
	  return;
	}
	fail("Assertion error expected");
  }
}