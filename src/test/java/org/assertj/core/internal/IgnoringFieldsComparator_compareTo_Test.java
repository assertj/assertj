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
package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class IgnoringFieldsComparator_compareTo_Test {

  private IgnoringFieldsComparator ignoringFieldsComparator;

  @Before
  public void setUp() {
    ignoringFieldsComparator = new IgnoringFieldsComparator("thinking");
  }

  @Test
  public void should_return_true_if_both_Objects_are_null() {
	assertThat(ignoringFieldsComparator.compare(null, null)).isZero();
  }

  @Test
  public void should_return_are_not_equal_if_first_Object_is_null_and_second_is_not() {
	assertThat(ignoringFieldsComparator.compare(null, new DarthVader("I like you", "I'll kill you"))).isNotZero();
  }

  @Test
  public void should_return_are_not_equal_if_second_Object_is_null_and_first_is_not() {
	assertThat(ignoringFieldsComparator.compare(new DarthVader("I like you", "I'll kill you"), null)).isNotZero();
  }

  @Test
  public void should_return_true_if_all_but_ignored_fields_are_equal() {
	assertThat(ignoringFieldsComparator.compare(new DarthVader("I like you", "I'll kill you"),
	                                            new DarthVader("I like you", "I like you"))).isZero();
  }

  @Test
  public void should_return_false_if_all_but_ignored_fields_are_not_equal() {
	assertThat(ignoringFieldsComparator.compare(new DarthVader("I like you", "I'll kill you"),
	                                            new DarthVader("I'll kill you", "I'll kill you"))).isNotZero();
  }

  @Test
  public void should_return_false_if_Objects_do_not_have_the_same_properties() {
    assertThat(ignoringFieldsComparator.compare(new DarthVader("I like you", "I'll kill you"), 2)).isNotZero();
  }

  public static class DarthVader {

	public final String telling;
	public final String thinking;

	public DarthVader(String telling, String thinking) {
	  this.telling = telling;
	  this.thinking = thinking;
	}

  }

}