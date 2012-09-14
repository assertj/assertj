/*
 * Created on Sep 23, 2006
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
 * Copyright @2006-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import org.fest.assertions.internal.ComparatorBasedComparisonStrategy;
import org.junit.Test;

/**
 * Tests for {@link ComparatorBasedComparisonStrategy#toString()}.
 * 
 * @author Joel Costigliola
 */
public class ComparatorBasedComparisonStrategy_toString_Test extends AbstractTest_ComparatorBasedComparisonStrategy {

  @Test
  public void toString_with_anonymous_comparator() {
    ComparatorBasedComparisonStrategy lengthComparisonStrategy = new ComparatorBasedComparisonStrategy(new Comparator<String>() {
      public int compare(String s1, String s2) {
        return s1.length() - s2.length();
      }
    });
    assertEquals("'anonymous comparator class'", lengthComparisonStrategy.toString());
  }

  @Test
  public void toString_with_non_anonymous_comparator() {
    assertEquals("'CaseInsensitiveStringComparator'", caseInsensitiveComparisonStrategy.toString());
  }

}
