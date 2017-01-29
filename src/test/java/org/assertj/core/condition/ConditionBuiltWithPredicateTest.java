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

import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.Set;
import java.util.function.Predicate;

import org.assertj.core.api.Condition;
import org.assertj.core.api.WithAssertions;
import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ConditionBuiltWithPredicateTest implements WithAssertions {

  @Rule
  public ExpectedException thrown = none();
  private final Set<String> jedis = newLinkedHashSet("Luke", "Yoda", "Obiwan");
  private Condition<String> jedi;
  private Condition<String> jediPower;
  private static final String JEDI = "jedi";

  @Before
  public void setup() {
	Predicate<String> jediPredicate = s -> jedis.contains(s);
	jedi = new Condition<String>(jediPredicate, "a %s", JEDI);
	jediPower = new Condition<String>(jediPredicate, "%s power", JEDI);
  }

  @Test
  public void is_condition_should_be_met() {
	assertThat("Luke").is(jedi);
  }

  @Test
  public void has_condition_should_be_met() {
	assertThat("Luke").has(jediPower);
  }
  
  @Test
  public void isNot_condition_should_be_met() {
	assertThat("Vader").isNot(jedi).doesNotHave(jediPower);
  }

  @Test
  public void doesNotHave_condition_should_be_met() {
	assertThat("Vader").doesNotHave(jediPower);
  }

  @Test
  public void should_fail_if_condition_is_not_met() {
    thrown.expectAssertionError("%n" +
                                "Expecting:%n" +
                                " <\"Vader\">%n" +
                                "to be <a jedi>");
    assertThat("Vader").is(jedi);
  }
}
