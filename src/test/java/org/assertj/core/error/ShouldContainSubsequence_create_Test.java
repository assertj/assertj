/*
 * Created on Aug 17, 2013
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
 * Copyright @2010-2013 the original author or authors.
 */
package org.assertj.core.error;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.error.ShouldContainSubsequence.shouldContainSubsequence;
import static org.assertj.core.util.Lists.newArrayList;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link ShouldContainSubsequence#create(Description)}</code>.
 * 
 * @author Marcin Mikosik
 */
public class ShouldContainSubsequence_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldContainSubsequence(newArrayList("Yoda", "Luke"),
        newArrayList("Han", "Leia"));
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] \nExpecting:\n <['Yoda', 'Luke']>\nto contain subsequence:\n <['Han', 'Leia']>\n", message);
  }

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
    ErrorMessageFactory factory = shouldContainSubsequence(newArrayList("Yoda", "Luke"), newArrayList("Han", "Leia"),
        new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.instance));
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] \nExpecting:\n <['Yoda', 'Luke']>\nto contain subsequence:\n <['Han', 'Leia']>\n"
        + "according to 'CaseInsensitiveStringComparator' comparator", message);
  }
}
