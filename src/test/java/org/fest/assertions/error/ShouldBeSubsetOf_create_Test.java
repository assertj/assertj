/*
 * Created on Nov 22, 2010
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
package org.fest.assertions.error;

import static junit.framework.Assert.assertEquals;

import static org.fest.assertions.error.ShouldBeSubsetOf.shouldBeSubsetOf;
import static org.fest.util.Collections.list;

import org.junit.Before;
import org.junit.Test;

import org.fest.assertions.description.Description;
import org.fest.assertions.description.TextDescription;
import org.fest.assertions.util.CaseInsensitiveStringComparator;
import org.fest.util.ComparatorBasedComparisonStrategy;

/**
 * Tests for <code>{@link ShouldBeSubsetOf#create(Description)}</code>.
 * 
 * @author Maciej Jaskowski
 */
public class ShouldBeSubsetOf_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldBeSubsetOf(list("Yoda", "Luke"), list("Han", "Luke"), list("Yoda"));
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] expecting:<['Yoda', 'Luke']> to be subset of <['Han', 'Luke']> but found those extra elements: <['Yoda']>", message);
  }

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
    ErrorMessageFactory factory = shouldBeSubsetOf(list("Yoda", "Luke"), list("Han", "Luke"), list("Yoda"), 
        new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] expecting:<['Yoda', 'Luke']> to be subset of <['Han', 'Luke']> "
        + "according to 'CaseInsensitiveStringComparator' comparator but found those extra elements: <['Yoda']>", message);
  }
}
