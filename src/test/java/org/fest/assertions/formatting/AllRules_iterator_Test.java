/*
 * Created on Dec 3, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.formatting;

import static java.util.Collections.unmodifiableList;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for <code>{@link AllRules#iterator()}</code>.
 *
 * @author Alex Ruiz
 */
public class AllRules_iterator_Test {

  private static AllRules allRules;

  @BeforeClass public static void setUpOnce() {
    allRules = new AllRules();
  }

  @Test public void should_return_all_implemented_rules() {
    List<ToStringRule> rules = rulesList();
    assertEquals(8, rules.size());
    assertContainsInstance(rules, ArrayToStringRule.class);
    assertContainsInstance(rules, ClassToStringRule.class);
    assertContainsInstance(rules, CollectionToStringRule.class);
    assertContainsInstance(rules, ConditionToStringRule.class);
    assertContainsInstance(rules, DimensionToStringRule.class);
    assertContainsInstance(rules, FileToStringRule.class);
    assertContainsInstance(rules, MapToStringRule.class);
    assertContainsInstance(rules, StringToStringRule.class);
  }

  private List<ToStringRule> rulesList() {
    List<ToStringRule> rules = new ArrayList<ToStringRule>();
    for (ToStringRule rule : allRules) rules.add(rule);
    return unmodifiableList(rules);
  }

  private void assertContainsInstance(List<ToStringRule> rules, Class<? extends ToStringRule> expected) {
    for (ToStringRule rule : rules)
      if (expected.isInstance(rule)) return;
    throw new AssertionError(String.format("Unable to find instance of %s in %s", expected.getName(), rules));
  }
}
