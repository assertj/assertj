/*
 * Created on Dec 3, 2010
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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.formatting;

import static org.fest.util.Collections.list;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link ToStringConverter#toString()}</code>.
 *
 * @author Alex Ruiz
 */
public class ToStringConverter_toStringOf_Test {

  private ToStringRule rule;
  private AllRules rules;
  private ToStringConverter converter;

  @Before public void setUp() {
    rule = mock(ToStringRule.class);
    rules = mock(AllRules.class);
    converter = new ToStringConverter(rules);
  }

  @Test public void should_delegate_to_ToStringRule_that_can_handle_object_type() {
    when(rules.iterator()).thenReturn(list(rule).iterator());
    when(rule.canHandle("Yoda")).thenReturn(true);
    when(rule.toStringOf("Yoda")).thenReturn("Jedi");
    assertEquals("Jedi", converter.toStringOf("Yoda"));
  }

  @Test public void should_delegate_to_default_ToStringRule_if_a_matching_ToStringRule_cannot_be_found() {
    when(rules.iterator()).thenReturn(list(rule).iterator());
    when(rule.canHandle("Yoda")).thenReturn(false);
    ToStringRule defaultRule = mock(ToStringRule.class);
    when(rules.defaultRule()).thenReturn(defaultRule);
    when(defaultRule.toStringOf("Yoda")).thenReturn("Jedi");
    assertEquals("Jedi", converter.toStringOf("Yoda"));
  }
}
