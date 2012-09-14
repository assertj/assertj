/*
 * Created on Nov 22, 2010
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

import static org.fest.assertions.error.ShouldBeSubsetOf.shouldBeSubsetOf;
import static org.fest.util.Lists.newArrayList;

import org.junit.Before;
import org.junit.Test;

import org.fest.assertions.description.Description;
import org.fest.assertions.description.TextDescription;
import org.fest.assertions.internal.ComparatorBasedComparisonStrategy;
import org.fest.assertions.util.CaseInsensitiveStringComparator;

/**
 * Tests for <code>{@link ShouldBeSubsetOf#create(Description)}</code>.
 * 
 * @author Maciej Jaskowski
 */
public class ShouldBeSubsetOf_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldBeSubsetOf(newArrayList("Yoda", "Luke"), newArrayList("Han", "Luke"), newArrayList("Yoda"));
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] expecting:\n" + "<['Yoda', 'Luke']>\n" + " to be subset of\n" + "<['Han', 'Luke']>\n"
        + " but found those extra elements:\n" + "<['Yoda']>", message);
  }

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
    ErrorMessageFactory factory = shouldBeSubsetOf(newArrayList("Yoda", "Luke"), newArrayList("Han", "Luke"), newArrayList("Yoda"),
        new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] expecting according to 'CaseInsensitiveStringComparator' comparator:\n" + "<['Yoda', 'Luke']>\n"
        + " to be subset of\n" + "<['Han', 'Luke']>\n" + " but found those extra elements:\n" + "<['Yoda']>", message);
  }
}
