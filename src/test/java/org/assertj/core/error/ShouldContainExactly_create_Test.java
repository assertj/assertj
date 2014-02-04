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

import static junit.framework.Assert.assertEquals;

import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for <code>{@link ShouldContainExactly#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 * 
 * @author Joel Costigliola
 */
public class ShouldContainExactly_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldContainExactly(newArrayList("Yoda", "Han"), newArrayList("Luke", "Yoda"), newLinkedHashSet("Luke"),
        newLinkedHashSet("Han"));
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertEquals(
        "[Test] \nExpecting:\n <[\"Yoda\", \"Han\"]>\nto contain exactly (and in same order):\n"
            + " <[\"Luke\", \"Yoda\"]>\nbut some elements were not found:\n <[\"Luke\"]>\nand others were not expected:\n <[\"Han\"]>\n",
        message);
  }

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
    ErrorMessageFactory factory = shouldContainExactly(newArrayList("Yoda", "Han"), newArrayList("Luke", "Yoda"),
        newLinkedHashSet("Luke"), newLinkedHashSet("Han"), new ComparatorBasedComparisonStrategy(
            CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertEquals("[Test] \nExpecting:\n <[\"Yoda\", \"Han\"]>\nto contain exactly (and in same order):\n"
        + " <[\"Luke\", \"Yoda\"]>\nbut some elements were not found:\n <[\"Luke\"]>\nand others were not expected:\n"
        + " <[\"Han\"]>\naccording to 'CaseInsensitiveStringComparator' comparator", message);
  }

  @Test
  public void should_create_error_message_when_only_elements_order_differs() {
    factory = shouldContainExactly("Luke", "Han", 1);
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertEquals(
        "[Test] \nActual and expected have the same elements but not in the same order, at index 1 actual element was:\n"
            + " <\"Luke\">\nwhereas expected element was:\n <\"Han\">\n", message);
  }

  @Test
  public void should_create_error_message_when_only_elements_order_differs_according_to_custom_comparison_strategy() {
    factory = shouldContainExactly("Luke", "Han", 1, new ComparatorBasedComparisonStrategy(
        CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    assertEquals(
        "[Test] \nActual and expected have the same elements but not in the same order, at index 1 actual element was:\n"
            + " <\"Luke\">\nwhereas expected element was:\n <\"Han\">\naccording to 'CaseInsensitiveStringComparator' comparator",
        message);
  }
}
