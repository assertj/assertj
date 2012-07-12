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

import static org.fest.assertions.error.ShouldBeSorted.shouldBeSortedAccordingToGivenComparator;
import static org.fest.util.Arrays.array;

import java.util.Comparator;

import org.junit.Test;

import org.fest.assertions.description.Description;
import org.fest.assertions.internal.TestDescription;
import org.fest.assertions.util.CaseInsensitiveStringComparator;

/**
 * Tests for <code>{@link ShouldBeSorted#create(Description)}</code>.
 * 
 * @author Joel Costigliola
 */
public class ShouldBeSortedAccordingToComparator_create_Test {

  @Test
  public void should_create_error_message_with_comparator() {
    ErrorMessageFactory factory = shouldBeSortedAccordingToGivenComparator(1, array("b", "c", "A"),
        new CaseInsensitiveStringComparator());
    String message = factory.create(new TestDescription("Test"));
    assertEquals(
        "[Test] group is not sorted according to 'CaseInsensitiveStringComparator' comparator because element 1:<'c'> is not less or equal than element 2:<'A'>.\n"
            + "group was:\n" + "<['b', 'c', 'A']>", message);
  }

  @Test
  public void should_create_error_message_with_private_static_comparator() {
    ErrorMessageFactory factory = shouldBeSortedAccordingToGivenComparator(1, array("b", "c", "a"), new StaticStringComparator());
    String message = factory.create(new TestDescription("Test"));
    assertEquals(
        "[Test] group is not sorted according to 'StaticStringComparator' comparator because element 1:<'c'> is not less or equal than element 2:<'a'>.\n"
            + "group was:\n" + "<['b', 'c', 'a']>", message);
  }

  private static class StaticStringComparator implements Comparator<String> {
    public int compare(String s1, String s2) {
      return s1.compareTo(s2);
    }
  }
}
