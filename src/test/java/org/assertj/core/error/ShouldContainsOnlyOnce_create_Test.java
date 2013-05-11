/*
 * Created on Sep 17, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.error;

import static org.assertj.core.error.ShouldContainsOnlyOnce.shouldContainsOnlyOnce;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.junit.Assert.assertEquals;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link ShouldContainsOnlyOnce#create(Description)}</code>.
 * 
 * @author William Delanoue
 */
public class ShouldContainsOnlyOnce_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldContainsOnlyOnce(newArrayList("Yoda", "Han"), newArrayList("Luke", "Yoda"),
        newLinkedHashSet("Luke"), newLinkedHashSet("Han"));
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] \nExpecting:\n <['Yoda', 'Han']>\nto contain only once:\n <['Luke', 'Yoda']>\n"
        + "elements not found:\n <['Luke']>\nand elements more than once:\n <['Han']>\n", message);
  }

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
    ErrorMessageFactory factory = shouldContainsOnlyOnce(newArrayList("Yoda", "Han"), newArrayList("Luke", "Yoda"),
        newLinkedHashSet("Luke"), newLinkedHashSet("Han"), new ComparatorBasedComparisonStrategy(
            CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] \nExpecting:\n <['Yoda', 'Han']>\nto contain only once:\n <['Luke', 'Yoda']>\n"
        + "elements not found:\n <['Luke']>\nand elements more than once:\n <['Han']>\n"
        + "according to 'CaseInsensitiveStringComparator' comparator", message);
  }
}
