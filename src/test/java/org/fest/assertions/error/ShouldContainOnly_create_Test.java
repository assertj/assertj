/*
 * Created on Sep 17, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.error;

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.error.ShouldContainOnly.shouldContainOnly;
import static org.fest.util.Sets.newLinkedHashSet;
import static org.fest.util.Lists.newArrayList;

import org.fest.assertions.description.Description;
import org.fest.assertions.description.TextDescription;
import org.fest.assertions.internal.ComparatorBasedComparisonStrategy;
import org.fest.assertions.util.CaseInsensitiveStringComparator;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link ShouldContainOnly#create(Description)}</code>.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
public class ShouldContainOnly_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldContainOnly(newArrayList("Yoda", "Han"), newArrayList("Luke", "Yoda"), newLinkedHashSet("Luke"), newLinkedHashSet("Han"));
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] expecting:\n" + "<['Yoda', 'Han']>\n" + " to contain only:\n" + "<['Luke', 'Yoda']>\n"
        + " elements not found:\n" + "<['Luke']>\n" + " and elements not expected:\n" + "<['Han']>\n", message);
  }

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
    ErrorMessageFactory factory = shouldContainOnly(newArrayList("Yoda", "Han"), newArrayList("Luke", "Yoda"), newLinkedHashSet("Luke"), newLinkedHashSet("Han"),
        new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] expecting:\n" + "<['Yoda', 'Han']>\n" + " to contain only:\n" + "<['Luke', 'Yoda']>\n"
        + " elements not found:\n" + "<['Luke']>\n" + " and elements not expected:\n" + "<['Han']>\n"
        + " according to 'CaseInsensitiveStringComparator' comparator", message);
  }
}
