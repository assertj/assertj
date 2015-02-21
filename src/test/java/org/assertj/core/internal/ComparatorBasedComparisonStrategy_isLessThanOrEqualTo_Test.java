/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.internal;

import static org.junit.Assert.*;

import java.awt.Rectangle;

import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.junit.Test;

/**
 * Tests for {@link ComparatorBasedComparisonStrategy#isLessThanOrEqualTo(Object, Object)}.
 * 
 * @author Joel Costigliola
 */
public class ComparatorBasedComparisonStrategy_isLessThanOrEqualTo_Test extends AbstractTest_ComparatorBasedComparisonStrategy {

  @Test
  public void should_pass() {
    String string = "string";
    String greaterString = "STRINGA";
    assertTrue(caseInsensitiveComparisonStrategy.isLessThanOrEqualTo(string, greaterString));
    assertTrue(caseInsensitiveComparisonStrategy.isLessThanOrEqualTo(string, "STRING"));
    assertTrue(caseInsensitiveComparisonStrategy.isLessThanOrEqualTo(string, string));
    assertFalse(caseInsensitiveComparisonStrategy.isLessThanOrEqualTo(greaterString, string));
    String lowerString = "stringA";
    assertTrue(caseInsensitiveComparisonStrategy.isLessThanOrEqualTo(string, lowerString));
    assertFalse(caseInsensitiveComparisonStrategy.isLessThanOrEqualTo(lowerString, string));
  }

  @Test
  public void should_fail_if_a_parameter_is_not_comparable() {
    thrown.expect(ClassCastException.class);
    caseInsensitiveComparisonStrategy.isLessThanOrEqualTo(new Rectangle(), new Rectangle());
  }

}
