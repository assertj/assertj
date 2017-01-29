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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.ExpectedException.none;

import java.util.function.Predicate;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

public class Condition_constructor_with_predicate_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_create_condition_with_predicate() {
	Condition<String> fairyTale = new Condition<String>(s -> s.startsWith("Once upon a time"), "a %s tale", "fairy");
	String littleRedCap = "Once upon a time there was a dear little girl ...";
	assertThat(littleRedCap).is(fairyTale);
  }

  @SuppressWarnings("unused")
  @Test
  public void should_throw_error_if_predicate_is_null() {
	thrown.expect(NullPointerException.class);
	new Condition<Object>((Predicate<Object>) null, "");
  }
}

