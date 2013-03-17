/*
 * Created on Nov 20, 2010
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
package org.assertj.core.error;

import static junit.framework.Assert.assertEquals;

import static org.assertj.core.data.Index.atIndex;
import static org.assertj.core.error.ShouldContainAtIndex.shouldContainAtIndex;
import static org.assertj.core.util.Lists.newArrayList;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.error.ShouldContainAtIndex;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Test;


/**
 * Tests for <code>{@link ShouldContainAtIndex#create(Description)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ShouldContainAtIndex_create_Test {

  @Test
  public void should_create_error_message() {
    ErrorMessageFactory factory = shouldContainAtIndex(newArrayList("Yoda", "Luke"), "Leia", atIndex(1), "Luke");
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] expecting:<'Leia'> at index <1> but found <'Luke'> in:\n" + " <['Yoda', 'Luke']>\n", message);
  }

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
    ErrorMessageFactory factory = shouldContainAtIndex(newArrayList("Yoda", "Luke"), "Leia", atIndex(1), "Luke",
        new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] expecting:<'Leia'> at index <1> but found <'Luke'> in:\n" + " <['Yoda', 'Luke']>\n"
        + " according to 'CaseInsensitiveStringComparator' comparator", message);
  }
}
