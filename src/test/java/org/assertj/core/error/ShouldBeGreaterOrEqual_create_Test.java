/*
 * Created on Oct 19, 2010
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

import static org.assertj.core.error.ShouldBeGreaterOrEqual.shouldBeGreaterOrEqual;

import static org.junit.Assert.assertEquals;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.error.ShouldBeGreaterOrEqual;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.AbsValueComparator;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests for <code>{@link ShouldBeGreaterOrEqual#create(Description)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class ShouldBeGreaterOrEqual_create_Test {

  private ErrorMessageFactory factory;

  @Before
  public void setUp() {
    factory = shouldBeGreaterOrEqual(6, 8);
  }

  @Test
  public void should_create_error_message() {
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] expected:<6> to be greater than or equal to:<8>", message);
  }

  @Test
  public void should_create_error_message_with_custom_comparison_strategy() {
    factory = shouldBeGreaterOrEqual(6, 8, new ComparatorBasedComparisonStrategy(new AbsValueComparator<Integer>()));
    String message = factory.create(new TextDescription("Test"));
    assertEquals("[Test] expected:<6> to be greater than or equal to:<8> according to 'AbsValueComparator' comparator", message);
  }
}
