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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.assertj.guava.data.MapEntry.entry;
import static org.junit.Assert.fail;

import org.assertj.guava.data.MapEntry;
import org.junit.Test;

public class RangeMapAssert_contains_Test extends RangeMapAssertBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_entries() {
	assertThat(actual).contains(entry(400, "violet"));
	assertThat(actual).contains(entry(420, "violet"), entry(595, "orange"));
  }

  @Test
  public void should_fail_if_actual_is_null() {
	expectException(AssertionError.class, actualIsNull());
	actual = null;
	assertThat(actual).contains(entry(400, "violet"));
  }

  @Test
  public void should_fail_if_entries_to_look_for_are_null() {
	expectException(IllegalArgumentException.class, "The entries to look for should not be null");
	assertThat(actual).contains((MapEntry<Integer, String>[]) null);
  }

  @Test
  public void should_fail_if_entries_to_look_for_are_empty() {
	expectException(IllegalArgumentException.class, "The entries to look for should not be empty");
	assertThat(actual).contains();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_all_given_entries() {
	try {
	  assertThat(actual).contains(entry(400, "violet"), entry(100, "violet"), entry(500, "pink"));
	} catch (AssertionError e) {
	  // @format:off
	  assertThat(e).hasMessage(format("%n"+
	                           "Expecting:%n" +
		                       " <[[380‥450)=violet, [450‥495)=blue, [495‥570)=green, [570‥590)=yellow, [590‥620)=orange, [620‥750)=red]>%n" +
		                       "to contain:%n" +
		                       " <[MapEntry[key=400, value='violet'],%n"+
		                       "    MapEntry[key=100, value='violet'],%n"+
		                       "    MapEntry[key=500, value='pink']]>%n" +
		                       "but could not find:%n" +
		                       " <[MapEntry[key=100, value='violet'], MapEntry[key=500, value='pink']]>%n"));
	  // @format:on
	  return;
	}
	fail("Assertion error expected");
  }
}